package Assign32starter;

import java.awt.Dimension;

import org.json.*;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import java.util.Base64;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;


/**
 * The ClientGui class is a GUI frontend that displays an image grid, an input text box,
 * a button, and a text area for status.
 *
 * Methods of Interest
 * ----------------------
 * show(boolean modal) - Shows the GUI frame with current state
 *     -> modal means that it opens GUI and suspends background processes.
 * 		  Processing still happens in the GUI. If it is desired to continue processing in the
 *        background, set modal to false.
 * newGame(int dimension) - Start a new game with a grid of dimension x dimension size
 * insertImage(String filename, int row, int col) - Inserts an image into the grid
 * appendOutput(String message) - Appends text to the output panel
 * submitClicked() - Button handler for the submit button in the output panel
 *
 * Notes
 * -----------
 * > Does not show when created. show() must be called to show he GUI.
 *
 */
public class ClientGui implements Assign32starter.OutputPanel.EventHandlers {
	JDialog frame;
	PicturePanel picPanel;
	OutputPanel outputPanel;
	String currentMess;
	private int pointsThisRound = 0;
	private String playerName = "";

	Socket sock;
	PrintWriter out;
	BufferedReader bufferedReader;

	String host = "localhost";
	int port = 8888;

	/**
	 * Construct dialog
	 * @throws IOException
	 */
	public ClientGui(String host, int port) throws IOException {
		this.host = host;
		this.port = port;

		frame = new JDialog();
		frame.setLayout(new GridBagLayout());
		frame.setMinimumSize(new Dimension(500, 500));
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		// setup the top picture frame
		picPanel = new PicturePanel();
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.25;
		frame.add(picPanel, c);

		// setup the input, button, and output area
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.75;
		c.weightx = 1;
		c.fill = GridBagConstraints.BOTH;
		outputPanel = new OutputPanel();
		outputPanel.addEventHandlers(this);
		frame.add(outputPanel, c);

		picPanel.newGame(1);

		open(); // open connection to server

		JSONObject startMessage = new JSONObject();
		startMessage.put("type", "start");
		sendRequest(startMessage);

		JSONObject resp = readResponse();
		if (resp != null) {
			outputPanel.appendOutput(resp.getString("value"));
			if (resp.has("image")) {
				insertBase64Image(resp.getString("image"));
			}
		}

		close(); // close connection

		// Now Client interaction only happens when the submit button is used, see "submitClicked()" method
	}

	/**
	 * Shows the current state in the GUI
	 * @param makeModal - true to make a modal window, false disables modal behavior
	 */
	public void show(boolean makeModal) {
		frame.pack();
		frame.setModal(makeModal);
		frame.setVisible(true);
	}

	/**
	 * Creates a new game and set the size of the grid
	 * @param dimension - the size of the grid will be dimension x dimension
	 * No changes should be needed here
	 */
	public void newGame(int dimension) {
		picPanel.newGame(1);
		outputPanel.appendOutput("Started new game with a " + dimension + "x" + dimension + " board.");
	}

	/**
	 * Insert an image into the grid at position (col, row)
	 *
	 * @param filename - filename relative to the root directory
	 * @param row - the row to insert into
	 * @param col - the column to insert into
	 * @return true if successful, false if an invalid coordinate was provided
	 * @throws IOException An error occured with your image file
	 */
	public boolean insertImage(String filename, int row, int col) throws IOException {
		return insertImage(filename, row, col, true);
	}

	// Overloaded method with silent flag
	public boolean insertImage(String filename, int row, int col, boolean showMessage) throws IOException {
		String error = "";
		try {
			if (picPanel.insertImage(filename, row, col)) {
				if (showMessage) {
					outputPanel.appendOutput("Inserting " + filename + " in position (" + row + ", " + col + ")");
				}
				return true;
			}
			error = "File(\"" + filename + "\") not found.";
		} catch(PicturePanel.InvalidCoordinateException e) {
			error = e.toString();
		}
		if (showMessage) {
			outputPanel.appendOutput(error);
		}
		return false;
	}

