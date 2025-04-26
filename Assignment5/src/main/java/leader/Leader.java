package leader;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Leader {

    public static void main(String[] args) {
        int port = 5000;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Leader is waiting for a client on port " + port + "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected!");

            ObjectInputStream clientOis = new ObjectInputStream(clientSocket.getInputStream());

            List<Integer> numbers = (List<Integer>) clientOis.readObject();
            int delayMillis = clientOis.readInt();

            System.out.println("Received list: " + numbers);
            System.out.println("Received delay: " + delayMillis + "ms");

            List<List<Integer>> chunks = splitList(numbers, 3);
            for (int i = 0; i < chunks.size(); i++) {
                System.out.println("Chunk for Node " + (i + 1) + ": " + chunks.get(i));
            }

            Map<Integer, Integer> portIndexMap = Map.of(
                    6000, 0,
                    6001, 1,
                    6002, 2
            );
            int[] nodePorts = {6000, 6001, 6002};

            List<NodeHandler> handlers = new ArrayList<>();
            List<Thread> threads = new ArrayList<>();

            for (int i = 0; i < nodePorts.length; i++) {
                NodeHandler handler = new NodeHandler("localhost", nodePorts[i], chunks.get(i), delayMillis, nodePorts[i], portIndexMap);
                Thread thread = new Thread(handler);
                handlers.add(handler);
                threads.add(thread);
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            List<Integer> partialSumList = new ArrayList<>();
            for (int sum : partialSums) {
                partialSumList.add(sum);
            }

            System.out.println("\nPartial sums collected from Nodes: " + partialSumList);
            int distributedSum = partialSumList.stream().mapToInt(Integer::intValue).sum();

            System.out.println("\nStarting Consensus Check...");
            List<Boolean> agreements = new ArrayList<>(List.of(false, false, false));
            List<Thread> verifyThreads = new ArrayList<>();

            for (int i = 0; i < nodePorts.length; i++) {
                final int targetPort = nodePorts[i];
                final int myIndex = portIndexMap.get(targetPort);
                final int verifyIndex = (myIndex + 1) % nodePorts.length;

                Thread verifyThread = new Thread(() -> {
                    try (Socket nodeSocket = new Socket("localhost", targetPort)) {
                        ObjectOutputStream oos = new ObjectOutputStream(nodeSocket.getOutputStream());
                        oos.flush();
                        ObjectInputStream ois = new ObjectInputStream(nodeSocket.getInputStream());

                        List<Integer> chunkToVerify = chunks.get(verifyIndex);
                        int expectedSum = partialSums[verifyIndex];

                        oos.writeObject(chunkToVerify);
                        oos.writeInt(delayMillis);
                        oos.writeBoolean(true);
                        oos.writeInt(expectedSum);
                        oos.flush();

                        boolean agrees = ois.readBoolean();
                        agreements.set(myIndex, agrees);

                        oos.close();
                        ois.close();
                    } catch (Exception e) {
                        System.err.println("Consensus check failed for Node on port " + targetPort);
                        agreements.set(myIndex, false);
                    }
                });

                verifyThreads.add(verifyThread);
                verifyThread.start();
            }

            for (Thread thread : verifyThreads) {
                thread.join();
            }

            boolean allAgree = agreements.stream().allMatch(Boolean::booleanValue);

            if (allAgree) {
                System.out.println("\nConsensus Reached: Distributed sum is valid!");
                System.out.println("Final Distributed Total Sum: " + distributedSum);

                System.out.println("\nStarting local sum computation...");
                long startTime = System.currentTimeMillis();
                int localSum = computeSumWithDelay(numbers, delayMillis);
                long endTime = System.currentTimeMillis();

                System.out.println("Local Sum: " + localSum);
                System.out.println("Time taken: " + (endTime - startTime) + " ms");

            } else {
                System.out.println("\nConsensus Failed: There was disagreement among nodes.");
                System.out.println("Error: Distributed result may be invalid!");
            }

            clientOis.close();
            clientSocket.close();

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
        return total;
    }

    public static List<List<Integer>> splitList(List<Integer> numbers, int parts) {
        List<List<Integer>> chunks = new ArrayList<>();
        int chunkSize = (int) Math.ceil((double) numbers.size() / parts);

        for (int i = 0; i < numbers.size(); i += chunkSize) {
            int end = Math.min(numbers.size(), i + chunkSize);
            chunks.add(new ArrayList<>(numbers.subList(i, end)));
        }

        return chunks;
    }

    static int[] partialSums = new int[3];

    static class NodeHandler implements Runnable {
        private String host;
        private int port;
        private List<Integer> chunk;
        private int delayMillis;
        private int partialSum = 0;
        private int portKey;
        private Map<Integer, Integer> portIndexMap;
        private int[] sharedArray;

        public NodeHandler(String host, int port, List<Integer> chunk, int delayMillis, int portKey, Map<Integer, Integer> portIndexMap) {
            this.host = host;
            this.port = port;
            this.chunk = chunk;
            this.delayMillis = delayMillis;
            this.portKey = portKey;
            this.portIndexMap = portIndexMap;
        }

        public int getPartialSum() {
            return partialSum;
        }

        public int getPort() {
            return portKey;
        }

        @Override
        public void run() {
            try (Socket nodeSocket = new Socket(host, port)) {
                System.out.println("Connected to Node at " + host + ":" + port);

                ObjectOutputStream oos = new ObjectOutputStream(nodeSocket.getOutputStream());
                oos.flush();
                ObjectInputStream ois = new ObjectInputStream(nodeSocket.getInputStream());

                oos.writeObject(chunk);
                oos.flush();
                oos.writeInt(delayMillis);
                oos.flush();
                oos.writeBoolean(false);
                oos.flush();

                partialSum = ois.readInt();
                System.out.println("Received partial sum from Node at port " + port + ": " + partialSum);

                int correctIndex = portIndexMap.get(port);
                synchronized (Leader.class) {
                    Leader.partialSums[correctIndex] = partialSum;
                }

                oos.close();
                ois.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
