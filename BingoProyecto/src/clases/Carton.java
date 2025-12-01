/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import clases.Persona;   
import logica.ControladorJuego;


/**
 *
 * @author QXC
 */
  public class Carton extends Thread  {
       private final int idCarton;
    // Números del cartón organizados en una matriz 5x5.
    private final int[][] numeros = new int[5][5];
    
    //Indica si el cartón ha resultado ganador
    private final boolean[][] marcados = new boolean[5][5];
    private boolean ganador;
    
    // Describe el tipo de jugada ganadora (línea, cartón lleno, etc.)
    private String tipoJugada;
    //Si la Persona que compró el cartón
    private Persona comprador;
        // --- campos para manejar el hilo ---
    private final ControladorJuego controlador;
    private boolean activo = true;
    private int numeroPendiente;
    private boolean hayNumero = false;


    //Se genera los cartones y mediante los getter va a devolver los identificadores de cada carton
        public Carton(int idCarton, ControladorJuego controlador) {
        this.idCarton = idCarton;
        this.controlador = controlador;
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
    
      // getters/setters del comprador
    public Persona getComprador() {
        return comprador;
    }

    public void setComprador(Persona comprador) {
        this.comprador = comprador;
    }

    public boolean isVendido() {
        return comprador != null;
    }

    
    public boolean isMarcado(int fila, int col) {
        return marcados[fila][col];
    }

    // Se genera los cartones
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

    // Método para marcar el numero del carton
    public void marcarNumero(int numero) {
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                if (numeros[f][c] == numero) {
                    marcados[f][c] = true; // aquí se marcará
                }
            }
        }
    }

    // Metodo para revisar si  ya hay un ganador
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
        /**
     * Envia un nuevo número al cartón y despierta el hilo.
     */
    public void recibirNumero(int numero) {
        synchronized (this) {
            numeroPendiente = numero;
            hayNumero = true;
            notify();
        }
    }

    /**
     * Detiene el hilo del cartón.
     */
    public void detener() {
        activo = false;
        synchronized (this) {
            notify();
        }
    }

    @Override
    public void run() {
        try {
            while (activo) {
                int numero;

                // espera hasta que llegue un nuevo número
                synchronized (this) {
                    while (!hayNumero && activo) {
                        wait();
                    }
                    if (!activo) {
                        break;
                    }
                    numero = numeroPendiente;
                    hayNumero = false;
                }

                // marcar el número
                marcarNumero(numero);

                // revisar si este cartón ganó
                if (revisarGanador()) {
                    controlador.cartonGano(this);
                    activo = false;
                }
            }
        } catch (InterruptedException e) {
            // hilo terminado
        }
    }

}
