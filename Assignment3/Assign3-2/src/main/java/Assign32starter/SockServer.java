package Assign32starter;
import java.net.*;
import java.util.Base64;
import java.util.Set;
import java.util.Stack;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.image.BufferedImage;
import java.io.*;
import org.json.*;

import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * A class to demonstrate a simple client-server connection using sockets.
 * Ser321 Foundations of Distributed Software Systems
 */
public class SockServer {
	static Stack<String> imageSource = new Stack<String>();
	static String currentMovieBase = null;
	static int currentImageLevel = 1;
	static int correctGuesses = 0;
	static int remainingSkips = 0;
	static long gameEndTime = 0;
	static String name = "";
	static Map<String, Double> leaderboard = new HashMap<>();

	// List of movie base names
	static List<String> movies = Arrays.asList(
			"JurassicPark", "LordOfTheRings", "TheDarkKnight", "TheLionKing", "BackToTheFuture"
	);

	// Movie answer mapping
	static Map<String, String> movieAnswers = Map.of(
			"JurassicPark", "Jurassic Park",
			"LordOfTheRings", "The Lord of the Rings",
			"TheDarkKnight", "The Dark Knight",
			"TheLionKing", "The Lion King",
			"BackToTheFuture", "Back to the Future"
	);

	static Set<String> shownMovies = new HashSet<>();

	static Random rand = new Random();


