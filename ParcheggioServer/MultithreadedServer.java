import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedServer implements  Runnable{

    protected int           serverPort      = 8080;
    protected ServerSocket  serverSocket    = null;
    protected boolean       isStopped       = false;
    protected Thread        runnningThread  = null;
    private final ExecutorService workerThreads;


    public MultithreadedServer(int port){
        serverPort = port;
        workerThreads = Executors.newFixedThreadPool(8);
    }


    @Override
    public void run() {

        synchronized (this){
            runnningThread = Thread.currentThread();
        }

        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!isStopped){
            Socket clientSocket = null;


            try{
                System.out.println("Waiting....");
                clientSocket = serverSocket.accept();   //questo metodo è bloccante finchè non arriva una connessione
            } catch (IOException e) {

                if(isStopped()){
                    System.out.println("Server is stopped!");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

            try{
                workerThreads.execute(new WorkerThread(clientSocket, "ThreadPooled Server") );
            }
            catch (Exception e){
                System.out.println("Weird exception... Still continuing!");
            }

        }


    }



    private boolean isStopped() {
        return isStopped;
    }
}
