import Server.MultithreadedServer;
import Server.Parcheggio;

public class Main {


    public static void main(String[] args) {

        Parcheggio parcheggio = new Parcheggio(100);

        new Thread(new MultithreadedServer(9000, parcheggio)).start();

        try{
            Thread.sleep(20*1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
