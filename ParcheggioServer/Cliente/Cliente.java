package Cliente;

import java.io.*;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

public class Cliente implements Runnable {

    private String targa;
    private String marca;
    private final static int NRO_RUN = 5;


    public Cliente(String _targa, String _marca) {
        targa = _targa;
        marca = _marca;
    }

    public void run() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        dtf.format(LocalDateTime.now());

            for (int i = 0; i < NRO_RUN; i++) {
                    /* Richiesta ingresso ... */
                    System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + targa + ": Sta per inviare enter parking");
                    enterParking();
                    System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + targa + ": Enter parking inviata, inizio sosta");

                    /* Simulazione sosta in parcheggio; da 1 a 10 secondi di sosta...*/
                    try {
                        int r = new Random().nextInt(10);
                        Thread.sleep(((r == 0) ? (1 + r) : r) * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    /* Richiesta uscita ... */
                    System.out.print("[" + dtf.format(LocalDateTime.now()) + "]" + targa + ": Sta per inviare exit parking");
                    exitParking();
                    System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + targa + ": Exit parking inviata");
            }
            System.out.println("[" + dtf.format(LocalDateTime.now()) + "]" + targa + ": Routine terminata!");
    }

    /* Per ogni richiesta di ingresso ed uscita, apro un nuovo socket! */
    private void enterParking() {
        try (Socket socket = new Socket("localhost", 8080)) {
            writeMessage(socket, initRequestString("0"));
            waitResponse(socket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void waitResponse(Socket clientSocket) {
        try {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            InputStream input = clientSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String requestString;

            while((requestString = reader.readLine()) != null){
                System.out.println("\t\t" +requestString);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void exitParking() {
        try (Socket socket = new Socket("localhost", 8080)) {
            writeMessage(socket, initRequestString("1"));
            waitResponse(socket);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private String initRequestString(String request) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return request + " " + marca + " " + targa + " " + formatter.format(date);
    }

    private void writeMessage(Socket clientSocket, String requestString) throws IOException {
        OutputStream output = clientSocket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println(requestString);
    }

}

