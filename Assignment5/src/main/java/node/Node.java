package node;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Node {

    public static void main(String[] args) {
        int port = 0;

        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 6000;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Node started. Listening on port " + port);

            while (true) {
                Socket leaderSocket = serverSocket.accept();
                System.out.println("\nLeader connected!");

                ObjectOutputStream oos = new ObjectOutputStream(leaderSocket.getOutputStream());
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(leaderSocket.getInputStream());

                List<Integer> chunk = (List<Integer>) ois.readObject();
                int delayMillis = ois.readInt();
                boolean isVerification = ois.readBoolean();
                int expectedSum = 0;

                if (isVerification) {
                    expectedSum = ois.readInt();
                }

                if (!isVerification) {
                    int partialSum = computeSumWithDelay(chunk, delayMillis);
                    System.out.println("Partial Sum: " + partialSum);
                    oos.writeInt(partialSum);
                    oos.flush();
                } else {
                    int recalculatedSum = computeSumWithDelay(chunk, delayMillis);
                    System.out.println("Expected sum: " + expectedSum + ", Recalculated sum: " + recalculatedSum);
                    boolean agrees = (expectedSum == recalculatedSum);
                    oos.writeBoolean(agrees);
                    oos.flush();
                }

                ois.close();
                oos.close();
                leaderSocket.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int computeSumWithDelay(List<Integer> numbers, int delayMillis) {
        int total = 0;
        for (int num : numbers) {
            total += num;
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                System.err.println("Sleep interrupted: " + e.getMessage());
            }
        }

        if (isFaultMode()) {
            System.out.println("Fault simulation active: Altering the sum!");
            total += 10;
        }

        return total;
    }

    private static boolean isFaultMode() {
        String faultFlag = System.getProperty("Fault");
        return "1".equals(faultFlag);
    }
}
