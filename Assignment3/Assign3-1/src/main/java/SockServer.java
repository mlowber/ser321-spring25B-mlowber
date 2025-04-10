import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.*;
import java.io.*;

/**
 * A class to demonstrate a simple client-server connection using sockets.
 *
 */
public class SockServer {
  static Socket sock;
  static DataOutputStream os;
  static ObjectInputStream in;
  static java.util.List<String[]> quizList = new java.util.ArrayList<>();
  static String currentQuestion = null;
  static String currentAnswer = null;

  static int port = 8888;

  public static void main (String args[]) {

    if (args.length != 1) {
      System.out.println("Expected arguments: <port(int)>");
      System.exit(1);
    }

    try {
      port = Integer.parseInt(args[0]);
    } catch (NumberFormatException nfe) {
      System.out.println("[Port|sleepDelay] must be an integer");
      System.exit(2);
    }

    try {
      //open socket
      ServerSocket serv = new ServerSocket(port);
      System.out.println("Server ready for connections");

      /**
       * Simple loop accepting one client and calling handling one request.
       *
       */


      while (true){
        System.out.println("Server waiting for a connection");
        sock = serv.accept(); // blocking wait
        System.out.println("Client connected");

        // setup the object reading channel
        in = new ObjectInputStream(sock.getInputStream());

        // get output channel
        OutputStream out = sock.getOutputStream();

        // create an object output writer (Java only)
        os = new DataOutputStream(out);

        boolean connected = true;
        while (connected) {
          String s = "";
          try {
            s = (String) in.readObject(); // attempt to read string in from client
          } catch (Exception e) { // catch rough disconnect
            System.out.println("Client disconnect");
            connected = false;
            continue;
          }

          JSONObject res = isValid(s);

          if (res.has("ok")) {
            writeOut(res);
            continue;
          }

          JSONObject req = new JSONObject(s);

          res = testField(req, "type");
          if (!res.getBoolean("ok")) { // no "type" header provided
            res = noType(req);
            writeOut(res);
            continue;
          }
          // check which request it is (could also be a switch statement)
          if (req.getString("type").equals("echo")) {
            res = echo(req);
          } else if (req.getString("type").equals("add")) {
            res = add(req);
          } else if (req.getString("type").equals("addmany")) {
            res = addmany(req);
          } else if (req.getString("type").equals("quizgame")) {
            res = handleQuizGame(req);
          } else if (req.getString("type").equals("stringconcatenation")) {
            res = stringConcatenation(req);
          } else {
            res = wrongType(req);
          }
          writeOut(res);
        }
        // if we are here - client has disconnected so close connection to socket
        overandout();
      }
    } catch(Exception e) {
      e.printStackTrace();
      overandout(); // close connection to socket upon error
    }
  }

  static JSONObject handleQuizGame(JSONObject req) {
    JSONObject res = new JSONObject();
    res.put("type", "quizgame");

    try {
      // Add Question
      if (req.has("addQuestion") && req.getBoolean("addQuestion")) {
        if (!req.has("question") || !req.has("answer")) {
          res.put("ok", false);
          res.put("message", "Missing 'question' or 'answer' field for addQuestion=true.");
          return res;
        }
        String q = req.getString("question");
        String a = req.getString("answer");
        quizList.add(new String[]{q, a});
        res.put("ok", true);
        return res;
      }

      // Play: Send random question
      if (req.has("addQuestion") && !req.getBoolean("addQuestion")) {
        if (quizList.isEmpty()) {
          res.put("ok", false);
          res.put("message", "No quiz questions available.");
        } else {
          int rand = (int)(Math.random() * quizList.size());
          String[] qa = quizList.get(rand);
          currentQuestion = qa[0];
          currentAnswer = qa[1];
          res.put("ok", true);
          res.put("question", currentQuestion);
        }
        return res;
      }

      // Answering mode
      if (req.has("answer")) {
        if (currentAnswer == null) {
          res.put("ok", false);
          res.put("message", "No active question. Use addQuestion=false to get one.");
        } else {
          String guess = req.getString("answer");
          boolean correct = guess.trim().equalsIgnoreCase(currentAnswer.trim());
          res.put("ok", true);
          res.put("result", correct);
          if (!correct) {
            res.put("question", currentQuestion); // repeat question if incorrect
          }
          currentQuestion = null;
          currentAnswer = null;
        }
        return res;
      }

      // Unknown request format
      res.put("ok", false);
      res.put("message", "Unknown quizgame request format.");
      return res;

    } catch (Exception e) {
      res.put("ok", false);
      res.put("message", "Error processing quizgame request: " + e.getMessage());
      return res;
    }
  }

