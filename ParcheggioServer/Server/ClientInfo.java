package Server;

public class ClientInfo {
    private String marca;
    private Long tempoGestione;

    public ClientInfo(String _marca, Long _tempoGestione){
        marca = _marca;
        tempoGestione = _tempoGestione;
    }


    public String getMarca() {
        return marca;
    }

    public String getTempoGestione() {
        return tempoGestione.toString();
    }
}
