package model;

import java.util.ArrayList;
import java.util.HashMap;

public class AuthenticatedUserList {

    private static AuthenticatedUserList instance;
    private HashMap<String,UserModel> authenticatedUserList = new HashMap<>();

    private AuthenticatedUserList() {}

    public void addUser(String key, UserModel user) {
        authenticatedUserList.put(key,user);
    }

    public UserModel getUser(String key) {
        return authenticatedUserList.get(key);
    }

    public ArrayList<UserModel> getAuthenticatedUsers() {
        ArrayList<UserModel> users = new ArrayList<>();
        authenticatedUserList.forEach((s, userModel) -> users.add(userModel));
        return users;
    }

    public static AuthenticatedUserList getInstance() {
        if(AuthenticatedUserList.instance == null) {
            AuthenticatedUserList.instance = new AuthenticatedUserList();
        }
        return AuthenticatedUserList.instance;
    }
}
