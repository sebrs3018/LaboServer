package Server;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Timer;
import java.util.TimerTask;
public class Main {
    static int counter = 0;
    static Timer timer;

    public static void main(String[] args) {

        Parcheggio parcheggio = new Parcheggio();

        new Thread(new MultithreadedServer(8080, parcheggio)).start();

        CompletableFuture.runAsync(() -> {
            parcheggio.clearLog("Log.txt");
            for(int i = 0; i<1000; i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                    parcheggio.saveLog("Log.txt");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


       // try{
       //     Thread.sleep(20*1000);
       // } catch (InterruptedException e){
       //     e.printStackTrace();
       // }



    }
}
