package Server;

import java.io.*;
import java.net.Socket;

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
    * [0] => TIPO, [1] => MARCA, [2] => TARGA, [3] => DATA/ORA
    */
    private void handleRequest(String requestString){
        String[] parsedString = parseRequest(requestString);

        if(parsedString[0].equals("0")){
            parcheggio.Entrata(parsedString[2], parsedString[1]);
        }
        else{
            parcheggio.Uscita(parsedString[1]);
        }
    }

    /* Precondizione: marca, targa e data/ora non sono nulli */
    private String[] parseRequest(String requestString){
        String delimSpace = " ";
        return requestString.split(delimSpace);

    }


}
