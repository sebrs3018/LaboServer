package Server;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Parcheggio {

    Map<String, ClientInfo> Parcheggio = new ConcurrentHashMap<>(); //struttura dati per tenere traccia delle targhe e modelli
    private boolean isOutOfParking = false;

    // costruttore per inizializzare le istanze (es. con il numero di posti totale)
    public Parcheggio(int _nroPostiLiberi){
    }

    /*un metodo “ENTRATA(MARCA,TARGA)” per richiedere l’ingresso dall’unica via di accesso
    e memorizzare i dati dell’auto quando viene posteggiata. Il metodo deve essere
    bloccante quando i posti sono tuZ occupati.*/
    public void Entrata(String Targa, String Marca, LocalTime oraRichiesta){

        /* La putIfAbsent mi garantisce un inserimento sincronizzato */
        long tempoGestione = ChronoUnit.MILLIS.between(oraRichiesta, LocalTime.now());
        Parcheggio.putIfAbsent(Targa, new ClientInfo(Marca, tempoGestione));
    }

    //un metodo “USCITA(TARGA)” per notificare l’uscita dal parcheggio. Il metodo non è
    //bloccante.
    public void Uscita(String targa){
//        System.out.println("- Rimuovo: " + targa);
        Parcheggio.remove(targa);
        setOutOfParking(true);
//        System.out.println("\t- Fuori dal parcheggio: " + targa + "\n numero macchine rimanenti: " + Parcheggio.size());
    }

    /* Un metodo “SALVA_LOG(FILENAME)” che salva sul file FILENAME la lista di auto
    attualmente parcheggiate. */
    public void saveLog(String filename){
        try {
            FileWriter myWriter = new FileWriter(filename, true);
            Set<Map.Entry<String, ClientInfo>> entry =  Parcheggio.entrySet();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            myWriter.write("["+ dtf.format(now) + "]\n");

            System.out.println("\t\tnumero utenti in parcheggio: " + Parcheggio.size());
            Iterator<Map.Entry<String, ClientInfo>> it = Parcheggio.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = it.next();

                String key = (String) pair.getKey();
                ClientInfo clientInfo = (ClientInfo) pair.getValue();
                myWriter.write("\tTarga: " + key + "\tModello: " + clientInfo.getMarca()  + "\ttempoGestione: " + clientInfo.getTempoGestione()+ "\n" );
                it.remove(); // avoids a ConcurrentModificationException
            }

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public synchronized boolean isOutOfParking() {
        return isOutOfParking;
    }

    public synchronized void setOutOfParking(boolean outOfParking) {
        isOutOfParking = outOfParking;
    }
}
