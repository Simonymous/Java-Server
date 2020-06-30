import java.io.*;
import java.net.Socket;

public class RunnableWorker implements Runnable{
    protected Socket clientSocket = null;
    protected String workerName   = null;

    public RunnableWorker(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.workerName   = serverText;
    }

    public void run() {
        System.out.println("[LOG] "+this.workerName+": Thread wird bearbeitet...");
        try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            //Output für den Client (Derzeit String!)
            output.write(("HTTP/1.1 200 OK\n\nHey, ich bin der Worker! Wenn du das hier lesen kannst laueft alles und ich habe deine Anfrage bearbeitet :-) " +
                    "\n\nZugewiesener Worker: " +
                    this.workerName + "\n\nBearbeitet zum Zeitpunkt: " +
                    time +
                    "").getBytes());
            output.close();
            input.close();
            System.out.println("[LOG] "+this.workerName+": Request erfolgreich bearbeitet zum Zeitpunkt: " + time);
        } catch (IOException e) {
            //Fehler!!
            e.printStackTrace();
        }
    }
}
