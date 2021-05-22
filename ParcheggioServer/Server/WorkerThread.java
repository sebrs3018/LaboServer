package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Time;

public class WorkerThread implements Runnable {

    protected Socket clientSocket;
    protected String serverName;

    public WorkerThread(Socket _clientSocket, String _serverName){
        clientSocket = _clientSocket;
        serverName   = _serverName;
    }

    @Override
    public void run() {

        try{
            InputStream input   = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            output.write(("Hello World").getBytes());   //converstion chars2bits
            output.close();
            input.close();

            System.out.println("Request processed");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
