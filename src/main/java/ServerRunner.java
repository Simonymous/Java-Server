public class ServerRunner {
    public static void main(String[] args) {
        Server server = new Server(9000);
        System.out.println("Server "+server.toString()+" wird jetzt gestartet...");
        new Thread(server).start();

        //Loop der den Server ein paar Sekunden laufen lässt! NUR TEST!
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Server "+server.toString()+" wird jetzt gestoppt...");
        server.stop();
    }

}
