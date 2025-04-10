import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static org.junit.Assert.*;

public class QuizGameTest {

    private static Socket socket;
    private static ObjectOutputStream oos;
    private static DataInputStream dis;

    @BeforeClass
    public static void setUp() throws Exception {
        socket = new Socket("localhost", 8888);
        oos = new ObjectOutputStream(socket.getOutputStream());
        dis = new DataInputStream(socket.getInputStream());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        oos.close();
        dis.close();
        socket.close();
    }

    private JSONObject sendRequest(JSONObject req) throws Exception {
        oos.writeObject(req.toString()); // send JSON string
        oos.flush();

        String response = dis.readUTF(); // receive JSON string
        return new JSONObject(response);
    }

    @Test
    public void testAddQuestion() throws Exception {
        JSONObject req = new JSONObject();
        req.put("type", "quizgame");
        req.put("addQuestion", true);
        req.put("question", "What is the capital of Germany?");
        req.put("answer", "Berlin");

        JSONObject res = sendRequest(req);

        assertTrue(res.getBoolean("ok"));
        assertEquals("quizgame", res.getString("type"));
    }

    @Test
    public void testPlayGame() throws Exception {
        // Ensure a question exists
        JSONObject addReq = new JSONObject();
        addReq.put("type", "quizgame");
        addReq.put("addQuestion", true);
        addReq.put("question", "What is 9 + 10?");
        addReq.put("answer", "19");
        sendRequest(addReq);

        JSONObject playReq = new JSONObject();
        playReq.put("type", "quizgame");
        playReq.put("addQuestion", false);

        JSONObject res = sendRequest(playReq);

        assertTrue(res.getBoolean("ok"));
        assertEquals("quizgame", res.getString("type"));
        assertTrue(res.has("question"));
    }

    @Test
    public void testCorrectAnswer() throws Exception {
        // Add question
        JSONObject addReq = new JSONObject();
        addReq.put("type", "quizgame");
        addReq.put("addQuestion", true);
        addReq.put("question", "What is the capital of Spain?");
        addReq.put("answer", "Madrid");
        sendRequest(addReq);

        // Trigger play
        JSONObject playReq = new JSONObject();
        playReq.put("type", "quizgame");
        playReq.put("addQuestion", false);
        sendRequest(playReq); // We ignore the returned question, assume it’s the right one

        // Send the correct answer
        JSONObject answerReq = new JSONObject();
        answerReq.put("type", "quizgame");
        answerReq.put("answer", "Madrid");

        JSONObject res = sendRequest(answerReq);

        assertTrue(res.getBoolean("ok"));
        assertEquals("quizgame", res.getString("type"));
        assertTrue(res.getBoolean("result")); // expecting it to be correct
    }

    @Test
    public void testIncorrectAnswer() throws Exception {
        JSONObject addReq = new JSONObject();
        addReq.put("type", "quizgame");
        addReq.put("addQuestion", true);
        addReq.put("question", "What color is the sky?");
        addReq.put("answer", "blue");
        sendRequest(addReq);

        JSONObject playReq = new JSONObject();
        playReq.put("type", "quizgame");
        playReq.put("addQuestion", false);
        sendRequest(playReq);

        JSONObject req = new JSONObject();
        req.put("type", "quizgame");
        req.put("answer", "green");

        JSONObject res = sendRequest(req);

        assertTrue(res.getBoolean("ok"));
        assertEquals("quizgame", res.getString("type"));
        assertFalse(res.getBoolean("result"));
        assertTrue(res.has("question"));
    }
}
