package example.grpcclient;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;
import com.google.protobuf.Empty;
import service.*;
import service.SortGrpc;
import service.SortRequest;
import service.SortResponse;
import service.Algo;
import service.CoffeePotGrpc;
import service.BrewResponse;
import service.GetCupResponse;
import service.BrewStatusResponse;
import service.BrewStatus;
import service.KVStoreGrpc;
import service.KVPutRequest;
import service.KVPutResponse;
import service.KVGetRequest;
import service.KVGetResponse;
import service.TodoGrpc;
import service.AddTaskRequest;
import service.AddTaskResponse;
import service.ListTasksRequest;
import service.ListTasksResponse;
import service.TaskEntry;
import service.MarkDoneRequest;
import service.MarkDoneResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

/**
 * Client that requests methods from various gRPC services via a menu.
 */
public class Client {
  private final EchoGrpc.EchoBlockingStub echoStub;
  private final JokeGrpc.JokeBlockingStub jokeStub;
  private final RegistryGrpc.RegistryBlockingStub registryStub;
  private final SortGrpc.SortBlockingStub sortStub;
  private final CoffeePotGrpc.CoffeePotBlockingStub coffeeStub;
  private final KVStoreGrpc.KVStoreBlockingStub kvStub;
  private final TodoGrpc.TodoBlockingStub todoStub;

  /**
   * Construct client for accessing server using the existing channels.
   */
  public Client(Channel channel, Channel regChannel) {
    echoStub     = EchoGrpc.newBlockingStub(channel);
    jokeStub     = JokeGrpc.newBlockingStub(channel);
    registryStub = RegistryGrpc.newBlockingStub(regChannel);
    sortStub     = SortGrpc.newBlockingStub(channel);
    coffeeStub   = CoffeePotGrpc.newBlockingStub(channel);
    kvStub       = KVStoreGrpc.newBlockingStub(channel);
    todoStub     = TodoGrpc.newBlockingStub(channel);
  }

  /**
   * Convenience constructor when registry channel equals service channel.
   */
  public Client(Channel channel) {
    this(channel, channel);
  }

  // Echo service
  public void askServerToParrot(String message) {
    ClientRequest req = ClientRequest.newBuilder().setMessage(message).build();
    try {
      ServerResponse res = echoStub.parrot(req);
      System.out.println("-> Echo: " + res.getMessage());
    } catch (Exception e) {
      System.err.println("Echo RPC failed: " + e.getMessage());
    }
  }

  // Joke services
  public void askForJokes(int num) {
    JokeReq req = JokeReq.newBuilder().setNumber(num).build();
    try {
      JokeRes res = jokeStub.getJoke(req);
      for (String j : res.getJokeList()) System.out.println("-> Joke: " + j);
    } catch (Exception e) {
      System.err.println("GetJoke RPC failed: " + e.getMessage());
    }
  }

  public void setJoke(String joke) {
    JokeSetReq req = JokeSetReq.newBuilder().setJoke(joke).build();
    try {
      JokeSetRes res = jokeStub.setJoke(req);
      System.out.println(res.getOk() ? "-> Joke set" : "-> Joke set failed: " + res.getMessage());
    } catch (Exception e) {
      System.err.println("SetJoke RPC failed: " + e.getMessage());
    }
  }

  // Sort service
  public void doSort(List<Integer> data) {
    SortRequest req = SortRequest.newBuilder().setAlgo(Algo.MERGE).addAllData(data).build();
    try {
      SortResponse res = sortStub.sort(req);
      System.out.println(res.getIsSuccess() ? "-> Sorted: " + res.getDataList() : "-> Sort error: " + res.getError());
    } catch (Exception e) {
      System.err.println("Sort RPC failed: " + e.getMessage());
    }
  }

