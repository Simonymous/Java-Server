package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class UserAuthenticator {
    static ArrayList<UserModel> userList = new ArrayList<>();

    /**
     * TODO: Ineffizientes authentifizieren: Besser einmalig per init json auslesen?
     */
    public UserAuthenticator() {
        parseJsonData();
    }

    /**
     * Prüft ob ein Nutzer (mit name passwort) in der json steht
     * @param name
     * @param password
     * @return
     */
    public UserModel authenticateUser(String name, String password) {
        AtomicReference<UserModel> user = new AtomicReference<>();
        userList.forEach(u -> {
            if (u.name.equals(name)&&u.password.equals(password)) {
                user.set(u);
            }
        });
        return user.get();
    }

    /**
     * Parst ein einzelnes Nutzerobjekt und fügt es in die Liste ein
     * @param jsonUser
     */
    private static void parseUserObject(JSONObject jsonUser) {
        JSONObject userObject = (JSONObject) jsonUser.get("user");
        String name = (String) userObject.get("name");
        String password = (String) userObject.get("password");


        UserModel user = new UserModel(name, password);
        userList.add(user);
    }

    /**
     * Liefert alle Nutzer aus der json als ArrayList
     * @return Nutzerliste
     */
    public ArrayList<UserModel> getUsers() {
        return userList;
    }

    /**
     * Lese die Json aus und parse die User in eine interne Liste
     */
    private static void parseJsonData() {
        userList.clear();
        JSONParser parser = new JSONParser();
        try {
            File file = new File(
                    UserAuthenticator.class.getClassLoader().getResource("users.json").getFile()
            );
            if (file.length() > 0) {
                Object obj = parser.parse(new FileReader(file));

                JSONArray usersJson = (JSONArray) obj;

                usersJson.forEach( u -> parseUserObject((JSONObject) u));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Zum Testen ob die json ausgelesen wird
     * @param args
     */
    public static void main(String[] args) {
        parseJsonData();
        System.out.println("Reading Users: "+userList);
    }


}
