package Cliente;
import java.io.IOException;
import java.lang.Runnable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
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
            clientSocket = new Socket("localhost", 9090);
            InputStream is = clientSocket.getInputStream();//non ci dovrebbe servire
            os = clientSocket.getOutputStream();

            addr = clientSocket.getInetAddress();
            System.out.println("Connected to " + addr);


             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));

         } catch (IOException e) {
            System.out.println("Can't connect to localhost");
            System.out.println(e);
        }
    }

    public void run(){

    }

    private Socket createSocket() throws IOException {

        List<Integer> ports = IntStream.rangeClosed(49152, 65535)
                    .boxed()
                    .collect(toList());


        for (int i = 49152; i <= 65535; i++) {
            try {
                return new Socket("localhost", i);
            } catch (IOException ex) {
                continue; // try next port
            }
        }

        // if the program gets here, no port in the range was found
        throw new IOException("no free port found");
    }
}
