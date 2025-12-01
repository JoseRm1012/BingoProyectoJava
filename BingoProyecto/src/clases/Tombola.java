/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa la tómbola del juego de bingo.
 * 
 * Administra una lista de números del 1 al 75 por que ese es el limite, los revuelve de forma aleatoria
 * y permite ir obteniendo las bolitas una a una en orden de extracción.
 *
 * @author QXC
 */
public class Tombola {
    //Se crea un array para la lista de números disponibles en la tómbola 
     private final List<Integer> numeros = new ArrayList<>();
    private int indiceActual;

    public Tombola() {
        inicializar();
    }

    // Llena de 1 a 75 para mezclar
    public void inicializar() {
        numeros.clear();
        for (int i = 1; i <= 75; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);
        indiceActual = 0;
    }
    
    //Va a indicar si todavia quedarian bolitas disponibles
    public boolean hayMasBolitas() {
        return indiceActual < numeros.size();
    }
    
     //Devuelve la siguiente bolita de la tómbola.
     // Si ya no hay más bolitas, devuelve -1.
     

    public int siguienteBolita() {
        if (!hayMasBolitas()) {
            return -1;
        }
        int n = numeros.get(indiceActual);
        indiceActual++;
        return n;
    }
}
