/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author QXC
 */
public class Tombola {

     private final List<Integer> numeros = new ArrayList<>();
    private int indiceActual;

    public Tombola() {
        inicializar();
    }

    // Llena de 1 a 75 y revuelve
    public void inicializar() {
        numeros.clear();
        for (int i = 1; i <= 75; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);
        indiceActual = 0;
    }

    public boolean hayMasBolitas() {
        return indiceActual < numeros.size();
    }

    public int siguienteBolita() {
        if (!hayMasBolitas()) {
            return -1;
        }
        int n = numeros.get(indiceActual);
        indiceActual++;
        return n;
    }
}
