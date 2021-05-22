package Cliente;

import java.io.*;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Cliente implements Runnable {

    private String targa;
    private String marca;
    private Socket clientSocket;
    private final static int NRO_RUN = 10;


    public Cliente(String _targa, String _marca) throws IOException{
        targa = _targa;
        marca = _marca;

        InetAddress addr;
        clientSocket = new Socket("localhost", 8080);
        InputStream is = clientSocket.getInputStream();//non ci dovrebbe servire
        addr = clientSocket.getInetAddress();
        System.out.println("Connected to " + addr);

    }

    public void run(){

        for (int i = 0; i<NRO_RUN; i++){
            /* Richiesta ingresso ... */
            enterParking();
            System.out.println("Sono entrato!");
            /* Simulazione sosta in parcheggio ...*/
            try {
                Thread.sleep ( 2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /* Richiesta uscita ... */
            exitParking();

        }
    }


    private synchronized void enterParking(){
        String requestString = initRequestString("0");
        var dataBytes = requestString.getBytes(StandardCharsets.UTF_8);

        DataOutputStream out = null;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(dataBytes.length); //dimensione del messaggio
            out.write(dataBytes);           //payload
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private synchronized void exitParking(){
        String requestString = initRequestString("1");
        var dataBytes = requestString.getBytes(StandardCharsets.UTF_8);

        DataOutputStream out = null;
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeInt(dataBytes.length); //dimensione del messaggio
            out.write(dataBytes);           //payload
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String initRequestString(String request){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return request + " " + marca + " " + targa + " " + formatter.format(date);
    }


}
