package Cliente;
import java.io.*;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Cliente implements Runnable {

    private final String targa;
    private final String marca;
    private Socket clientSocket;
    private final static int NRO_RUN = 3;



    public Cliente(String _targa, String _marca){
        targa = _targa;
        marca = _marca;
         try {
            InetAddress addr;
            clientSocket = new Socket("localhost", 8080);
            InputStream is = clientSocket.getInputStream();//non ci dovrebbe servire

            addr = clientSocket.getInetAddress();
            System.out.println("Connected to " + addr);

             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

         } catch (IOException e) {
            System.out.println("Can't connect to localhost");
            System.out.println(e);
        }
    }

    public void run(){
        for (int i = 0; i<NRO_RUN; i++){
            System.out.println(" \t### entrata nel parcheggio " + targa + " | " + marca +" ### ");

            /* Richiesta ingresso ... */
            enterParking();

            /* Simulazione sosta in parcheggio ...*/
            try {
                System.out.println(" \t\t### sono nel parcheggio... " + targa + " | " + marca +" ### ");
                Thread.sleep ( 2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(" \t### sono uscito dal parcheggio... " + targa + " | " + marca +" ### ");

            /* Richiesta uscita ... */
            exitParking();

        }
    }

    private Socket createSocket() throws IOException {

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }




    private void enterParking(){
        String requestString = initRequestString("0");
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(requestString);
    }

    private void exitParking(){
        String requestString = initRequestString("1");
        PrintWriter out = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.write(requestString);
    }

    private String initRequestString(String request){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return request + marca + targa + formatter.format(date);
    }



}
