package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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
            InputStream input   = clientSocket.getInputStream();
            var requestString = Arrays.toString(input.readAllBytes());
            gestisciRichiesta(requestString);

            OutputStream output = clientSocket.getOutputStream();
            output.write(("Richiesta processata!").getBytes());   //converstion chars2bits
            output.close();
            input.close();

            System.out.println("Request processed");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /*
    * [0] => TIPO, [1] => MARCA, [2] => TARGA, [3] => DATA/ORA
    */
    private void gestisciRichiesta(String requestString){
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
        String[] tokenizedString  = requestString.split(delimSpace);
        for (String uniqVal1 : tokenizedString) {
            System.out.println("*" + uniqVal1 + "*");
        }
        return tokenizedString;
    }
    

}