  // CoffeePot service
  public void brew() {
    try {
      BrewResponse res = coffeeStub.brew(Empty.newBuilder().build());
      System.out.println(res.getIsSuccess() ? "-> CoffeePot brew: " + res.getMessage() : "-> CoffeePot brew error: " + res.getError());
    } catch (Exception ex) {
      System.err.println("Brew RPC failed: " + ex.getMessage());
    }
  }

  public void brewStatus() {
    try {
      BrewStatusResponse res = coffeeStub.brewStatus(Empty.newBuilder().build());
      BrewStatus s = res.getStatus();
      System.out.println("-> CoffeePot status: " + s.getMessage());
    } catch (Exception ex) {
      System.err.println("BrewStatus RPC failed: " + ex.getMessage());
    }
  }

  public void getCup() {
    try {
      GetCupResponse res = coffeeStub.getCup(Empty.newBuilder().build());
      System.out.println(res.getIsSuccess() ? "-> CoffeePot getCup: " + res.getMessage() : "-> CoffeePot getCup error: " + res.getError());
    } catch (Exception ex) {
      System.err.println("GetCup RPC failed: " + ex.getMessage());
    }
  }

  // KVStore service
  public void putKV(String key, String value) {
    KVPutRequest req = KVPutRequest.newBuilder().setKey(key).setValue(value).build();
    try {
      KVPutResponse res = kvStub.put(req);
      System.out.println(res.getSuccess() ? "-> KVStore put OK" : "-> KVStore put error: " + res.getError());
    } catch (Exception e) {
      System.err.println("KVStore put RPC failed: " + e.getMessage());
    }
  }

  public void getKV(String key) {
    KVGetRequest req = KVGetRequest.newBuilder().setKey(key).build();
    try {
      KVGetResponse res = kvStub.get(req);
      System.out.println(res.getFound() ? "-> KVStore value: " + res.getValue() : "-> KVStore get error: " + res.getError());
    } catch (Exception e) {
      System.err.println("KVStore get RPC failed: " + e.getMessage());
    }
  }

  // To-Do service
  public void addTask(String text) {
    AddTaskRequest req = AddTaskRequest.newBuilder().setText(text).build();
    try {
      AddTaskResponse res = todoStub.addTask(req);
      System.out.println(res.getSuccess() ? "-> Added task id=" + res.getId() : "-> AddTask error: " + res.getError());
    } catch (Exception e) {
      System.err.println("AddTask RPC failed: " + e.getMessage());
    }
  }

  public void listTasks() {
    ListTasksResponse res = todoStub.listTasks(ListTasksRequest.newBuilder().build());
    for (TaskEntry t : res.getTasksList()) {
      System.out.printf("-> [%d] %s %s%n", t.getId(), t.getText(), t.getDone() ? "(done)" : "");
    }
  }

  public void markDone(int id) {
    MarkDoneResponse res = todoStub.markDone(MarkDoneRequest.newBuilder().setId(id).build());
    System.out.println(res.getSuccess() ? "-> Marked task " + id + " done" : "-> MarkDone error: " + res.getError());
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 6 || args.length > 7) {
      System.out.println("Usage: <host> <port> <regHost> <regPort> <message> <regOn> [auto]");
      System.exit(1);
    }

    String host    = args[0];
    int    port    = Integer.parseInt(args[1]);
    String regHost = args[2];
    int    regPort = Integer.parseInt(args[3]);
    String msg     = args[4];
    boolean regOn  = args[5].equalsIgnoreCase("true");
    boolean auto   = args.length == 7 && (args[6].equals("1") || args[6].equalsIgnoreCase("true"));

    ManagedChannel channel    = ManagedChannelBuilder.forTarget(host + ":" + port).usePlaintext().build();
    ManagedChannel regChannel = ManagedChannelBuilder.forTarget(regHost + ":" + regPort).usePlaintext().build();
    Client client = new Client(channel, regChannel);