	public static void main(String[] args) {
		Socket sock;
		int port = 8888; // Default port

		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Invalid port argument. Using default port 8888.");
			}
		}

		try {
			ServerSocket serv = new ServerSocket(port);
			System.out.println("Server ready for connection on port " + port);
			loadLeaderboard();


			// read in one object, the message. we know a string was written only by knowing what the client sent.
			// must cast the object from Object to desired type to be useful
			while(true) {
				sock = serv.accept(); // blocking wait

				BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				OutputStream out = sock.getOutputStream();

				String s = in.readLine();  // instead of casting an object
				JSONObject json;
				try {
					json = new JSONObject(s);
					if (json.has("name")) {
						name = json.getString("name");
					}
				} catch (JSONException e) {
					JSONObject error = new JSONObject();
					error.put("type", "error");
					error.put("message", "Malformed JSON request.");
					sendResponse(sock, error);
					continue;
				}

				JSONObject response = new JSONObject();

				if (json.getString("type").equals("start")){

					System.out.println("- Got a start");

					response.put("type","hello" );
					response.put("value","Hello, please tell me your name." );
					sendImg("img/hi.png", response); // calling a method that will manipulate the image and will make it send ready

				} else if (json.getString("type").equals("name")) {
					name = json.getString("value");
					System.out.println("- Got a name: " + name);

					response.put("type", "name");
					response.put("value", "Welcome " + name + "! Please choose an option: play, leaderboard, or quit.");
					sendImg("img/hi.png", response);
				} else if (json.getString("type").equals("choice")) {
					String choice = json.getString("value").toLowerCase();
					response.put("type", "choice");

					switch (choice) {
						case "play":
							response.put("value", "Choose game length: short, medium, or long.");
							break;
						case "leaderboard":
							response.put("value", "Leaderboard:");
							JSONArray scores = new JSONArray();
							leaderboard.entrySet().stream()
									.sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
									.forEach(entry -> {
										JSONObject player = new JSONObject();
										player.put("name", entry.getKey());
										player.put("score", entry.getValue());
										scores.put(player);
									});
							response.put("leaderboard", scores);

							// Reset game state to go back to menu
							currentMovieBase = null;
							currentImageLevel = 1;
							correctGuesses = 0;
							remainingSkips = 0;
							gameEndTime = 0;

							response.put("message", "Returning to main menu. Choose: play, leaderboard, or quit.");
							sendImg("img/hi.png", response);
							break;
						case "quit":
							response.put("value", "Thanks for playing!");
							sendImg("img/bye.png", response);
							break;
						default:
							response.put("type", "error");
							response.put("message", "Invalid choice. Please type: play, leaderboard, or quit.");
							break;
					}
				} else if (json.getString("type").equals("length")) {
					String length = json.getString("value").toLowerCase();
					response.put("type", "length");

					int duration = 0;
					switch (length) {
						case "short":
							duration = 10;
							remainingSkips = 2;
							break;
						case "medium":
							duration = 20;
							remainingSkips = 4;
							break;
						case "long":
							duration = 30;
							remainingSkips = 6;
							break;
						default:
							response.put("type", "error");
							response.put("message", "Invalid game length. Choose: short, medium, or long.");
							sendResponse(sock, response);
							continue;
					}

					gameEndTime = System.currentTimeMillis() + duration * 1000;
					correctGuesses = 0;
					List<String> unseen = new ArrayList<>(movies);
					unseen.removeAll(shownMovies);

					if (unseen.isEmpty()) {
						shownMovies.clear(); // all have been shown; reset for repeatability
						unseen = new ArrayList<>(movies);
					}

					currentMovieBase = unseen.get(rand.nextInt(unseen.size()));
					shownMovies.add(currentMovieBase);

					currentImageLevel = 1;

					response.put("value", "Game started! Here's your first movie image.");
					sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
				} else if (json.getString("type").equals("guess")) {
					response.put("type", "guess");

					if (checkAndHandleTimeout(sock, response)) {
						continue;
					}
					if (currentMovieBase == null || !movieAnswers.containsKey(currentMovieBase)) {
						response.put("value", "No movie loaded. Please start a game first by typing 'play' and selecting a length.");
						sendImg("img/hi.png", response);
						sendResponse(sock, response);
						continue;
					}
					String guess = json.getString("value").toLowerCase();
					String correctTitle = movieAnswers.get(currentMovieBase).toLowerCase();

					if (guess.equals(correctTitle)) {
						correctGuesses++;
						// Estimate time elapsed to compute provisional score
						long timeElapsedSec = (System.currentTimeMillis() - (gameEndTime - (1000 * 30))) / 1000;
						if (timeElapsedSec < 1) timeElapsedSec = 1; // prevent div by zero

						double score = calculateScore(correctGuesses, (int) timeElapsedSec);
						// Update leaderboard if needed
						if (!leaderboard.containsKey(name) || leaderboard.get(name) < score) {
							leaderboard.put(name, score);
							saveLeaderboard();
						}
						response.put("value", "Correct! Here's the next movie.");
						List<String> unseen = new ArrayList<>(movies);
						unseen.removeAll(shownMovies);

						if (unseen.isEmpty()) {
							shownMovies.clear(); // all have been shown; reset for repeatability
							unseen = new ArrayList<>(movies);
						}

						currentMovieBase = unseen.get(rand.nextInt(unseen.size()));
						shownMovies.add(currentMovieBase);
						currentImageLevel = 1;
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					} else {
						response.put("value", "Incorrect guess. Try again or use 'next' or 'skip'.");
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					}
				}
				else if (json.getString("type").equals("next")) {
					response.put("type", "next");

					if (checkAndHandleTimeout(sock, response)) {
						continue;
					}
					if (currentImageLevel < 4) {
						currentImageLevel++;
						response.put("value", "Here is a clearer image.");
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					} else {
						response.put("value", "No clearer image available. Please guess or skip.");
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					}
				} else if (json.getString("type").equals("skip")) {
					response.put("type", "skip");

					if (checkAndHandleTimeout(sock, response)) {
						continue;
					}
					if (remainingSkips > 0) {
						remainingSkips--;
						List<String> unseen = new ArrayList<>(movies);
						unseen.removeAll(shownMovies);

						if (unseen.isEmpty()) {
							shownMovies.clear(); // all have been shown; reset for repeatability
							unseen = new ArrayList<>(movies);
						}

						currentMovieBase = unseen.get(rand.nextInt(unseen.size()));
						shownMovies.add(currentMovieBase);
						currentImageLevel = 1;
						response.put("value", "Skipped! Here is a new movie.");
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					} else {
						response.put("value", "No skips remaining.");
						sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
					}

				} else if (json.getString("type").equals("remaining")) {
					response.put("type", "remaining");
					if (checkAndHandleTimeout(sock, response)) {
						continue;
					}
					response.put("value", "Skips remaining: " + remainingSkips);
					sendImg("img/" + currentMovieBase + currentImageLevel + ".png", response);
				} else {
					System.out.println("not sure what you meant");
					response.put("type","error" );
					response.put("message","unknown response" );
				}
				PrintWriter outWrite = new PrintWriter(sock.getOutputStream(), true); // using a PrintWriter here, you could also use and ObjectOutputStream or anything you fancy
				outWrite.println(response.toString());
			}

		} catch(Exception e) {e.printStackTrace();}
	}

	public static double calculateScore(int correct, int totalSeconds) {
		if (totalSeconds == 0) return 0.0;
		return ((double) correct / totalSeconds) * 100;
	}

	public static void sendResponse(Socket sock, JSONObject obj) {
		try {
			PrintWriter outWrite = new PrintWriter(sock.getOutputStream(), true);
			outWrite.println(obj.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JSONObject sendImg(String filename, JSONObject obj) throws Exception {
		File file = new File(filename);
		if (file.exists()) {
			BufferedImage image = ImageIO.read(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "png", baos);
			String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
			obj.put("image", base64Image);
			obj.put("imageFormat", "png");
		} else {
			obj.put("image", "");
			obj.put("message", "Image file not found: " + filename);
		}
		return obj;
	}

	private static boolean checkAndHandleTimeout(Socket sock, JSONObject response) {
		if (gameEndTime == 0) {
			// Game hasn't started yet
			return false;
		}

		if (System.currentTimeMillis() > gameEndTime) {
			response.put("type", "timeout");
			response.put("value", "Time is up!");

			long duration = (System.currentTimeMillis() - (gameEndTime - (1000 * 30))) / 1000;
			if (duration < 1) duration = 1;

			double score = calculateScore(correctGuesses, (int) duration);
			response.put("score", score);
			response.put("message", "Game over! You scored: " + score);

			if (!leaderboard.containsKey(name) || leaderboard.get(name) < score) {
				leaderboard.put(name, score);
				saveLeaderboard();
			}

			try {
				sendImg("img/hi.png", response);
				sendResponse(sock, response);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Reset game state
			currentMovieBase = null;
			currentImageLevel = 1;
			correctGuesses = 0;
			remainingSkips = 0;
			gameEndTime = 0;

			return true;
		}
		return false;
	}

	public static void saveLeaderboard() {
		try (FileWriter writer = new FileWriter("leaderboard.json")) {
			JSONObject json = new JSONObject(leaderboard);
			writer.write(json.toString(2)); // pretty print with indent
		} catch (IOException e) {
			System.out.println("Failed to save leaderboard: " + e.getMessage());
		}
	}

	public static void loadLeaderboard() {
		File file = new File("leaderboard.json");
		if (!file.exists()) return;

		try {
			String content = new String(Files.readAllBytes(Paths.get("leaderboard.json")));
			JSONObject json = new JSONObject(content);
			for (String key : json.keySet()) {
				leaderboard.put(key, json.getDouble(key));
			}
			System.out.println("Leaderboard loaded from file.");
		} catch (Exception e) {
			System.out.println("Failed to load leaderboard: " + e.getMessage());
		}
	}
}
