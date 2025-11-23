/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author QXC
 */
public class Carton  {
     private final int idCarton;
    private final int[][] numeros = new int[5][5];
    private final boolean[][] marcados = new boolean[5][5];
    private boolean ganador;
    private String tipoJugada;

    public Carton(int idCarton) {
        this.idCarton = idCarton;
    }

    public int getIdCarton() {
        return idCarton;
    }

    public int[][] getNumeros() {
        return numeros;
    }

    public boolean isGanador() {
        return ganador;
    }

    public String getTipoJugada() {
        return tipoJugada;
    }

    // 👇 NUEVO: para que el renderer pueda consultar si una casilla está marcada
    public boolean isMarcado(int fila, int col) {
        return marcados[fila][col];
    }

    // ===== GENERAR CARTÓN =====
    public void generarCarton() {
        Random rnd = new Random();

        int[][] rangos = {
            {1, 15},
            {16, 30},
            {31, 45},
            {46, 60},
            {61, 75}
        };

        for (int col = 0; col < 5; col++) {
            Set<Integer> usados = new HashSet<>();
            int desde = rangos[col][0];
            int hasta = rangos[col][1];

            for (int fila = 0; fila < 5; fila++) {
                if (fila == 2 && col == 2) { // casilla libre
                    numeros[fila][col] = 0;
                    marcados[fila][col] = true; // libre ya marcada
                    continue;
                }
                int n;
                do {
                    n = rnd.nextInt(hasta - desde + 1) + desde;
                } while (usados.contains(n));
                usados.add(n);
                numeros[fila][col] = n;
                marcados[fila][col] = false;
            }
        }
    }

    // ===== MARCAR NÚMERO =====
    public void marcarNumero(int numero) {
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (numeros[f][c] == numero) {
                    marcados[f][c] = true; // aquí se marcará
                }
            }
        }
    }

    // ===== REVISAR SI GANÓ =====
    public boolean revisarGanador() {
        // Filas
        for (int f = 0; f < 5; f++) {
            boolean ok = true;
            for (int c = 0; c < 5; c++) {
                if (!marcados[f][c]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                ganador = true;
                tipoJugada = "Fila " + (f + 1);
                return true;
            }
        }

        // Columnas
        for (int c = 0; c < 5; c++) {
            boolean ok = true;
            for (int f = 0; f < 5; f++) {
                if (!marcados[f][c]) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                ganador = true;
                tipoJugada = "Columna " + (c + 1);
                return true;
            }
        }

        // Diagonal principal
        boolean diag1 = true;
        for (int i = 0; i < 5; i++) {
            if (!marcados[i][i]) {
                diag1 = false;
                break;
            }
        }
        if (diag1) {
            ganador = true;
            tipoJugada = "Diagonal principal";
            return true;
        }

        // Diagonal secundaria
        boolean diag2 = true;
        for (int i = 0; i < 5; i++) {
            if (!marcados[i][4 - i]) {
                diag2 = false;
                break;
            }
        }
        if (diag2) {
            ganador = true;
            tipoJugada = "Diagonal secundaria";
            return true;
        }

        // Cuatro esquinas
        boolean esquinas = marcados[0][0] && marcados[0][4]
                && marcados[4][0] && marcados[4][4];

        if (esquinas) {
            ganador = true;
            tipoJugada = "Cuatro esquinas";
            return true;
        }

        return false;
    }
}
