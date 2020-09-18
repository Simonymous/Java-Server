
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.TransportObjectType;
import model.TransportObject;
import model.UserModel;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Scanner;

public class Client {
    static final private String ip = "127.0.0.1"; // localhost
    static final private int port = 9000;
    static private String key = "";

    /**
     * Main loop for Client IO
     * @param args
     */
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            boolean mainLoop = true;

            int choice;

            while(true){
                System.out.println("\n<<Client Main Menu>>\n");
                System.out.println("Bitte waehlen:");
                System.out.print("1.) Test Anfrage\n");
                System.out.print("2.) Authentifizierung\n");
                System.out.print("3.) Registrierung\n");
                System.out.print("4.) Primzahl überprüfen?\n");
                System.out.print("0.) Programm beenden.\n");

                choice = input.nextInt();

                /**
                 * Parse User Input Option
                 */
                switch (choice) {
                    case 0: System.exit(0);
                        break;
                    case 1: test();
                        break;
                    case 2: authenticate();
                        break;
                    case 3: register();
                        break;
                    case 4: isPrime();
                        break;
                    default: System.out.println("Fehler: <"+choice+"> steht nicht zur Option!" );
                        break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse Register Input and prepare it for sending
     * @throws IOException
     */
    static void register() throws IOException {

        System.out.println("<Registrierung>");
        System.out.print("Name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.next();
        System.out.print("Passwort: ");
        String pw = scanner.next();
        System.out.println("Nutzer '" + name + "' wird registriert...");

        UserModel usr = new UserModel(name, pw);

        TransportObject<UserModel> outputObject = new TransportObject<>(TransportObjectType.REGISTERREQUEST, usr);

        schreibeNachricht(outputObject);


    }

    /**
     * Parse Authenticate Input and prepare it for sending
     * @throws IOException
     */
    static void authenticate()  throws  IOException {
        if (!key.equals("")) {
            System.out.println("Fehler: Sie sind bereits authentifiziert!");
        } else {
            System.out.println("<Authentifizierung>");
            System.out.print("Name: ");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.next();
            System.out.print("Passwort: ");
            String pw = scanner.next();
            System.out.println("Nutzer '" + name + "' wird angemeldet...");
            authenticate(name, pw);
        }
    }
    /**
     * Parse Prime Request Input and prepare it for sending
     * @throws IOException
     */
    static void isPrime() throws IOException {
        if (!key.equals("")) {
            System.out.println("<Primzahl-Check>");
            System.out.print("Zahl: ");
            Scanner scanner = new Scanner(System.in);
            int number;
            while(!scanner.hasNextInt()) {
                System.out.println("Das ist keine valide Zahl! Neue Zahl:");
                scanner.next();
            }
            number = scanner.nextInt();
            TransportObject<Integer> outputObject = new TransportObject<Integer>(TransportObjectType.ISPRIMEREQUEST, number,key);

            schreibeNachricht(outputObject);
        } else {
            System.out.println("Sie müssen sich zuerst authentifizieren :");
            authenticate();
        }
    }

    /**
     * Send a test Request
     * @throws IOException
     */
    static void test()  throws  IOException {
        if (!key.equals("")) {
            TransportObject<String> outputObject = new TransportObject<>(TransportObjectType.TEST, "Hello Server!",key);

            schreibeNachricht(outputObject);
        } else {
            System.out.println("Sie müssen sich zuerst authentifizieren :");
            authenticate();
        }


    }

    /**
     * Build User Object for Authentication and send it
     * @param name
     * @param password
     * @throws IOException
     */
    static void authenticate(String name, String password) throws IOException{

        UserModel usr = new UserModel(name,password);

        TransportObject<UserModel> outputObject = new TransportObject<>(TransportObjectType.AUTHENTICATE, usr);
        String json = schreibeNachricht(outputObject);
    }


    /**
     * Sends any Object to server
     * @param obj
     * @return
     */
    static String schreibeNachricht(Object obj){
        String answer = null;
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket(ip,port); // verbindet sich mit Server
            System.out.println("[LOG] Nachricht wird geparsed: "+obj.toString());
            OutputStream out = socket.getOutputStream();
            PrintStream ps = new PrintStream(out, true);

            Gson gson = new Gson();
            String jsonString = gson.toJson(obj);

            System.out.println("[LOG] Sende json: "+jsonString);
            ps.println(jsonString);

            InputStream input = socket.getInputStream();
            InputStreamReader in = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(in);
            answer = br.readLine();
            System.out.println("[LOG] Received: "+answer);
            parseTransport(answer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("[LOG] Socket geschlossen...");

            } catch (IOException e) {
                System.out.println("[LOG] Socket nicht zu schliessen...\n");
                e.printStackTrace();
            }

        }


        return answer;
    }

    /**
     * Parses Returned Answers To Objec
     * @param content
     */
    static private void parseTransport(String content) {
        Gson gson = new Gson();
        TransportObject o = gson.fromJson(content, TransportObject.class);
        switch (o.type) {
            case AUTHENTICATERESPONSE:
                parseCode(content);
                break;
        }
    }

    /**
     * Parses An Authenticate-Response
     * @param content
     */
    static private void parseCode(String content) {
        Gson gson = new Gson();
        Type type = new TypeToken<TransportObject<String>>(){}.getType();
        TransportObject<String> received = gson.fromJson(content,type);
        if(received.object.equals("")) {
            System.out.println("[LOG] Login-Fehler: Falsche Daten!");
        } else {
            key = received.object;
            System.out.println("[LOG] Authentifizierung erfolgreich! Neuer Key: "+key);
        }

    }

}
