package Server;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

            readRequest();
            sendResponse();

        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void readRequest() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String requestString ;

        if((requestString = reader.readLine()) != null){     //rimane in attesa di avere un riga da leggere
            handleRequest(requestString);
        }
    }

    private void sendResponse() throws IOException {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
            writer.println("OK");
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
            System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + "Server: "+ parsedString[2] +" è dentro al parcheggio!");
        }
        else if (parsedString[0].equals("1")){
            parcheggio.Uscita(parsedString[2]);
            System.out.println("\t[" + dtf.format(LocalDateTime.now()) + "]" + "Server: "+ parsedString[2] +" è uscito dal parcheggio!");
        }
    }

    /* Precondizione: marca, targa e data/ora non sono nulli */
    private String[] parseRequest(String requestString){
        String delimSpace = " ";
        return requestString.split(delimSpace);

    }



}
