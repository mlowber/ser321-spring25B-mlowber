import org.json.JSONArray;
import org.json.JSONObject;
import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 */
class SockClient {
  static Socket sock = null;
  static String host = "localhost";
  static int port = 8888;
  static OutputStream out;
  // Using and Object Stream here and a Data Stream as return. Could both be the same type I just wanted
  // to show the difference. Do not change these types.
  static ObjectOutputStream os;
  static DataInputStream in;
  public static void main (String args[]) {

    if (args.length != 2) {
      System.out.println("Expected arguments: <host(String)> <port(int)>");
      System.exit(1);
    }

    try {
      host = args[0];
      port = Integer.parseInt(args[1]);
    } catch (NumberFormatException nfe) {
      System.out.println("[Port|sleepDelay] must be an integer");
      System.exit(2);
    }

    try {
      connect(host, port); // connecting to server
      System.out.println("Client connected to server.");
      boolean requesting = true;
      while (requesting) {
        System.out.println("What would you like to do: 1 - echo, 2 - add, 3 - addmany, 4 - string concatenation, 5 - quizz (0 to quit)");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(scanner.nextLine());
        // You can assume the user put in a correct input, you do not need to handle errors here
        // You can assume the user inputs a String when asked and an int when asked. So you do not have to handle user input checking
        JSONObject json = new JSONObject(); // request object
        switch(choice) {
          case 0:
            System.out.println("Choose quit. Thank you for using our services. Goodbye!");
            requesting = false;
            break;
          case 1:
            System.out.println("Choose echo, which String do you want to send?");
            String message = scanner.nextLine();
            json.put("type", "echo");
            json.put("data", message);
            break;
          case 2:
            System.out.println("Choose add, enter first number:");
            String num1 = scanner.nextLine();
            json.put("type", "add");
            json.put("num1", num1);

            System.out.println("Enter second number:");
            String num2 = scanner.nextLine();
            json.put("num2", num2);
            break;
          case 3:
            System.out.println("Choose addmany, enter as many numbers as you like, when done choose 0:");
            JSONArray array = new JSONArray();
            String num = "1";
            while (!num.equals("0")) {
              num = scanner.nextLine();
              array.put(num);
              System.out.println("Got your " + num);
            }
            json.put("type", "addmany");
            json.put("nums", array);
            break;
          case 4:
            System.out.println("Enter first string:");
            String s1 = scanner.nextLine();
            System.out.println("Enter second string:");
            String s2 = scanner.nextLine();

            json.put("type", "stringconcatenation");
            json.put("string1", s1);
            json.put("string2", s2);
            break;

          case 5:
            System.out.print("Enter quiz command (add/get/answer): ");
            String quizCmd = scanner.nextLine().trim();

            json.put("type", "quizgame");

            if (quizCmd.equalsIgnoreCase("add")) {
              json.put("addQuestion", true);
              System.out.print("Enter question: ");
              String q = scanner.nextLine();
              json.put("question", q);
              System.out.print("Enter answer: ");
              String a = scanner.nextLine();
              json.put("answer", a);
            } else if (quizCmd.equalsIgnoreCase("get")) {
              json.put("addQuestion", false);
            } else if (quizCmd.equalsIgnoreCase("answer")) {
              System.out.print("Enter your answer: ");
              String a = scanner.nextLine();
              json.put("answer", a);
            } else {
              System.out.println("Invalid quiz command.");
              continue; // skip sending invalid request
            }
            break;
        }
        if(!requesting) {
          continue;
        }

        // write the whole message
        os.writeObject(json.toString());
        // make sure it wrote and doesn't get cached in a buffer
        os.flush();

        // handle the response
        // - not doing anything other than printing payload
        // !! you will most likely need to parse the response for the other 2 services!
        String i = (String) in.readUTF();
        JSONObject res = new JSONObject(i);
        System.out.println("Got response: " + res);
        if (res.getBoolean("ok")) {
          if (res.has("echo")) {
            System.out.println("Echoed: " + res.getString("echo"));
          }
          if (res.has("result")) {
            System.out.println("Result: " + res.get("result"));
          }
          if (res.has("question")) {
            System.out.println("Question: " + res.getString("question"));
          }
          if (res.has("type")) {
            System.out.println("Type: " + res.getString("type"));
          }
        } else {
          System.out.println("Error: " + res.getString("message"));
        }
      }
      // want to keep requesting services so don't close connection
      //overandout();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void overandout() throws IOException {
    //closing things, could
    in.close();
    os.close();
    sock.close(); // close socked after sending
  }

  public static void connect(String host, int port) throws IOException {
    // open the connection
    sock = new Socket(host, port); // connect to host and socket on port 8888

    // get output channel
    out = sock.getOutputStream();

    // create an object output writer (Java only)
    os = new ObjectOutputStream(out);

    in = new DataInputStream(sock.getInputStream());
  }
}