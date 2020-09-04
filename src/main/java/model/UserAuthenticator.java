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

    public UserAuthenticator() {
        parseJsonData();
    }

    public UserModel authenticateUser(String name, String password) {
        AtomicReference<UserModel> user = new AtomicReference<>();
        userList.forEach(u -> {
            if (u.name.equals(name)&&u.password.equals(password)) {
                user.set(u);
            }
        });
        return user.get();
    }

    private static void parseUserObject(JSONObject jsonUser) {
        String name = (String) jsonUser.get("name");
        String password = (String) jsonUser.get("password");

        UserModel user = new UserModel(name, password);
        userList.add(user);
    }

    public ArrayList<UserModel> getUsers() {
        return userList;
    }

    private static void parseJsonData() {
        JSONParser parser = new JSONParser();
        try {
            File file = new File(
                    UserAuthenticator.class.getClassLoader().getResource("users.json").getFile()
            );
            Object obj = parser.parse(new FileReader(file));

            JSONArray usersJson = (JSONArray) obj;

            usersJson.forEach( u -> parseUserObject((JSONObject) u));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        parseJsonData();
        System.out.println("Reading Users: "+userList);
    }


}
