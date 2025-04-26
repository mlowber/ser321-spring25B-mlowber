package client;

import java.net.Socket;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter numbers separated by spaces:");
            String line = scanner.nextLine();
            List<Integer> numbers = Arrays.stream(line.split("\\s+"))
                    .map(Integer::parseInt)
                    .toList();

            System.out.println("Enter delay in milliseconds:");
            int delayMillis = scanner.nextInt();

            oos.writeObject(numbers);
            oos.writeInt(delayMillis);
            oos.flush();

            System.out.println("Data sent to Leader.");

            oos.close();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
