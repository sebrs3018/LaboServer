package Cliente;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    public static void main(String[] args) {
        Cliente c1 = new Cliente("a1","Volvo1");
        Cliente c2 = new Cliente("a2","Volvo2");
        Cliente c3 = new Cliente("a3","Volvo3");

        ExecutorService es = Executors.newFixedThreadPool(8);

        for (int i = 0; i<100; i++){
            es.execute(c1);
            es.execute(c2);
            es.execute(c3);
        }
    }
}
