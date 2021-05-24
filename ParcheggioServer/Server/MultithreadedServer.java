package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultithreadedServer implements  Runnable{

    protected int           serverPort      = 8080;
    protected ServerSocket  serverSocket    = null;
    protected boolean       isStopped       = false;
    protected Thread        runnningThread  = null;
    private final ExecutorService workerThreads;
    protected Parcheggio parcheggio;
    private final int SERVER_CAPACITY = 8; //numero di client gestiti alla volta

    public MultithreadedServer(int port, Parcheggio _parcheggio){
        serverPort = port;
        parcheggio = _parcheggio;
        workerThreads = Executors.newFixedThreadPool(SERVER_CAPACITY);
    }


    @Override
    public void run() {

        synchronized (this){
            runnningThread = Thread.currentThread();
        }

        try {
            serverSocket = new ServerSocket(serverPort);
            serverSocket.setSoTimeout(60000);   //aspetto 60 secondi finchè non arriva nessuta connessione
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(!isStopped){
            Socket clientSocket = null;
            try{
                clientSocket = serverSocket.accept();   //questo metodo è bloccante finchè non arriva una connessione
            }

            catch (IOException e) {
                if(isStopped()){
                    System.out.println("Server is stopped!");
                    return;
                }
                throw new RuntimeException("Error accepting client connection");
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

    public void shutDownServer(){

        /* Da questo momento in poi non accetto più thread... */
        workerThreads.shutdown();

        try{
            if(!workerThreads.awaitTermination(60, TimeUnit.SECONDS)){
                workerThreads.shutdownNow();

                if(!workerThreads.awaitTermination(60, TimeUnit.SECONDS)){
                    System.err.println("Non è stato possibile spegnere il server");
                }
            }

            if(workerThreads.isTerminated())
                isStopped = true;

        }catch (InterruptedException e){ }
    }

    public boolean isStopped() {
        return isStopped;
    }
}
