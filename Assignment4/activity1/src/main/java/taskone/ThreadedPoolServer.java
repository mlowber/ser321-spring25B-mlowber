package taskone;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONObject;

class ThreadPoolServer {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: gradle runTask3 -Pport=<port> -PmaxThreads=<num>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        int maxThreads = Integer.parseInt(args[1]);

        ExecutorService pool = Executors.newFixedThreadPool(maxThreads);
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("ThreadPoolServer started on port " + port + " with maxThreads=" + maxThreads);

        // Shared state for all clients
        StringList sharedList = new StringList();
        Performer performer = new Performer(sharedList);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
            pool.execute(() -> handleClient(clientSocket, performer));
        }
    }

    /**
     * Handle an individual client connection.
     */
    private static void handleClient(Socket client, Performer performer) {
        try (Socket sock = client;
             OutputStream out = sock.getOutputStream();
             InputStream in = sock.getInputStream()) {

            boolean quit = false;
            while (!quit) {
                byte[] reqBytes = NetworkUtils.receive(in);
                if (reqBytes.length == 0) break;
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
                        response = new JSONObject()
                                .put("type", "quit")
                                .put("data", JSONObject.NULL);
                        quit = true;
                        break;
                    default:
                        response = Performer.error("Invalid selection: " + choice + " is not an option");
                }

                NetworkUtils.send(out, JsonUtils.toByteArray(response));
            }
            System.out.println("Closing client: " + sock.getRemoteSocketAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
