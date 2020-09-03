import model.AuthenticatedUserList;
import model.UserAuthenticator;
import model.UserModel;

import java.util.ArrayList;
import java.util.Scanner;

public class ServerRunner {
    static Server server;
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean mainLoop = true;

        int choice;

        while(true){
            System.out.println("<<Server Main Menu>>\n");
            System.out.println("Bitte waehlen:");
            System.out.print("1.) Server starten\n");
            System.out.print("2.) Server stoppen.\n");
            System.out.print("3.) Registrierte Nutzer auflisten\n");
            System.out.print("4.) Authentifizierte Nutzer auflisten\n");
            System.out.print("0.) Programm beenden.\n");

            choice = input.nextInt();

            switch (choice) {
                case 0: stopServer();
                        System.exit(0);
                        break;
                case 1: startServer();
                        break;
                case 2: stopServer();
                        break;
                case 3: listRegisteredUsers();
                    break;
                case 4: listAuthenticatedUsers();;
                    break;
                default: System.out.println("<"+choice+"> steht nicht zur Option!" );
                         break;
            }

        }

    }

    private static void startServer() {
        if (server != null) {
            System.out.println("Server läuft bereits!!\n");
        } else {
            server = new Server(9000);
            System.out.println("Server "+server.toString()+" wird jetzt gestartet...");
            new Thread(server).start();
            System.out.println("Waiting for connections...");
        }
    }

    private static void stopServer() {
        if(server != null && !server.isStopped) {
            System.out.println("Server "+server.toString()+" wird jetzt gestoppt...");
            server.stop();
        } else {
            System.out.println("Es läuft kein Server!!\n");
        }

    }

    private static void listRegisteredUsers() {
        UserAuthenticator uA = new UserAuthenticator();
        ArrayList<UserModel> userList = uA.getUsers();
        System.out.println("Registrierte Nutzer: ");
        userList.forEach(u -> {
            System.out.println(u.getName());
        });
        System.out.println("");

    }

    private static void listAuthenticatedUsers() {
        if (server != null) {
            AuthenticatedUserList authenticatedUserList = AuthenticatedUserList.getInstance();
            ArrayList<UserModel> userList = authenticatedUserList.getAuthenticatedUsers();
            System.out.println("Authentifizierte Nutzer: ");
            userList.forEach(u -> {
                System.out.println(u.getName());
            });
            if(userList.isEmpty()) {
                System.out.println("Keine Nutzer angemeldet!");
            }
            System.out.println("");
        } else {
            System.out.println("Hierfür muss ein Server gestartet sein!");
        }

    }

}
