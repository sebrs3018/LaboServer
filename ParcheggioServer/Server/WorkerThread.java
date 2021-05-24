package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class WorkerThread implements Runnable {

    protected Socket clientSocket;
    protected String serverName;
    protected Parcheggio parcheggio;
    private boolean LastRequest = false;

    public WorkerThread(Socket _clientSocket, String _serverName, Parcheggio _parcheggio){
        clientSocket = _clientSocket;
        serverName   = _serverName;
        parcheggio = _parcheggio;
    }

    @Override
    public void run() {
        /* TODO: verificare corretta stampa e concorrenza*/
        try(InputStream input = clientSocket.getInputStream()){
                while(!LastRequest){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    String requestString = reader.readLine();   //rimane in attesa di avere un riga da leggere
                    if(requestString != null){
                        handleRequest(requestString);
                        sendResponse();
                        //System.out.println("Request processed: " + requestString);
                    }
                }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private void sendResponse() {
        try {
            OutputStream output = clientSocket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.println("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    /*
    * [0] => TIPO, [1] => MARCA, [2] => TARGA, [3] => ORA
    */
    private void handleRequest(String requestString){
        String[] parsedString = parseRequest(requestString);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        if(parsedString[0].equals("0")){
            var timeToPass = LocalTime.parse( parsedString[3] ) ;
            parcheggio.Entrata(parsedString[2], parsedString[1], timeToPass);
            System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + "Server: "+ parsedString[2] +"è dentro al parcheggio!");

        }
        else if (parsedString[0].equals("1")){
            parcheggio.Uscita(parsedString[2]);
            System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + "Server: "+ parsedString[2] +"è uscito dal parcheggio!");

            LastRequest = true;
        }
    }

    /* Precondizione: marca, targa e data/ora non sono nulli */
    private String[] parseRequest(String requestString){
        String delimSpace = " ";
        return requestString.split(delimSpace);

    }



}
