/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;
import Vista.DlgDatosCliente;
import Vista.VistaPrincipall;
import clases.Carton;
import clases.Tombola;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import Vista.DlgVenderCarton;
import clases.Persona;

public class ControladorJuego {

    private final VistaPrincipall vista;
    private final ArrayList<Carton> cartones = new ArrayList<>();
    private final Tombola tombola = new Tombola();
    private boolean juegoEnCurso = false;
    private long inicioJuegoMs;
    private final ArrayList<Persona> personas = new ArrayList<>();

    public ControladorJuego(VistaPrincipall vista) {
        this.vista = vista;
    }
    
    // ========= VENDER CARTÓN =========
public void venderCarton() {
    if (cartones.isEmpty()) {
        JOptionPane.showMessageDialog(vista, "Primero genere los cartones.");
        return;
    }

    DlgVenderCarton dlg = new DlgVenderCarton(vista, true, cartones.size());
    dlg.setVisible(true);

    if (!dlg.isAceptado()) {
        return; // canceló
    }

    Persona p = dlg.getPersona();
    if (p == null) {
        return;
    }

    int num = p.getNumeroCarton();

    if (num < 1 || num > cartones.size()) {
        JOptionPane.showMessageDialog(vista, "Número de cartón inválido.");
        return;
    }

    Carton carton = cartones.get(num - 1);

    if (carton.isVendido()) {
        JOptionPane.showMessageDialog(vista, "Ese cartón ya está vendido.");
        return;
    }

    // guardar la persona y marcar cartón como vendido
    carton.setComprador(p);
    personas.add(p);
    marcarCartonComoVendido(num);
}
// ========= MOSTRAR DATOS DEL CLIENTE =========
public void mostrarDatosCliente(int numCarton) {
    if (cartones.isEmpty()) {
        JOptionPane.showMessageDialog(vista, "Primero genere y venda cartones.");
        return;
    }

    if (numCarton < 1 || numCarton > cartones.size()) {
        JOptionPane.showMessageDialog(vista, "Número de cartón inválido.");
        return;
    }

    Carton carton = cartones.get(numCarton - 1);
    Persona comprador = carton.getComprador();

    if (comprador == null) {
        JOptionPane.showMessageDialog(vista, "Este cartón aún no ha sido vendido.");
        return;
    }

    DlgDatosCliente dlg = new DlgDatosCliente(vista, true);
    dlg.setDatosCliente(comprador);   // 👈 solo pasa la persona
    dlg.setVisible(true);
}

private void marcarCartonComoVendido(int num) {
    switch (num) {
        case 1 -> vista.txtEstado1.setText("VENDIDO");
        case 2 -> vista.txtEstado2.setText("VENDIDO");
        case 3 -> vista.txtEstado3.setText("VENDIDO");
        case 4 -> vista.txtEstado4.setText("VENDIDO");
        case 5 -> vista.txtEstado5.setText("VENDIDO");
    }
}

    // ========= NUEVO JUEGO =========
    public void nuevoJuego() {
        juegoEnCurso = false;
        cartones.clear();
        tombola.inicializar();

        limpiarTablasCartones();
        limpiarTablaNumerosJugados();

        vista.txtEstado1.setText("DISPONIBLE");
        vista.txtEstado2.setText("DISPONIBLE");
        vista.txtEstado3.setText("DISPONIBLE");
        vista.txtEstado4.setText("DISPONIBLE");
        vista.txtEstado5.setText("DISPONIBLE");

        vista.lblBolitaTitulo.setText("Bolita N°");
    }

    // ========= GENERAR CARTONES =========
    public void generarCartones() {
        if (!cartones.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los cartones ya fueron generados.");
            return;
        }

        for (int i = 1; i <= 5; i++) {
            Carton c = new Carton(i);
            c.generarCarton();
            cartones.add(c);

            JTable tabla = obtenerTablaCarton(i);
            configurarRendererCarton(tabla, c);   // 👈 renderer para colorear
            llenarTablaCarton(tabla, c.getNumeros());
        }
    }

    // ========= COMENZAR BINGO =========
    public void comenzarBingo() {
        if (cartones.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero genere los cartones.");
            return;
        }
        juegoEnCurso = true;
        inicioJuegoMs = System.currentTimeMillis();
    }

