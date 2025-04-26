package leader;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class MiniLeader {
    public static void main(String[] args) {
        List<Integer> chunk = Arrays.asList(1, 2, 3);
        int delayMillis = 100;

        try (Socket nodeSocket = new Socket("localhost", 6000)) {
            System.out.println("Connected to Node!");

            ObjectOutputStream oos = new ObjectOutputStream(nodeSocket.getOutputStream());
            oos.flush();
            ObjectInputStream ois = new ObjectInputStream(nodeSocket.getInputStream());

            oos.writeObject(chunk);
            oos.writeInt(delayMillis);
            oos.flush();

            int result = ois.readInt();
            System.out.println("Node returned sum: " + result);

            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}