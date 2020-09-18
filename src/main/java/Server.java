import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class Server implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected volatile boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected int          workerID = 0;

    public Server(int port){
        this.serverPort = port;
    }

    /**
     * run for Thread.run
     */
    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        System.out.println("[LOG] [Server]: Server hoert momentan auf Port "+this.serverPort);
        openServerSocket();
        //Basic loop der läuft! kann mit der .stop() Methode gestoppt werden
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("[LOG] [Server] Server gestoppt.") ;
                    return;
                }
                throw new RuntimeException(
                        "[LOG] [Server] Client-Verbindung konnte nicht angenommen werden!", e);
            }
            //Hier wird jede Verbindung einem eigenen Worker zugewiesen:
            System.out.println("[LOG] [Server]: Neuer Worker wird gestartet...");
            new Thread(
                    new RunnableWorker(
                            clientSocket, "[Worker"+workerID+"]")
            ).start();
            workerID++;
        }
        System.out.println("[LOG] [Server] Server gestoppt.") ;
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    /**
     * Try to close the server socket and then stop
     */
    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("[LOG] [Server] Server konnte nicht gestoppt werden! ", e);
        }
    }

    /**
     * Opens new Server Socket
     */
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("[LOG] [Server] Port konnte nicht gebindet werden!", e);
        }
    }


}