    // ========= NUEVA BOLITA =========
    public void nuevaBolita() {
        if (!juegoEnCurso) {
            JOptionPane.showMessageDialog(vista, "Debe comenzar el bingo primero.");
            return;
        }

        if (!tombola.hayMasBolitas()) {
            JOptionPane.showMessageDialog(vista, "Ya no hay más bolitas.");
            juegoEnCurso = false;
            return;
        }

        int n = tombola.siguienteBolita();
        if (n == -1) return;

        // Mostrar bolita en el label
        vista.lblBolitaTitulo.setText("Bolita N° " + n);

        // Registrar en la tabla de números jugados
        agregarNumeroJugado(n);

        // Marcar en cartones y revisar ganador
        for (Carton c : cartones) {
            c.marcarNumero(n);  // se actualiza la matriz marcados

            // repintar la tabla correspondiente para que se vea el color
            JTable tabla = obtenerTablaCarton(c.getIdCarton());
            if (tabla != null) {
                tabla.repaint();
            }

            if (c.revisarGanador()) {
                juegoEnCurso = false;
                long duracionSeg = (System.currentTimeMillis() - inicioJuegoMs) / 1000;
                mostrarGanador(c, duracionSeg);
                break;
            }
        }
    }

    // ========= MÉTODOS PRIVADOS DE APOYO =========

    private JTable obtenerTablaCarton(int num) {
        return switch (num) {
            case 1 -> vista.tblCarton1;
            case 2 -> vista.tblCarton2;
            case 3 -> vista.tblCarton3;
            case 4 -> vista.tblCarton4;
            case 5 -> vista.tblCarton5;
            default -> null;
        };
    }

    // --- RENDERER PARA COLOREAR CASILLAS MARCADAS ---
    private void configurarRendererCarton(JTable tabla, Carton carton) {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                Component comp = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                // fondo por defecto
                comp.setBackground(Color.WHITE);

                // si la casilla está marcada en el cartón -> color
                if (carton.isMarcado(row, column)) {
                    comp.setBackground(Color.YELLOW); // o Color.RED
                }

                return comp;
            }
        });
    }

    // limpia las 5 tablas de cartones dejándolas 5x5 vacías
    private void limpiarTablasCartones() {
        limpiarTablaCarton(vista.tblCarton1);
        limpiarTablaCarton(vista.tblCarton2);
        limpiarTablaCarton(vista.tblCarton3);
        limpiarTablaCarton(vista.tblCarton4);
        limpiarTablaCarton(vista.tblCarton5);
    }

    private void limpiarTablaCarton(JTable tabla) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();
        int filas = m.getRowCount();
        int cols  = m.getColumnCount();
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                m.setValueAt("", f, c);
            }
        }
    }

    // llena el cartón con los números generados (5x5) ocupando todo el JTable
    private void llenarTablaCarton(JTable tabla, int[][] datos) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();

        // Rellenar valores 5x5
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                int valor = datos[f][c];
                m.setValueAt(valor == 0 ? "" : valor, f, c);
            }
        }

        // Ajustar la altura de fila para que las 5 filas llenen el alto del viewport
        java.awt.Component parent = tabla.getParent(); // normalmente el JViewport
        int alto = parent.getHeight();
        int filas = m.getRowCount();
        if (alto > 0 && filas > 0) {
            tabla.setRowHeight(alto / filas);
        } else {
            tabla.setRowHeight(30);
        }

        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.getTableHeader().setReorderingAllowed(false);
    }

    // deja vacía toda la grilla de NÚMEROS JUGADOS (5 x 15)
    private void limpiarTablaNumerosJugados() {
        DefaultTableModel m = (DefaultTableModel) vista.tblNumerosJugadoss.getModel();
        int filas = m.getRowCount();   // 15
        int cols  = m.getColumnCount(); // 5

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                m.setValueAt("", f, c);
            }
        }
    }

    // coloca el número en la primera celda vacía (recorriendo 5 x 15)
    private void agregarNumeroJugado(int n) {
        DefaultTableModel m = (DefaultTableModel) vista.tblNumerosJugadoss.getModel();
        int filas = m.getRowCount();   // 15
        int cols  = m.getColumnCount(); // 5

        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < cols; c++) {
                Object val = m.getValueAt(f, c);
                if (val == null || val.toString().trim().isEmpty()) {
                    m.setValueAt(n, f, c);
                    return; // ya lo pusimos
                }
            }
        }
        // Si se llenó la tabla completa, no agrega más
    }

    private void mostrarGanador(Carton carton, long duracionSegundos) {
        String mensaje = """
                         ¡BINGO!
                         Cartón ganador: %d
                         Tipo de jugada: %s
                         Tiempo de juego: %d segundos
                         """.formatted(
                carton.getIdCarton(),
                carton.getTipoJugada(),
                duracionSegundos
        );
        JOptionPane.showMessageDialog(vista, mensaje);
    }
}