	public void insertBase64Image(String base64Data) {
		try {
			byte[] imageBytes = Base64.getDecoder().decode(base64Data);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			File tempFile = new File("temp_image.png");
			ImageIO.write(img, "png", tempFile);
			insertImage(tempFile.getPath(), 0, 0, false);
		} catch (Exception e) {
			outputPanel.appendOutput("Failed to decode and show image.");
			e.printStackTrace();
		}
	}

	/**
	 * Submit button handling
	 *
	 * Right now this method opens and closes the connection after every interaction, if you want to keep that or not is up to you.
	 */
	@Override
	public void submitClicked() {
		try {
			open(); // open connection

			String inputRaw = outputPanel.getInputText().trim();
			String input = inputRaw.toLowerCase();
			System.out.println("submit clicked: " + input);

			// Capture player name early if it's the first input after "Hello"
			if (playerName.isEmpty()) {
				playerName = inputRaw;
			}

			JSONObject req = new JSONObject();

			// Determine command type based on input
			switch (input) {
				case "play":
				case "leaderboard":
				case "quit":
					req.put("type", "choice");
					req.put("value", input);
					break;
				case "short":
				case "medium":
				case "long":
					req.put("type", "length");
					req.put("value", input);
					pointsThisRound = 0; // reset points
					outputPanel.setPoints(pointsThisRound);
					break;
				case "skip":
				case "next":
				case "remaining":
					req.put("type", input);
					break;
				default:
					// Treat any other input as a guess
					req.put("type", "guess");
					req.put("value", input);
					break;
			}

			// Store name if it's the 'name' phase
			if (req.getString("type").equals("name")) {
				playerName = inputRaw;
			}

			if (!playerName.isEmpty()) {
				req.put("name", playerName);
			}

			// Send to server
			sendRequest(req);

			// Get server response
			JSONObject resp = readResponse();
			if (resp != null) {
				if (resp.has("value")) {
					outputPanel.appendOutput(resp.getString("value"));
				}
				if (resp.has("message")) {
					outputPanel.appendOutput(resp.getString("message"));
				}
				if (resp.has("score")) {
					double score = resp.getDouble("score");
					String formattedScore = String.format("%.0f", score);
					outputPanel.appendOutput("Score: " + formattedScore);
				}
				if (resp.has("leaderboard")) {
					JSONArray scores = resp.getJSONArray("leaderboard");
					outputPanel.appendOutput("--- Leaderboard ---");
					for (int i = 0; i < scores.length(); i++) {
						JSONObject entry = scores.getJSONObject(i);
						String entryText = String.format("%s: %.0f", entry.getString("name"), entry.getDouble("score"));
						outputPanel.appendOutput(entryText);
					}
				}
				if (resp.has("type") && resp.getString("type").equals("guess") && resp.has("value")
						&& resp.getString("value").startsWith("Correct")) {
					pointsThisRound++;
					outputPanel.setPoints(pointsThisRound);
				}
				if (resp.has("image")) {
					insertBase64Image(resp.getString("image"));
				}
			}

			close(); // close connection
			outputPanel.setInputText(""); // Clears the text field after submission
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Key listener for the input text box
	 *
	 * Change the behavior to whatever you need
	 */
	@Override
	public void inputUpdated(String input) {
		if (input.equals("surprise")) {
			outputPanel.appendOutput("You found me!");
		}
	}

	public void open() throws UnknownHostException, IOException {
		this.sock = new Socket(host, port); // connect to host and socket
		this.out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
		this.bufferedReader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
	}

	public void close() {
		try {
			if (out != null)  out.close();
			if (bufferedReader != null)   bufferedReader.close();
			if (sock != null) sock.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendRequest(JSONObject obj) {
		if (out != null) {
			out.println(obj.toString());
		}
	}

	public JSONObject readResponse() {
		try {
			String line = bufferedReader.readLine();
			if (line != null) {
				return new JSONObject(line);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws IOException {
		try {
			String host = "localhost";
			int port = 8888; // Default to match server

			if (args.length > 0) {
				host = args[0];
			}
			if (args.length > 1) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid port argument. Using default 8888.");
				}
			}

			System.out.println("Connecting to " + host + " on port " + port);
			ClientGui main = new ClientGui(host, port);
			main.show(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
