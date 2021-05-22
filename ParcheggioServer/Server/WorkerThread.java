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
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            var requestString = in.readLine();  //lettura dell'unica linea
            handleRequest(requestString);

/*            OutputStream output = clientSocket.getOutputStream();
            output.write(("Richiesta processata!").getBytes());   //converstion chars2bits
            output.close();*/
            in.close();

            System.out.println("Request processed");
        }catch (IOException e){
            System.out.println("Processo è terminato...");
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
