package taskone;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;

class ThreadedServer {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: gradle runTask2 -Pport=<port>");
            System.exit(1);
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("[Port] must be an integer");
            return;
        }
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("ThreadedServer started on port " + port);

        // Shared state for all clients
        StringList sharedList = new StringList();
        Performer performer = new Performer(sharedList);

        // Unbounded threads: accept and spawn handler for each client
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
            new Thread(() -> handleClient(clientSocket, performer)).start();
        }
    }

    /**
     * Handle a single client connection.
     */
    private static void handleClient(Socket socket, Performer performer) {
        try (Socket client = socket;
             OutputStream out = client.getOutputStream();
             InputStream in = client.getInputStream()) {

            boolean quit = false;
            while (!quit) {
                byte[] reqBytes = NetworkUtils.receive(in);
                if (reqBytes.length == 0) {
                    break;
                }
                JSONObject request = JsonUtils.fromByteArray(reqBytes);
                int choice = request.getInt("selected");
                JSONObject response;

                switch (choice) {
                    case 1:
                        String str = request.getString("data");
                        response = performer.add(str);
                        break;
                    case 3:
                        response = performer.display();
                        break;
                    case 4:
                        response = performer.count();
                        break;
                    case 0:
                        response = new JSONObject().put("type", "quit").put("data", JSONObject.NULL);
                        quit = true;
                        break;
                    default:
                        response = Performer.error("Invalid selection: " + choice + " is not an option");
                }

                NetworkUtils.send(out, JsonUtils.toByteArray(response));
            }
            System.out.println("Closing client: " + client.getRemoteSocketAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
