package Server;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Parcheggio {

    Map<String, ClientInfo> Parcheggio = new ConcurrentHashMap<>(); //struttura dati per tenere traccia delle targhe e modelli
    private boolean isOutOfParking = false;
    private boolean firstEntry = false;

    /*
        un metodo “ENTRATA(MARCA,TARGA)” per richiedere l’ingresso dall’unica via di accesso
        e memorizzare i dati dell’auto quando viene posteggiata. Il metodo deve essere
        bloccante quando i posti sono tuZ occupati.
        */
    public void Entrata(String Targa, String Marca, LocalTime oraRichiesta){
        firstEntry = true;

        /* La putIfAbsent mi garantisce un inserimento sincronizzato */
        long tempoGestione = ChronoUnit.MILLIS.between(oraRichiesta, LocalTime.now());
        Parcheggio.putIfAbsent(Targa, new ClientInfo(Marca, tempoGestione));
    }

    //un metodo “USCITA(TARGA)” per notificare l’uscita dal parcheggio. Il metodo non è
    //bloccante.
    public void Uscita(String targa){
        Parcheggio.remove(targa);
        setOutOfParking(true);
    }

    /*
    Un metodo “SALVA_LOG(FILENAME)” che salva sul file FILENAME la lista di auto
    attualmente parcheggiate.
    */
    public void saveLog(String filename){

        if(!firstEntry) //inizio a stampare appena entra qualcuno
            return;

        try {
            FileWriter myWriter = new FileWriter(filename, true);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            myWriter.write("["+ dtf.format(now) + "]\n");

            Iterator<Map.Entry<String, ClientInfo>> it = Parcheggio.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = it.next();

                String key = (String) pair.getKey();
                ClientInfo clientInfo = (ClientInfo) pair.getValue();
                myWriter.write("\tTarga: " + key + "\tModello: " + clientInfo.getMarca()  + "\ttempo di gestione richiesta: " + clientInfo.getTempoGestione()+ "ms\n" );
                it.remove(); // avoids a ConcurrentModificationException
            }

            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void clearLog(String filename){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        writer.print("");
        writer.close();
    }

    public synchronized void setOutOfParking(boolean outOfParking) {
        isOutOfParking = outOfParking;
    }
}
