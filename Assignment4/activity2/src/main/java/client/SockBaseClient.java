package client;

import buffers.RequestProtos.*;
import buffers.ResponseProtos.*;

import java.io.*;
import java.net.Socket;

class SockBaseClient {
    public static void main (String[] args) throws Exception {
        Socket serverSock = null;
        OutputStream out = null;
        InputStream in = null;
        int i1=0, i2=0;
        int port = 8000; // default port

        // Make sure two arguments are given
        if (args.length != 2) {
            System.out.println("Expected arguments: <host(String)> <port(int)>");
            System.exit(1);
        }
        String host = args[0];
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.out.println("[Port] must be integer");
            System.exit(2);
        }

        // Build the first request object just including the name
        Request op = nameRequest().build();
        Response response;
        try {
            // connect to the server
            serverSock = new Socket(host, port);

            // write to the server
            out = serverSock.getOutputStream();
            in = serverSock.getInputStream();

            op.writeDelimitedTo(out);

            while (true) {
                // read from the server
                response = Response.parseDelimitedFrom(in);
                System.out.println("Got a response: " + response.toString());

                Request.Builder req = Request.newBuilder();

                switch (response.getResponseType()) {
                    case GREETING:
                        System.out.println(response.getMessage());
                        req = chooseMenu(req, response);
                        break;
                    case LEADERBOARD:
                        System.out.println(response.toString());
                        req = chooseMenu(req, response);
                        break;
                    case START:
                        System.out.println(response.getBoard());
                        System.out.println(response.getMenuoptions());
                        Response endResp = handleInGame(in, out);
                        req = chooseMenu(Request.newBuilder(), endResp);
                        break;
                    case BYE:
                        System.out.println(response.getMessage());
                        exitAndClose(in, out, serverSock);
                        return;
                    case ERROR:
                        System.out.println("Error: " + response.getMessage() + "Type: " + response.getErrorType());
                        if (response.getNext() == 1) {
                            req = nameRequest();
                        } else {
                            req = chooseMenu(req, response);
                        }
                        break;
                    default:
                        System.out.println("Unhandled response type: " + response.getResponseType());
                        req = chooseMenu(req, response);
                        break;
                }
                req.build().writeDelimitedTo(out);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            exitAndClose(in, out, serverSock);
        }
    }

