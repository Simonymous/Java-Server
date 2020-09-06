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

public class PrimeTest {

    static private final Server server = new Server(9000);

    @Test
    void primeRequest() {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.AUTHENTICATE, new UserModel("admin","admin"));
        String token = (String) schreibeNachricht(to).object;
        TransportObject<Integer> to2 = new TransportObject<>(TransportObjectType.ISPRIMEREQUEST, 11, token);
        String answer = (String) schreibeNachricht(to2).object;
        assertEquals("11 is prime!",answer);
    }

    @Test
    void noPrimeRequest() {
        TransportObject<UserModel> to = new TransportObject<>(TransportObjectType.AUTHENTICATE, new UserModel("admin","admin"));
        String token = (String) schreibeNachricht(to).object;
        TransportObject<Integer> to2 = new TransportObject<>(TransportObjectType.ISPRIMEREQUEST, 9, token);
        String answer = (String) schreibeNachricht(to2).object;
        assertEquals("9 is no prime!",answer);
    }

    @BeforeAll
    static void startServer() {
        new Thread(server).start();
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
            //System.out.println("!!!!2"+answer);
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

        //System.out.println("!!!!!"+returnObject);
        return returnObject;
    }
}