    if (auto) {
      System.out.println("** AUTO-RUN mode **");
      // Echo
      client.askServerToParrot("auto_test_message");
      // Joke
      client.askForJokes(2);
      client.setJoke("auto_test_joke");
      client.askForJokes(1);
      // Sort
      client.doSort(List.of(7,9,4,6,1,3,8,2,5));
      // CoffeePot
      client.brew();
      client.brewStatus();
      Thread.sleep(31000);
      client.getCup();
      // KVStore
      client.putKV("foo", "bar");
      client.getKV("foo");
      client.getKV("missing");
      // To-Do
      client.addTask("auto_task1");
      client.listTasks();
      client.markDone(1);
      client.listTasks();

      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      if (regOn) regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
      return;
    }

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      System.out.println("\n--- Available Services ---");
      System.out.println("1) Echo");
      System.out.println("2) Joke - getJoke");
      System.out.println("3) Joke - setJoke");
      System.out.println("4) Sort");
      System.out.println("5) CoffeePot");
      System.out.println("6) KVStore");
      System.out.println("7) To-Do List");
      System.out.println("0) Exit");
      System.out.print("Enter choice: ");

      int choice;
      try { choice = Integer.parseInt(reader.readLine()); } catch (Exception e) { continue; }

      switch (choice) {
        case 0:
          System.out.println("Goodbye!");
          channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
          if (regOn) regChannel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
          return;
        case 1:
          System.out.print("Enter message: ");
          client.askServerToParrot(reader.readLine());
          break;
        case 2:
          System.out.print("How many jokes: ");
          client.askForJokes(Integer.parseInt(reader.readLine()));
          break;
        case 3:
          System.out.print("Enter new joke: ");
          client.setJoke(reader.readLine());
          break;
        case 4:
          System.out.print("Enter numbers comma-separated: ");
          List<Integer> nums = new ArrayList<>();
          for (String p : reader.readLine().split(",")) {
            try { nums.add(Integer.parseInt(p.trim())); } catch (Exception ignored) {}
          }
          client.doSort(nums);
          break;
        case 5:
          sub: {
            System.out.println("\n-- CoffeePot --");
            System.out.println("1) Brew");
            System.out.println("2) BrewStatus");
            System.out.println("3) GetCup");
            System.out.println("4) Run All");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int c;
            try { c = Integer.parseInt(reader.readLine()); } catch (Exception ex) { break sub; }
            switch (c) {
              case 1: client.brew(); break;
              case 2: client.brewStatus(); break;
              case 3: client.getCup(); break;
              case 4:
                client.brew(); client.brewStatus(); Thread.sleep(31000); client.getCup();
                break;
              default: break;
            }
          }
          break;
        case 6:
          sub: {
            System.out.println("\n-- KVStore --");
            System.out.println("1) Put");
            System.out.println("2) Get");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int k;
            try { k = Integer.parseInt(reader.readLine()); } catch (Exception ignored) { break sub; }
            switch (k) {
              case 1:
                System.out.print("Key: "); String key = reader.readLine();
                System.out.print("Value: "); String val = reader.readLine();
                client.putKV(key, val);
                break;
              case 2:
                System.out.print("Key: "); client.getKV(reader.readLine());
                break;
              default: break;
            }
          }
          break;
        case 7:
          sub: {
            System.out.println("\n-- To-Do List --");
            System.out.println("1) Add Task");
            System.out.println("2) List Tasks");
            System.out.println("3) Mark Done");
            System.out.println("0) Back");
            System.out.print("Choice: ");
            int t;
            try { t = Integer.parseInt(reader.readLine()); } catch (Exception ignored) { break sub; }
            switch (t) {
              case 1:
                System.out.print("Task text: "); client.addTask(reader.readLine()); break;
              case 2:
                client.listTasks(); break;
              case 3:
                System.out.print("Task id: "); client.markDone(Integer.parseInt(reader.readLine())); break;
              default: break;
            }
          }
          break;
        default:
          System.out.println("Unknown option");
      }
    }
  }
}