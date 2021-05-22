package Server;

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
    protected Parcheggio parcheggio;


    public MultithreadedServer(int port, Parcheggio _parcheggio){
        serverPort = port;
        workerThreads = Executors.newFixedThreadPool(8);
        parcheggio = _parcheggio;
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
                System.out.println("In attesa che qualcuno si colleghi ....");
                clientSocket = serverSocket.accept();   //questo metodo è bloccante finchè non arriva una connessione
            } catch (IOException e) {

                if(isStopped()){
                    System.out.println("Server is stopped!");
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }

            try{
                //Elaboro richiesta su parcheggio ...
                workerThreads.execute(new WorkerThread(clientSocket, "ThreadPooled Server", parcheggio) );
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }


    private boolean isStopped() {
        return isStopped;
    }
}
