package server;

import buffers.RequestProtos.Request;
import buffers.RequestProtos.Message;
import buffers.RequestProtos.Logs;
import buffers.ResponseProtos.Response;
import buffers.ResponseProtos.Entry;
import buffers.ResponseProtos.Response.EvalType;
import buffers.RequestProtos.Request.OperationType;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

class SockBaseServer {
    static String logFilename = "logs.txt";

    // Please use these as given so it works with our test cases
    static String menuOptions = "\nWhat would you like to do? \n 1 - to see the leader board \n 2 - to enter a game \n 3 - quit the game";
    static String gameOptions = "\nChoose an action: \n (1-9) - Enter an int to specify the row you want to update \n c - Clear number \n r - New Board";

    // track each player’s points and login count
    private static class LeaderEntry implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        int points  = 0;
        int logins  = 0;
    }
    private static final ConcurrentHashMap<String, LeaderEntry> leaderboardMap =
            new ConcurrentHashMap<>();

    ServerSocket serv = null;
    InputStream in = null;
    OutputStream out = null;
    Socket clientSocket = null;
    private final int id; // client id

    Game game; // current game

    private boolean inGame = false; // a game was started (you can decide if you want this
    private String name; // player name

    private int currentState =1; // I used something like this to keep track of where I am in the game, you can decide if you want that as well

    private static boolean grading = true; // if the grading board should be used

    public SockBaseServer(Socket sock, Game game, int id) {
        this.clientSocket = sock;
        this.game = game;
        this.id = id;
        try {
            in = clientSocket.getInputStream();
            out = clientSocket.getOutputStream();
        } catch (Exception e){
            System.out.println("Error in constructor: " + e);
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadLeaderboard() {
        File f = new File("leaderboard.dat");
        if (!f.exists()) return;
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(f))) {
            Map<String, LeaderEntry> disk = (Map<String, LeaderEntry>) in.readObject();
            leaderboardMap.putAll(disk);
            System.out.println("Loaded leaderboard from disk: " + leaderboardMap.keySet());
        } catch (Exception e) {
            System.err.println("Failed to load leaderboard: " + e.getMessage());
        }
    }

    private static void saveLeaderboard() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("leaderboard.dat"))) {
            // serialize a plain HashMap to avoid ConcurrentHashMap quirks
            out.writeObject(new HashMap<>(leaderboardMap));
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
        }
    }

    /**
     * Received a request, starts to evaluate what it is and handles it, not complete
     */
    public void startGame() throws IOException {
        try {
            while (true) {
                // read the proto object and put into new object
                Request op = Request.parseDelimitedFrom(in);
                System.out.println("Got request: " + op.toString());
                Response response;

                boolean quit = false;

                // should handle all the other request types here, my advice is to put them in methods similar to nameRequest()
                switch (op.getOperationType()) {
                    case NAME:
                        if (op.getName().isBlank()) {
                            response = error(1, "name");
                        } else {
                            response = nameRequest(op);
                        }
                        break;
                    case LEADERBOARD:
                        response = leaderboardRequest(op);
                        break;
                    case START:
                        response = startGame(op);
                        break;
                    case UPDATE:
                        response = playRequest(op);
                        break;
                    case CLEAR:
                        response = clearRequest(op);
                        break;
                    case QUIT:
                        response = quit();
                        quit = true;
                        break;
                    default:
                        response = error(2, op.getOperationType().name());
                        break;
                }
                response.writeDelimitedTo(out);

                if (quit) {
                    return;
                }
            }
        } catch (SocketException se) {
            System.out.println("Client disconnected");
        } catch (Exception ex) {
            Response error = error(0, "Unexpected server error: " + ex.getMessage());
            error.writeDelimitedTo(out);
        }
        finally {
            System.out.println("Client ID " + id + " disconnected");
            this.inGame = false;
            exitAndClose(in, out, clientSocket);
        }
    }

    void exitAndClose(InputStream in, OutputStream out, Socket serverSock) throws IOException {
        if (in != null)   in.close();
        if (out != null)  out.close();
        if (serverSock != null) serverSock.close();
    }

    /**
     * Handles the name request and returns the appropriate response
     * @return Request.Builder holding the reponse back to Client as specified in Protocol
     */
    private Response nameRequest(Request op) throws IOException {
        name = op.getName();

        // update leaderboard login count
        leaderboardMap.compute(name, (k, old) -> {
            if (old == null) {
                LeaderEntry e = new LeaderEntry();
                e.logins = 1;
                return e;
            } else {
                old.logins++;
                return old;
            }
        });
        saveLeaderboard();

        writeToLog(name, Message.CONNECT);
        currentState = 2;

        System.out.println("Got a connection and a name: " + name);
        return Response.newBuilder()
                .setResponseType(Response.ResponseType.GREETING)
                .setMessage("Hello " + name + " and welcome to a simple game of Sudoku.")
                .setMenuoptions(menuOptions)
                .setNext(currentState)
                .build();
    }

    /**
     * Handles a LEADERBOARD request.
     */
    private Response leaderboardRequest(Request op) throws IOException {
        Response.Builder resp = Response.newBuilder()
                .setResponseType(Response.ResponseType.LEADERBOARD)
                .setMenuoptions(menuOptions)
                .setNext(2);

        leaderboardMap.forEach((player, data) -> {
            resp.addLeader(
                    Entry.newBuilder()
                            .setName(player)
                            .setPoints(data.points)
                            .setLogins(data.logins)
            );
        });

        return resp.build();
    }

    /**
     * Handles the START request: spins up a new game at the requested difficulty,
     * or returns an error if difficulty is out of range.
     */
    private Response startGame(Request op) throws IOException {
        writeToLog(name, Message.START);
        int difficulty = op.getDifficulty();             // read client’s choice
        System.out.println("Starting new game with difficulty " + difficulty);

        if (difficulty <1 || difficulty > 20) {
            return Response.newBuilder()
                    .setResponseType(Response.ResponseType.ERROR)
                    .setErrorType(5)
                    .setMessage("Error: difficulty is out of range")
                    .setMenuoptions(menuOptions)
                    .setNext(2)
                    .build();
        }

        game.newGame(grading, difficulty);               // use it here
        String boardStr = game.getDisplayBoard();        // initial board

        return Response.newBuilder()
                .setResponseType(Response.ResponseType.START)
                .setBoard(boardStr)
                .setMessage("\n")
                .setMenuoptions(gameOptions)                 // the in-game menu
                .setNext(3)                                  // 3 = client should use in-game menu next
                .build();
    }

    /**
     * Handles a PLAY request (placing a number).
     */
    private Response playRequest(Request op) {
        int row = op.getRow();
        int col = op.getColumn();
        int val = op.getValue();

        // convert from 1-based to 0-based for Game logic
        row -= 1;
        col -= 1;

        if (row < 0 || row > 8 || col < 0 || col > 8) {
            return Response.newBuilder()
                    .setResponseType(Response.ResponseType.ERROR)
                    .setErrorType(3)
                    .setMessage("Error: row or column out of bounds")
                    .setMenuoptions(gameOptions)
                    .setNext(3)
                    .build();
        }

        // type=0 means "UPDATE" in Game.updateBoard
        int rawResult = game.updateBoard(row, col, val, 0);
        EvalType result = Response.EvalType.forNumber(rawResult);
        if (rawResult == 2 || rawResult == 3 || rawResult == 4 || rawResult == 1) {
            game.setPoints(-2);
        }

        String board = game.getDisplayBoard();
        int pts     = game.getPoints();

        Response.Builder b = Response.newBuilder()
                .setResponseType(Response.ResponseType.PLAY)
                .setBoard(board)
                .setType(result)
                .setPoints(pts)
                .setMenuoptions(gameOptions)
                .setNext(3);

        // if the move filled the last X…
        if (game.checkWon()) {
            game.setPoints(+20);
            writeToLog(name, Message.WIN);

            leaderboardMap.compute(name, (k, e) -> {
                e.points += game.getPoints();
                return e;
            });
            saveLeaderboard();

            b.setResponseType(Response.ResponseType.WON)
                    .setMessage("You solved the current puzzle, good job!")
                    .setMenuoptions(menuOptions)
                    .setNext(2)
                    .setType(EvalType.UPDATE)
                    .setPoints(game.getPoints());
        }
        return b.build();
    }

    /**
     * Handles a CLEAR request (clearing cell/row/col/grid/board).
     */
    private Response clearRequest(Request op) {
        int row       = op.getRow();
        int col       = op.getColumn();
        int clearCode = op.getValue();  // 1 = value, 2 = row, 3 = col, 4 = grid, 5 = board, 6 = new board

        // Validate parameters based on clearCode
        boolean invalid = false;

        switch (clearCode) {
            case 1: // clear value
            case 2: // clear row
                if (row < 1 || row > 9) invalid = true;
                break;
            case 3: // clear column
                if (col < 1 || col > 9) invalid = true;
                break;
            case 4: // clear grid
                if (row < 1 || row > 9) invalid = true; // row is used as grid ID
                break;
            case 5: // clear board
            case 6: // new board
                // no validation needed, row and col can be -1
                break;
            default:
                return Response.newBuilder()
                        .setResponseType(Response.ResponseType.ERROR)
                        .setErrorType(0)
                        .setMessage("Error: unknown clear code")
                        .setMenuoptions(gameOptions)
                        .setNext(3)
                        .build();
        }

        if (invalid) {
            return Response.newBuilder()
                    .setResponseType(Response.ResponseType.ERROR)
                    .setErrorType(3)
                    .setMessage("Error: row or column out of bounds for clear operation")
                    .setMenuoptions(gameOptions)
                    .setNext(3)
                    .build();
        }

        if (row > 0) row -= 1;
        if (col > 0) col -= 1;

        int rawResult = game.updateBoard(row, col, 0, clearCode);
        EvalType result = Response.EvalType.forNumber(rawResult);
        game.setPoints(-5);
        leaderboardMap.compute(name, (k, e) -> {
            e.points = game.getPoints();
            return e;
        });
        saveLeaderboard();

        String board = game.getDisplayBoard();
        int pts      = game.getPoints();

        return Response.newBuilder()
                .setResponseType(Response.ResponseType.PLAY)
                .setBoard(board)
                .setType(result)
                .setPoints(pts)
                .setMenuoptions(gameOptions)
                .setNext(3)
                .build();
    }

    /**
     * Handles the quit request, might need adaptation
     * @return Request.Builder holding the reponse back to Client as specified in Protocol
     */
    private  Response quit() throws IOException {
        this.inGame = false;
        return Response.newBuilder()
                .setResponseType(Response.ResponseType.BYE)
                .setMessage("Thank you for playing! goodbye.")
                .build();
    }

    /**
     * Start of handling errors, not fully done
     * @return Request.Builder holding the reponse back to Client as specified in Protocol
     */
    private Response error(int err, String field) throws IOException {
        String message = "";
        int type = err;
        Response.Builder response = Response.newBuilder();

        switch (err) {
            case 1:
                message = "\nError: required field missing or empty";
                break;
            case 2:
                message = "\nError: request not supported";
                break;
            default:
                message = "\nError: cannot process your request";
                type = 0;
                break;
        }

        response
                .setResponseType(Response.ResponseType.ERROR)
                .setErrorType(type)
                .setMessage(message)
                .setNext(currentState)
                .build();

        return response.build();
    }
    
    /**
     * Writing a new entry to our log
     * @param name - Name of the person logging in
     * @param message - type Message from Protobuf which is the message to be written in the log (e.g. Connect) 
     * @return String of the new hidden image
     */
    public void writeToLog(String name, Message message) {
        try {
            // read old log file
            Logs.Builder logs = readLogFile();

            Date date = java.util.Calendar.getInstance().getTime();

            // we are writing a new log entry to our log
            // add a new log entry to the log list of the Protobuf object
            logs.addLog(date + ": " +  name + " - " + message);

            // open log file
            FileOutputStream output = new FileOutputStream(logFilename);
            Logs logsObj = logs.build();

            // write to log file
            logsObj.writeTo(output);
        } catch(Exception e) {
            System.out.println("Issue while trying to save");
        }
    }

    /**
     * Reading the current log file
     * @return Logs.Builder a builder of a logs entry from protobuf
     */
    public Logs.Builder readLogFile() throws Exception {
        Logs.Builder logs = Logs.newBuilder();

        try {
            return logs.mergeFrom(new FileInputStream(logFilename));
        } catch (FileNotFoundException e) {
            System.out.println(logFilename + ": File not found.  Creating a new file.");
            return logs;
        }
    }

    public static void main (String[] args) throws Exception {
        loadLeaderboard();
        if (args.length != 2) {
            System.out.println("Expected arguments: <port(int)> <delay(int)>");
            System.exit(1);
        }
        int port = 8000; // default port
        grading = Boolean.parseBoolean(args[1]);
        Socket clientSocket = null;
        ServerSocket socket = null;

        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port|sleepDelay] must be an integer");
            System.exit(2);
        }
        try {
            socket = new ServerSocket(port);
            System.out.println("Server started..");
        } catch(Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
        int id = 1;
        while (true) {
            try {
                clientSocket = socket.accept();
                System.out.println("Attempting to connect to client-" + id);
                Game game = new Game();
                SockBaseServer server = new SockBaseServer(clientSocket, game, id++);
                server.startGame();
            } catch (Exception e) {
                System.out.println("Error in accepting client connection.");
            }
        }
    }
}