    /**
     * Runs the play/clear loop until the game ends (WON or BYE),
     * then returns that final Response so the main menu can be shown correctly.
     */
    static Response handleInGame(InputStream in, OutputStream out) throws IOException {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter move (row col val), c to clear, r for new board, or exit: ");
            String line = stdin.readLine().trim();
            Request.Builder reqB = Request.newBuilder();

            if (line.equalsIgnoreCase("exit")) {
                reqB.setOperationType(Request.OperationType.QUIT);

            } else if (line.equalsIgnoreCase("r")) {
                // RESET_BOARD
                reqB.setOperationType(Request.OperationType.CLEAR)
                        .setRow(-1)        // sentinel
                        .setColumn(-1)
                        .setValue(6);      // CLEAR code 6 == RESET_BOARD

            } else if (line.equalsIgnoreCase("c")) {
                // CLEAR_VALUE of a single cell
                System.out.print("Row to clear (1-9): ");
                int r = Integer.parseInt(stdin.readLine());
                System.out.print("Column to clear (1-9): ");
                int c = Integer.parseInt(stdin.readLine());

                reqB.setOperationType(Request.OperationType.CLEAR)
                        .setRow(r)         // leave 1–9 here; we’ll subtract in server
                        .setColumn(c)
                        .setValue(1);      // CLEAR_VALUE

            } else {
                // PLAY/UPDATE
                String[] parts = line.split("\\s+");
                int r = Integer.parseInt(parts[0]);
                int c = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                reqB.setOperationType(Request.OperationType.UPDATE)
                        .setRow(r)
                        .setColumn(c)
                        .setValue(v);
            }

            // send…
            reqB.build().writeDelimitedTo(out);

            // …and receive
            Response resp = Response.parseDelimitedFrom(in);
            switch (resp.getResponseType()) {
                case PLAY:
                    System.out.println(resp.getBoard());
                    System.out.println("Points: " + resp.getPoints());
                    System.out.println(resp.getMenuoptions());
                    break;
                case WON:
                    System.out.println(resp.getBoard());
                    System.out.println(resp.getMessage());
                    return resp;
                case BYE:
                    return resp;
                case ERROR:
                    System.out.println("Error: " + resp.getMessage());
                    System.out.println(resp.getMenuoptions());
                    break;
                default:
                    System.out.println("Unexpected response: " + resp.getResponseType());
                    break;
            }
        }
    }

    /**
     * handles building a simple name requests, asks the user for their name and builds the request
     * @return Request.Builder which holds all teh information for the NAME request
     */
    static Request.Builder nameRequest() throws IOException {
        System.out.println("Please provide your name for the server.");
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String strToSend = stdin.readLine();

        return Request.newBuilder()
                .setOperationType(Request.OperationType.NAME)
                .setName(strToSend);
    }

    /**
     * Shows the main menu and lets the user choose a number, it builds the request for the next server call
     * @return Request.Builder which holds the information the server needs for a specific request
     */
    static Request.Builder chooseMenu(Request.Builder req, Response response) throws IOException {
        while (true) {
            System.out.println(response.getMenuoptions());
            System.out.print("Enter a number 1-3: ");
            BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
            String menu_select = stdin.readLine();
            System.out.println(menu_select);
            switch (menu_select) {
                case "1":
                    // View leaderboard
                    req.setOperationType(Request.OperationType.LEADERBOARD);
                    return req;
                case "2":
                    // Start a new game
                    // Prompt for difficulty
                    System.out.print("Enter difficulty (1-20, 1 easy -> 20 hardest): ");
                    int diff;
                    while (true) {
                        try {
                            diff = Integer.parseInt(stdin.readLine());
                            if (diff < 1 || diff > 20) throw new NumberFormatException();
                            break;
                        } catch (NumberFormatException nfe) {
                            System.out.print("Invalid. Please enter an integer 1–20: ");
                        }
                    }
                    req.setOperationType(Request.OperationType.START)
                            .setDifficulty(diff);
                    return req;
                case "3":
                    // Quit
                    req.setOperationType(Request.OperationType.QUIT);
                    return req;
                default:
                    System.out.println("\nNot a valid choice, please choose again");
                    break;
            }
        }
    }

    /**
     * Exits the connection
     */
    static void exitAndClose(InputStream in, OutputStream out, Socket serverSock) throws IOException {
        if (in != null)   in.close();
        if (out != null)  out.close();
        if (serverSock != null) serverSock.close();
        System.exit(0);
    }

    /**
     * Handles the clear menu logic when the user chooses that in Game menu. It retuns the values exactly
     * as needed in the CLEAR request row int[0], column int[1], value int[3]
     */
    static int[] boardSelectionClear() throws Exception {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose what kind of clear by entering an integer (1 - 5)");
        System.out.print(" 1 - Clear value \n 2 - Clear row \n 3 - Clear column \n 4 - Clear Grid \n 5 - Clear Board \n");

        String selection = stdin.readLine();

        while (true) {
            if (selection.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                int temp = Integer.parseInt(selection);

                if (temp < 1 || temp > 5) {
                    throw new NumberFormatException();
                }

                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.println("Choose what kind of clear by entering an integer (1 - 5)");
                System.out.print("1 - Clear value \n 2 - Clear row \n 3 - Clear column \n 4 - Clear Grid \n 5 - Clear Board \n");
            }
            selection = stdin.readLine();
        }

        int[] coordinates = new int[3];

        switch (selection) {
            case "1":
                // clear value, so array will have {row, col, 1}
                coordinates = boardSelectionClearValue();
                break;
            case "2":
                // clear row, so array will have {row, -1, 2}
                coordinates = boardSelectionClearRow();
                break;
            case "3":
                // clear col, so array will have {-1, col, 3}
                coordinates = boardSelectionClearCol();
                break;
            case "4":
                // clear grid, so array will have {gridNum, -1, 4}
                coordinates = boardSelectionClearGrid();
                break;
            case "5":
                // clear entire board, so array will have {-1, -1, 5}
                coordinates[0] = -1;
                coordinates[1] = -1;
                coordinates[2] = 5;
                break;
            default:
                break;
        }

        return coordinates;
    }

    static int[] boardSelectionClearValue() throws Exception {
        int[] coordinates = new int[3];

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose coordinates of the value you want to clear");
        System.out.print("Enter the row as an integer (1 - 9): ");
        String row = stdin.readLine();

        while (true) {
            if (row.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                Integer.parseInt(row);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.print("Enter the row as an integer (1 - 9): ");
            }
            row = stdin.readLine();
        }

        coordinates[0] = Integer.parseInt(row);

        System.out.print("Enter the column as an integer (1 - 9): ");
        String col = stdin.readLine();

        while (true) {
            if (col.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                Integer.parseInt(col);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.print("Enter the column as an integer (1 - 9): ");
            }
            col = stdin.readLine();
        }

        coordinates[1] = Integer.parseInt(col);
        coordinates[2] = 1;

        return coordinates;
    }

    static int[] boardSelectionClearRow() throws Exception {
        int[] coordinates = new int[3];

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose the row you want to clear");
        System.out.print("Enter the row as an integer (1 - 9): ");
        String row = stdin.readLine();

        while (true) {
            if (row.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                Integer.parseInt(row);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.print("Enter the row as an integer (1 - 9): ");
            }
            row = stdin.readLine();
        }

        coordinates[0] = Integer.parseInt(row);
        coordinates[1] = -1;
        coordinates[2] = 2;

        return coordinates;
    }

    static int[] boardSelectionClearCol() throws Exception {
        int[] coordinates = new int[3];

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose the column you want to clear");
        System.out.print("Enter the column as an integer (1 - 9): ");
        String col = stdin.readLine();

        while (true) {
            if (col.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                Integer.parseInt(col);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.print("Enter the column as an integer (1 - 9): ");
            }
            col = stdin.readLine();
        }

        coordinates[0] = -1;
        coordinates[1] = Integer.parseInt(col);
        coordinates[2] = 3;
        return coordinates;
    }

    static int[] boardSelectionClearGrid() throws Exception {
        int[] coordinates = new int[3];

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Choose area of the grid you want to clear");
        System.out.println(" 1 2 3 \n 4 5 6 \n 7 8 9 \n");
        System.out.print("Enter the grid as an integer (1 - 9): ");
        String grid = stdin.readLine();

        while (true) {
            if (grid.equalsIgnoreCase("exit")) {
                return new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
            }
            try {
                Integer.parseInt(grid);
                break;
            } catch (NumberFormatException nfe) {
                System.out.println("That's not an integer!");
                System.out.print("Enter the grid as an integer (1 - 9): ");
            }
            grid = stdin.readLine();
        }

        coordinates[0] = Integer.parseInt(grid);
        coordinates[1] = -1;
        coordinates[2] = 4;

        return coordinates;
    }
}
