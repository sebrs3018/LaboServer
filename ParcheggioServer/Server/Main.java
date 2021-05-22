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

        Parcheggio parcheggio = new Parcheggio(100);

        new Thread(new MultithreadedServer(8080, parcheggio)).start();

        CompletableFuture.runAsync(() -> {
            for(int i = 0; i<100; i++){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                    parcheggio.saveLog("Log.txt");   //è tempo di chiusura
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });



/*        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if(counter <= 10)
                    parcheggio.saveLog("Log.txt");   //è tempo di chiusura
                counter++;
            }
        };*/
 /*       timer = new Timer("MyTimer");//create a new Timer
        timer.schedule(timerTask, 0, 500);//this line starts the timer at the same time its executed
*/

        try{
            Thread.sleep(20*1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }



    }
}
