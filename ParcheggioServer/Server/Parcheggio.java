package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Parcheggio {

    Map<String, String> Parcheggio = new ConcurrentHashMap<String, String>(); //struttura dati per tenere traccia delle targhe e modelli

    // costruttore per inizializzare le istanze (es. con il numero di posti totale)
    public Parcheggio(int _nroPostiLiberi){
    }

    /*un metodo “ENTRATA(MARCA,TARGA)” per richiedere l’ingresso dall’unica via di accesso
    e memorizzare i dati dell’auto quando viene posteggiata. Il metodo deve essere
    bloccante quando i posti sono tuZ occupati.*/
    public void Entrata(String Targa, String Marca){
        /* La putIfAbsent mi garantisce un inserimento sincronizzato */
        Parcheggio.putIfAbsent(Targa, Marca);
    }

    //un metodo “USCITA(TARGA)” per notificare l’uscita dal parcheggio. Il metodo non è
    //bloccante.
    public void Uscita(String targa){
        Parcheggio.remove(targa);
    }

    /* Un metodo “SALVA_LOG(FILENAME)” che salva sul file FILENAME la lista di auto
    attualmente parcheggiate. */
    public void salvaLog(String filename){
        try {
            FileWriter myWriter = new FileWriter(filename, true);
            Set<Map.Entry<String, String>> entry =  Parcheggio.entrySet();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            myWriter.write(dtf.format(now) + "\n");

            for (Map.Entry<String, String> theEntry : entry) {
                String key = theEntry.getKey();
                String value = theEntry.getValue();

                myWriter.write("Targa: " + key + "\tModello: " + value + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
