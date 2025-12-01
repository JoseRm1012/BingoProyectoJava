/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.time.LocalDateTime;

/**
 *
 * @author QXC
 */

//Se crea la clase ganador 
public class Ganador {
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
