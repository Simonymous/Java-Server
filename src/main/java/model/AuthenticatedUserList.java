package model;

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

    public static AuthenticatedUserList getInstance() {
        if(AuthenticatedUserList.instance == null) {
            AuthenticatedUserList.instance = new AuthenticatedUserList();
        }
        return AuthenticatedUserList.instance;
    }
}
