

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;


public class RunnableWorker implements Runnable{
    protected Socket clientSocket = null;
    protected String workerName   = null;

    public RunnableWorker(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.workerName   = serverText;
    }

    public void run() {
        Date startTime = new Date(System.currentTimeMillis());
        System.out.println("[LOG] ["+startTime+"] "+this.workerName+": Thread wird bearbeitet...");
        try {

            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();

            InputStreamReader in = new InputStreamReader(input);

            BufferedReader br = new BufferedReader(in);
            String content = br.readLine();
            TransportObject response = parseTransport(content);

            //System.out.println("[LOG] "+this.workerName+" Received: "+to.toString());


            PrintStream ps = new PrintStream(output, true);
            Gson gson = new Gson();
            String jsonString = gson.toJson(response);

            System.out.println("[LOG] Sende json: "+jsonString);
            ps.println(jsonString);

            output.close();
            input.close();
            //clientSocket.close();
            Date endTime = new Date(System.currentTimeMillis());
            long totaltime = endTime.getTime()-startTime.getTime();
            System.out.println("[LOG] ["+endTime+"] "+this.workerName+": Request erfolgreich bearbeitet...  Dauer: "+totaltime+"ms");
        } catch (IOException e) {
            //Fehler!!
            e.printStackTrace();
        }
    }

    private TransportObject parseTransport(String content) {
        System.out.println("[LOG] Parsing: "+content);
        Gson gson = new Gson();
        TransportObject o = gson.fromJson(content, TransportObject.class);
        TransportObject returnObj = null;
        switch (o.type) {
            case TEST: returnObj = test(content);
                       break;
            case AUTHENTICATE: returnObj =  authenticate(content);
                               break;
            case REGISTERREQUEST: returnObj = register(content);
                                  break;
            case ISPRIMEREQUEST: returnObj = isPrime(content);
                                 break;
        }
        return returnObj;
    }

    private TransportObject<String> isPrime(String content) {
        TransportObject<String> returnObj = new TransportObject<String>(TransportObjectType.GENERALRESPONSE, "");
        Gson gson = new Gson();
        Type type = new TypeToken<TransportObject<Integer>>(){}.getType();
        TransportObject<Integer> to = gson.fromJson(content,type);
        String token = to.token;
        System.out.println("[LOG] Test Request received with token : "+token);

        AuthenticatedUserList authenticatedUserList = AuthenticatedUserList.getInstance();
        if (authenticatedUserList.getUser(token)!=null) {
            System.out.println("[LOG] Token ok!");
            PrimeHelper ph = new PrimeHelper();
            if (to.object != null && ph.isPrime(to.object)) {
                returnObj.object = to.object+" is prime!";
            } else {
                returnObj.object = to.object+" is no prime!";
            }

        } else {
            System.out.println("[LOG] Token invalid!");
            returnObj.object = "Invalid Key!";
        }
        return returnObj;
    }

    private TransportObject<String> register(String content) {
        TransportObject<String> returnObj = new TransportObject<String>(TransportObjectType.GENERALRESPONSE, "");
        Gson gson = new Gson();
        Type type = new TypeToken<TransportObject<UserModel>>(){}.getType();
        TransportObject<UserModel> toUser = gson.fromJson(content,type);
        UserModel user = (UserModel) toUser.object;
        System.out.println("[LOG] User received: "+user);

        UserAuthenticator uA = new UserAuthenticator();
        ArrayList<UserModel> userList = uA.getUsers();

        AtomicBoolean userExists = new AtomicBoolean(false);

        userList.forEach(u -> {
            if (u.getName().equals(user.getName())) {
                userExists.set(true);
            }
        });

        if (userExists.get()) {
            System.out.println("[LOG] User '"+user.getName()+ "' is already registerd!");
            returnObj.object = "USER ALREADY EXISTS!";

        } else {
            System.out.println("[LOG] Registering '"+user.getName()+ "'...");
            RegisterHelper registerHelper = new RegisterHelper();
            registerHelper.registerUser(user.getName(),user.getPassword());


            returnObj.object = "REGISTERD!";
        }
        return returnObj;
    }

    private TransportObject<String> test(String content) {
        TransportObject<String> returnObj = new TransportObject<String>(TransportObjectType.GENERALRESPONSE, "");
        Gson gson = new Gson();
        Type type = new TypeToken<TransportObject<String>>(){}.getType();
        TransportObject<String> to = gson.fromJson(content,type);
        String token = to.token;
        System.out.println("[LOG] Test Request received with message: '"+to.object+"' and token: '"+token+"'");

        AuthenticatedUserList authenticatedUserList = AuthenticatedUserList.getInstance();
        if (authenticatedUserList.getUser(token)!=null) {
            System.out.println("[LOG] Token ok!");
            returnObj.object = "Key OK!";
        } else {
            System.out.println("[LOG] Token invalid!");
            returnObj.object = "Invalid Key!";
        }
        return returnObj;
    }

    private TransportObject<String> authenticate(String content) {
        TransportObject<String> returnObj = new TransportObject<String>(TransportObjectType.AUTHENTICATERESPONSE, "");

        Gson gson = new Gson();
        Type type = new TypeToken<TransportObject<UserModel>>(){}.getType();
        TransportObject<UserModel> toUser = gson.fromJson(content,type);
        UserModel u = (UserModel) toUser.object;
        System.out.println("[LOG] User received: "+u);

        UserAuthenticator uA = new UserAuthenticator();
        UserModel user = uA.authenticateUser(u.getName(), u.getPassword());
        if (user != null) {
            TokenHelper th = new TokenHelper();
            String token = th.generateToken();
            AuthenticatedUserList authenticatedUserList = AuthenticatedUserList.getInstance();
            authenticatedUserList.addUser(token, user);
            System.out.println("[LOG] Generated new Token for user '"+u.getName()+ "' : "+token);
            returnObj.object = token;
            System.out.println(token);
        } else {
            System.out.println("[LOG] User '"+u.getName()+ "' tried to authenticate with wrong credentials!");
            //returnObj.object = "Wrong credentials!!";
        }
        return returnObj;
    }
}
