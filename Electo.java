import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class Electo{
    public static class Votante{
        private int id;
        private final String nombreVotante;
        private boolean yaVoto;

        public Votante(int id, String nombreVotante){
            this.id = id;
            this.nombreVotante = nombreVotante;
            this.yaVoto = false;
        }
        public int getVotanteId(){
            return id;
        }
        public String getNombreVotante(){
            return nombreVotante;
        }
        public boolean isYaVoto(){
            return yaVoto;
        }
        public void marcarComoVotado(){
            this.yaVoto = true;
        }
    }
    public static class Voto{
        private int id;
        private int idVotante;
        private int idCandidato;
        private String timestamp;
    
    public Voto(int id, int idVotante, int idCandidato, String timestamp){
        this.id = id;
        this.idVotante = idVotante;
        this.idCandidato = idCandidato;
        this.timestamp = timestamp;
    }
    public int getId(){
        return id;
    }
    public int getIdVotante(){
        return idVotante;
    }
    public int getIdCandidato(){
        return idCandidato;
    }
    public String getTimestamp(){
        return timestamp;
    }
    }
    public static class Candidato{
        private int id; 
        private String nombre;
        private String partido;
        private Queue<Voto> votosRecibidos;
    
    public Candidato(int id, String nombre, String partido){
        this.id = id;
        this.nombre = nombre;
        this.partido = partido;
        this.votosRecibidos = new LinkedList<>();
    }
    public int getId(){
        return id;
    }
    public String getNombre(){
        return nombre;
    }
    public String getPartido(){
        return partido;
    }
    public Queue<Voto> obtenerVotosRecibidos(){
        return votosRecibidos;
    }
    public void agregarVoto(Voto voto) {
        this.votosRecibidos.add(voto);
    }
}
public static class UrnaElectoral{
    public LinkedList<Candidato> Listacandidatos;
    private final Stack<Voto> historialVotos;
    private int idCounter;

    public UrnaElectoral(){
        this.Listacandidatos = new LinkedList<>();
        this.historialVotos = new Stack<>();
        this.idCounter = 0;
    }
    public boolean verificarVotante(int idVotante, List<Votante> votantes){
        for(Votante votante : votantes){
            if(votante.getVotanteId() == idVotante && !votante.isYaVoto()){
                return true;
            }
        }
        return false;
    }
    public boolean registrarVoto(int idVotante, int idCandidato, String timestamp, List<Votante> votantes){
        if(verificarVotante(idVotante, votantes)){
            Voto nuevoVoto = new Voto(idCounter++, idVotante, idCandidato, timestamp);
            for(Candidato candidato : Listacandidatos){
                if(candidato.getId() == idCandidato){
                    candidato.agregarVoto(nuevoVoto);
                    historialVotos.push(nuevoVoto);
                    for(Votante votante : votantes){
                        if(votante.getVotanteId() == idVotante){
                            votante.marcarComoVotado();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public boolean reportarVoto(int idVoto){
        for(Candidato candidato : Listacandidatos){
            Queue<Voto> votos = candidato.obtenerVotosRecibidos();
            for(Voto voto : votos){
                if(voto.getId() == idVoto){
                    historialVotos.remove(voto);
                    votos.remove(voto);
                    return true;
                }
            }
        }
        return false;
    }
    public Map<Integer, Integer> ObtenerResultados() {
        Map<Integer, Integer> resultados = new HashMap<>();
        for (Candidato candidato : Listacandidatos) {
            resultados.put(candidato.getId(), candidato.obtenerVotosRecibidos().size());
        }
        return resultados;
    }
    public void agregarCandidato(Candidato candidato){
        this.Listacandidatos.add(candidato);
    }
}
public static void main(String[] args){
    UrnaElectoral urna = new UrnaElectoral();

    List<Votante> votantes = new ArrayList<>();
    Candidato candidato1 = new Candidato(1, "Sebastian Piñera", "Partido 1");
    Candidato candidato2 = new Candidato(2, "Michelle Bachellet", "Partido 2");
    Candidato candidato3 = new Candidato(3, "Gabriel Boric", "Partido 3");
    urna.agregarCandidato(candidato1);
    urna.agregarCandidato(candidato3);
    urna.agregarCandidato(candidato2);
    votantes.add(new Votante(1, "Diego"));
    urna.verificarVotante(1, votantes);
    System.out.println("Votante 1: " + votantes.get(0).getNombreVotante());
    votantes.add(new Votante(2, "David"));
    System.out.println("Votante 2: " + votantes.get(1).getNombreVotante());
    urna.verificarVotante(2, votantes);
    votantes.add(new Votante(3, "Donald"));
    System.out.println("Votante 3: " + votantes.get(2).getNombreVotante());
    urna.verificarVotante(3, votantes);
    votantes.add(new Votante(4, "Diana"));
    System.out.println("Votante 4: " + votantes.get(3).getNombreVotante());
    urna.verificarVotante(4, votantes);
    
    urna.registrarVoto(1, 1, "2025-01-10 10:00:00", votantes);
    urna.registrarVoto(2, 2, "2025-01-10 10:05:00", votantes);
    urna.registrarVoto(3, 3, "2025-01-10 10:10:00", votantes);
    urna.registrarVoto(4, 1, "2025-01-10 10:15:00", votantes);
    urna.registrarVoto(1, 2, "2025-01-10 10:20:00", votantes);
    urna.reportarVoto(5);  
    System.out.println(urna.ObtenerResultados());

    Map<Integer, Integer> resultados = urna.ObtenerResultados();
    System.out.println("Resultados:");
    resultados.forEach((idCandidato, votos) -> {
        System.out.println("Candidato ID: " + idCandidato + ", Votos: " + votos);
    });
}
}