package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Verwaltet zur Laufzeit angemeldete Nutzer (wird vom Server verwendet)
 * -> Singleton
 */
public class AuthenticatedUserList {

    private static AuthenticatedUserList instance;
    private HashMap<String,UserModel> authenticatedUserList = new HashMap<>();

    private AuthenticatedUserList() {}

    /**
     * Fügt Nutzer in die Liste ien
     * @param key Token des Clients
     * @param user User
     */
    public void addUser(String key, UserModel user) {
        authenticatedUserList.put(key,user);
    }

    /**
     * Liefert einen Nutzer für ein session Token zurück
     * @param key session Token
     * @return Nutzer (falls vorhanden)
     */
    public UserModel getUser(String key) {
        return authenticatedUserList.get(key);
    }

    /**
     * Liefert alle authentifizierten Nutzer aus der Liste zurück als Arraylist
     */
    public ArrayList<UserModel> getAuthenticatedUsers() {
        ArrayList<UserModel> users = new ArrayList<>();
        authenticatedUserList.forEach((s, userModel) -> users.add(userModel));
        return users;
    }

    /**
     * Instanz des Singleton
     * @return Instanz
     */
    public static AuthenticatedUserList getInstance() {
        if(AuthenticatedUserList.instance == null) {
            AuthenticatedUserList.instance = new AuthenticatedUserList();
        }
        return AuthenticatedUserList.instance;
    }
}
