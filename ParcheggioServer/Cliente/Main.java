package Cliente;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {

        Cliente c1 = new Cliente("C1","Volvo");
        Cliente c2 = new Cliente("C2","Seat");
        Cliente c3 = new Cliente("C3","Volvo");
        Cliente c4 = new Cliente("C4","Mercedes");
        Cliente c5 = new Cliente("C5","Fiat");
        Cliente c6 = new Cliente("C6","Fiat");
        Cliente c7 = new Cliente("C7","Jeep");
        Cliente c8 = new Cliente("C8","BMW");


        ExecutorService es = Executors.newFixedThreadPool(8);

        for (int i = 0; i<10; i++){
            es.execute(c1);
            es.execute(c2);
            es.execute(c3);
            es.execute(c4);
            es.execute(c5);
            es.execute(c6);
            es.execute(c7);
            es.execute(c8);
        }

        //Graceful termination...
        es.shutdown();

    }


}
