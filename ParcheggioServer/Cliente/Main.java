package Cliente;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    public static void main(String[] args) {

        Cliente c1 = new Cliente("CD987BB","Volvo");
        Cliente c2 = new Cliente("CV166AT","Seat");
        Cliente c3 = new Cliente("FE853IT","Volvo");
        Cliente c4 = new Cliente("FH246BN","Mercedes");
        Cliente c5 = new Cliente("AL895TE","Fiat");
        Cliente c6 = new Cliente("DT852VR","Fiat");
        Cliente c7 = new Cliente("CF888ID","Jeep");
        Cliente c8 = new Cliente("DP777GK","BMW");


        ExecutorService es = Executors.newFixedThreadPool(8);


        es.execute(c1);
        es.execute(c2);
        es.execute(c3);
        es.execute(c4);
        es.execute(c5);
        es.execute(c6);
        es.execute(c7);
        es.execute(c8);



    }
}
