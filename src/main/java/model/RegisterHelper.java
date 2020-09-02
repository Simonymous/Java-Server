package model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RegisterHelper {

    public RegisterHelper() {
    }

    public String registerUser(String name, String password) {
        JSONParser parser = new JSONParser();
        try {
            File file = new File(
                    UserAuthenticator.class.getClassLoader().getResource("users.json").getFile()
            );

            Object obj = parser.parse(new FileReader(file));
            JSONArray usersJson = (JSONArray) obj;

            JSONObject userObject = new JSONObject();

            userObject.put("name", name);
            userObject.put("password", password);

            usersJson.add(userObject);

            FileWriter fileWriter = new FileWriter(file);

            fileWriter.write(usersJson.toJSONString());
            fileWriter.flush();
            fileWriter.close();

            return String.format("Registration of user %s successfull", name);
        } catch (Exception exception) {
            return exception.getMessage();
        }
    }
}