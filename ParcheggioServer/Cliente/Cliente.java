package Cliente;

import java.io.*;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Cliente implements Runnable {

    private String targa;
    private String marca;
    private final static int NRO_RUN = 5;


    public Cliente(String _targa, String _marca) {
        targa = _targa;
        marca = _marca;
    }



    public void run(){

        try(Socket socket = new Socket("localhost", 8080)){
            for (int i = 0; i<NRO_RUN; i++){
                /* Richiesta ingresso ... */
                enterParking(socket);
                /* Simulazione sosta in parcheggio ...*/
                try {
                    var r = new Random().nextInt(10);
                    System.out.println("rand: " + r * 1000 );
                    Thread.sleep ( (r + 1) * 500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /* Richiesta uscita ... */
                exitParking(socket);
            }
        } catch (UnknownHostException ex) {
        System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }


    private  void enterParking(Socket clientSocket){
        try {
/*            String requestString = initRequestString("0");
            var dataBytes = requestString.getBytes(StandardCharsets.UTF_8);

            DataOutputStream out = null;

            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(dataBytes.length); //dimensione del messaggio
            out.write(dataBytes);           //payload*/

            writeMessage(clientSocket, initRequestString("0"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private  void exitParking(Socket clientSocket){

        try {
            System.out.println("\tRischiesta di uscita...");
            String requestString = initRequestString("1");
/*            var dataBytes = requestString.getBytes(StandardCharsets.UTF_8);

            DataOutputStream out = null;

            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(dataBytes.length); //dimensione del messaggio
            out.write(dataBytes);           //payload*/

        writeMessage(clientSocket, requestString);
            System.out.println("\t\t Fine rischiesta di uscita...");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String initRequestString(String request){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return request + " " + marca + " " + targa + " " + formatter.format(date);
    }


    private void writeMessage(Socket clientSocket, String requestString) throws  IOException{
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(requestString);
    }

}
