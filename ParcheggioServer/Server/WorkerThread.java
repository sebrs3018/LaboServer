package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.Arrays;

public class WorkerThread implements Runnable {

    protected Socket clientSocket;
    protected String serverName;
    protected Parcheggio parcheggio;

    public WorkerThread(Socket _clientSocket, String _serverName, Parcheggio _parcheggio){
        clientSocket = _clientSocket;
        serverName   = _serverName;
        parcheggio = _parcheggio;
    }

    @Override
    public void run() {

        try{
            DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
            int length = in.readInt();  //leggo la dimensione del messaggio

            byte[] messageByte = new byte[length];
            boolean end = false;
            StringBuilder dataString = new StringBuilder(length);
            int totalBytesRead = 0;
            while(!end) {
                int currentBytesRead = in.read(messageByte);
                totalBytesRead = currentBytesRead + totalBytesRead;
                if(totalBytesRead <= length) {
                    dataString
                            .append(new String(messageByte, 0, currentBytesRead, StandardCharsets.UTF_8));
                } else {
                    dataString
                            .append(new String(messageByte, 0, length - totalBytesRead + currentBytesRead,
                                    StandardCharsets.UTF_8));
                }
                if(dataString.length()>=length) {
                    end = true;
                }
            }

            handleRequest(dataString.toString());

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    /*
    * [0] => TIPO, [1] => MARCA, [2] => TARGA, [3] => ORA
    */
    private void handleRequest(String requestString){
        String[] parsedString = parseRequest(requestString);

        for (var val : parsedString){
            System.out.print(val + " ");
        }
        System.out.println("\n");

        if(parsedString[0].equals("0")){
            var timeToPass = LocalTime.parse( parsedString[3] ) ;
            parcheggio.Entrata(parsedString[2], parsedString[1], timeToPass);
        }
        else if (parsedString[0].equals("1")){
            parcheggio.Uscita(parsedString[2]);
        }
    }

    /* Precondizione: marca, targa e data/ora non sono nulli */
    private String[] parseRequest(String requestString){
        String delimSpace = " ";
        return requestString.split(delimSpace);

    }


}
