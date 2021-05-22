import Server.MultithreadedServer;
import Server.Parcheggio;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {

        Parcheggio parcheggio = new Parcheggio(100);

        new Thread(new MultithreadedServer(8080, parcheggio)).start();

        try{
            Thread.sleep(20*1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }


        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 0; i< 10; i++){
                    TimeUnit.MILLISECONDS.sleep(1000);
                    parcheggio.salvaLog("Log.txt");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
