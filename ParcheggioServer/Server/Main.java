package Server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Parcheggio parcheggio = new Parcheggio();
        MultithreadedServer multithreadedServer = new MultithreadedServer(8080, parcheggio);

        new Thread(multithreadedServer).start();

        CompletableFuture.runAsync(() -> {
            parcheggio.clearLog("Log.txt");

            while(!multithreadedServer.isStopped){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    parcheggio.saveLog("Log.txt");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            Thread.sleep(60000); //dopo 60 secondi spengo il server
            multithreadedServer.shutDownServer();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
