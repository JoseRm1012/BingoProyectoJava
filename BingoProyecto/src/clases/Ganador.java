
package clases;

import java.time.LocalDateTime;

/**
 * Clase  del jugador que ganó una partida
 * persona, número de cartón, tipo de jugada, premio etc.
 */


public class Ganador {
    //Atributos
     private Persona persona;
    private int numeroCarton;
    private String tipoJugada;     
    private LocalDateTime fechaHora;

    
    //Constructores vacio y lleno
    public Ganador(Persona persona, int numeroCarton, String tipoJugada, LocalDateTime fechaHora) {
        this.persona = persona;
        this.numeroCarton = numeroCarton;
        this.tipoJugada = tipoJugada;
        this.fechaHora = fechaHora;
    }
    
    public Ganador() {
    }
    //Setter and Getter

    public Persona getPersona() {
        return persona; 
    }
    public void setPersona(Persona persona) {
        this.persona = persona; 
    }

    public int getNumeroCarton() {
        return numeroCarton; 
    }
    public void setNumeroCarton(int numeroCarton) { 
        this.numeroCarton = numeroCarton; 
    }

    public String getTipoJugada() {
        return tipoJugada; 
    }
    public void setTipoJugada(String tipoJugada) {
        this.tipoJugada = tipoJugada; 
    }

    public LocalDateTime getFechaHora() {
        return fechaHora; 
    }
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora; 
    }
}
