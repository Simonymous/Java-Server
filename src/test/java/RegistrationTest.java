import com.google.gson.Gson;
import model.TransportObject;
import model.TransportObjectType;
import model.UserModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class RegistrationTest {

    static private final Server server = new Server(9000);

    @BeforeAll
    static void startServer() {
        new Thread(server).start();
    }

    @Test
    void registrationSuccess() {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.REGISTERREQUEST, new UserModel("admin2","admin"));
        String answer = (String)  schreibeNachricht(to).object;
        assertEquals("REGISTERD!",answer);

        //TODO: Call Login Test to verify correct registartion
    }

    @Test
    void registrationDouble() {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.REGISTERREQUEST, new UserModel("admin","admin"));
        String answer = (String)  schreibeNachricht(to).object;
        assertEquals("USER ALREADY EXISTS!",answer);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    static TransportObject schreibeNachricht(Object obj){
        String answer = null;
        java.net.Socket socket = null;
        TransportObject returnObject = null;
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
            returnObject = gson.fromJson(answer, TransportObject.class);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return returnObject;
    }
}
