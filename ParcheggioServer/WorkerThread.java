import java.net.Socket;

public class WorkerThread implements Runnable {

    protected Socket clientSocket;
    protected String serverName;

    public WorkerThread(Socket _clientSocket, String _serverName){
        clientSocket = _clientSocket;
        serverName   = _serverName;
    }

    @Override
    public void run() {
//        try{
//
//        }
    }
}
