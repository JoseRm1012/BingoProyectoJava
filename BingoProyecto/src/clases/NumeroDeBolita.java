/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
/**
 * Representa una bolita del bingo con su número asociado.
 * Se utiliza para manejar los números extraídos en la tómbola mientras vamos sacando cada bolita.
 *
 * @author QXC
 */
public class NumeroDeBolita {
    // Número de la bolita extraída en el juego de bingo.
     private int numero;
    //Se crea una nueva bolita con el numero
    public NumeroDeBolita(int numero) {
        this.numero = numero;
    }

    public NumeroDeBolita() {
    }

    public int getNumero() {
        return numero; 
    }
    public void setNumero(int numero) {
        this.numero = numero; 
    }
}

