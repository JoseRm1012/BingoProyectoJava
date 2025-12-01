/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

/**
 * Se crea la clase Persona con sus atributos, constructores lleno y vacio 
 * y sus respectivos setter and getter
 * @author QXC
 */
public class Persona {
      private String nombre;
    private String apellidos;
    private String cedula;
    private String telefono;
    private int numeroCarton;

    public Persona(String nombre, String apellidos, String cedula, String telefono, int numeroCarton) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.cedula = cedula;
        this.telefono = telefono;
        this.numeroCarton = numeroCarton;
    }

    public Persona() {
    }

    // Getters y Setters
    public String getNombre() {
        return nombre; 
    }
    public void setNombre(String nombre) {
        this.nombre = nombre; 
    }

    public String getApellidos() {
        return apellidos; 
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos; 
    }

    public String getCedula() {
        return cedula; 
    }
    public void setCedula(String cedula) {
        this.cedula = cedula; 
    }

    public String getTelefono() {
        return telefono; 
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono; 
    }

    public int getNumeroCarton() {
        return numeroCarton; 
    }
    public void setNumeroCarton(int numeroCarton) {
        this.numeroCarton = numeroCarton; 
    }
}