  static JSONObject stringConcatenation(JSONObject req) {
    System.out.println("String concatenation request: " + req.toString());

    JSONObject res1 = testField(req, "string1");
    if (!res1.getBoolean("ok")) return res1;

    JSONObject res2 = testField(req, "string2");
    if (!res2.getBoolean("ok")) return res2;

    JSONObject res = new JSONObject();
    res.put("ok", true);
    res.put("type", "stringconcatenation");

    try {
      String s1 = req.getString("string1");
      String s2 = req.getString("string2");
      res.put("result", s1 + s2);
    } catch (Exception e) {
      res.put("ok", false);
      res.put("message", "string1 and string2 must be strings");
    }

    return res;
  }

  /**
   * Checks if a specific field exists
   *
   */
  static JSONObject testField(JSONObject req, String key){
    JSONObject res = new JSONObject();

    // field does not exist
    if (!req.has(key)){
      res.put("ok", false);
      res.put("message", "Field " + key + " does not exist in request");
      return res;
    }
    return res.put("ok", true);
  }

  // handles the simple echo request
  static JSONObject echo(JSONObject req){
    System.out.println("Echo request: " + req.toString());
    JSONObject res = testField(req, "data");
    if (res.getBoolean("ok")) {
      if (!req.get("data").getClass().getName().equals("java.lang.String")){
        res.put("ok", false);
        res.put("message", "Field data needs to be of type: String");
        return res;
      }

      res.put("type", "echo");
      res.put("echo", "Here is your echo: " + req.getString("data"));
    }
    return res;
  }

  // handles the simple add request with two numbers
  static JSONObject add(JSONObject req){
    System.out.println("Add request: " + req.toString());
    JSONObject res1 = testField(req, "num1");
    if (!res1.getBoolean("ok")) {
      return res1;
    }

    JSONObject res2 = testField(req, "num2");
    if (!res2.getBoolean("ok")) {
      return res2;
    }

    JSONObject res = new JSONObject();
    res.put("ok", true);
    res.put("type", "add");
    try {
      res.put("result", req.getInt("num1") + req.getInt("num2"));
    } catch (org.json.JSONException e){
      res.put("ok", false);
      res.put("message", "Field num1/num2 needs to be of type: int");
    }
    return res;
  }

  // implement me in assignment 3
  static JSONObject inventory(JSONObject req) {
    return new JSONObject();
  }

  // implement me in assignment 3
  static JSONObject charCount(JSONObject req) {
    return new JSONObject();
  }

  // handles the simple addmany request
  static JSONObject addmany(JSONObject req){
    System.out.println("Add many request: " + req.toString());
    JSONObject res = testField(req, "nums");
    if (!res.getBoolean("ok")) {
      return res;
    }

    int result = 0;
    JSONArray array = req.getJSONArray("nums");
    for (int i = 0; i < array.length(); i ++){
      try{
        result += array.getInt(i);
      } catch (org.json.JSONException e){
        res.put("ok", false);
        res.put("message", "Values in array need to be ints");
        return res;
      }
    }

    res.put("ok", true);
    res.put("type", "addmany");
    res.put("result", result);
    return res;
  }

  // creates the error message for wrong type
  static JSONObject wrongType(JSONObject req){
    System.out.println("Wrong type request: " + req.toString());
    JSONObject res = new JSONObject();
    res.put("ok", false);
    res.put("message", "Type " + req.getString("type") + " is not supported.");
    return res;
  }

  // creates the error message for no given type
  static JSONObject noType(JSONObject req){
    System.out.println("No type request: " + req.toString());
    JSONObject res = new JSONObject();
    res.put("ok", false);
    res.put("message", "No request type was given.");
    return res;
  }

  // From: https://www.baeldung.com/java-validate-json-string
  public static JSONObject isValid(String json) {
    try {
      new JSONObject(json);
    } catch (JSONException e) {
      try {
        new JSONArray(json);
      } catch (JSONException ne) {
        JSONObject res = new JSONObject();
        res.put("ok", false);
        res.put("message", "req not JSON");
        return res;
      }
    }
    return new JSONObject();
  }

  // sends the response and closes the connection between client and server.
  static void overandout() {
    try {
      os.close();
      in.close();
      sock.close();
    } catch(Exception e) {e.printStackTrace();}

  }

  // sends the response and closes the connection between client and server.
  static void writeOut(JSONObject res) {
    try {
      os.writeUTF(res.toString());
      // make sure it wrote and doesn't get cached in a buffer
      os.flush();

    } catch(Exception e) {e.printStackTrace();}

  }
}