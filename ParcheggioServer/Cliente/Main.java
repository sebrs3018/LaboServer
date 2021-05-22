package Cliente;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    public static void main(String[] args) {

        try {
        Cliente c1 = new Cliente("a1","Volvo1");
        Cliente c2 = new Cliente("a2","Volvo2");
        Cliente c3 = new Cliente("a3","Volvo3");
        Cliente c4 = new Cliente("a4","Volvo4");
        Cliente c5 = new Cliente("a5","Volvo5");
        Cliente c6 = new Cliente("a6","Volvo6");
        Cliente c7 = new Cliente("a7","Volvo7");
        Cliente c8 = new Cliente("a8","Volvo8");

        ExecutorService es = Executors.newFixedThreadPool(8);

        for (int i = 0; i<10; i++){
            es.execute(c1);
            es.execute(c2);
            es.execute(c3);
        }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


    }
}
