import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.google.gson.Gson;
import model.TransportObject;
import model.TransportObjectType;
import model.UserModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

public class LoginTest {

    static private final Server server = new Server(9000);

    @BeforeAll
    static void startServer() {
        new Thread(server).start();
    }

    @Test
    void testLoginSucces() throws IOException {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.AUTHENTICATE, new UserModel("admin","admin"));
        assertNotEquals("",schreibeNachricht(to));
    }

    @Test
    void testLoginFailure() {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.AUTHENTICATE, new UserModel("admin","password"));
        assertNotEquals("",schreibeNachricht(to));
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    static String schreibeNachricht(Object obj){
        String answer = null;
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket("127.0.0.1", 9000); // verbindet sich mit Server
            OutputStream out = socket.getOutputStream();
            PrintStream ps = new PrintStream(out, true);

            Gson gson = new Gson();
            String jsonString = gson.toJson(obj);

            ps.println(jsonString);

            InputStream input = socket.getInputStream();
            InputStreamReader in = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(in);
            answer = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return answer;
    }
}
