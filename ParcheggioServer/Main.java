import Server.MultithreadedServer;

public class Main {


    public static void main(String[] args) {

        new Thread(new MultithreadedServer(9000)).start();

        try{
            Thread.sleep(20*1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
