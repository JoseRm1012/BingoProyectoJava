
package logica;
import Vista.DlgDatosCliente;
import Vista.DlgHistorial;
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




/**
 * Controla la lógica principal del juego de bingo.
 * Se encarga de manejar la vista, los cartones, la tómbola y las personas.
 */
public class ControladorJuego {
    private String ultimoHistorial;  // texto del último ganador


    private final VistaPrincipall vista;
    //Array de los cartones que se usan en el juego
    private final ArrayList<Carton> cartones = new ArrayList<>();
    //Generara las bolitas 
    private final Tombola tombola = new Tombola();
    private boolean juegoEnCurso = false;
    private long inicioJuegoMs;
    //Array de las personas que se registro que realizaron la compra 
    private final ArrayList<Persona> personas = new ArrayList<>();
    private Persona ultimoGanadorPersona;
    private int ultimoGanadorCarton;
    private String ultimoGanadorJugada;
    private long ultimoGanadorTiempo;
    
    // Premios posibles para el ganador
    private final String[] premios = {
    "Pantalla de 72 pulgadas",
    "Una refrigeradora",
    "Un iPhone"
    };

    public ControladorJuego(VistaPrincipall vista) {
        this.vista = vista;
    }
    
    //Metodo para vender los cartones
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
    // Metodo para mostrar datos de los clientes que se registro
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
    dlg.setDatosCliente(comprador);   
    dlg.setVisible(true);
}
    //Luego de haber seleccionado los cartones se marcara como vendidos en cada carton
    private void marcarCartonComoVendido(int num) {
        switch (num) {
        case 1 -> vista.txtEstado1.setText("VENDIDO");
        case 2 -> vista.txtEstado2.setText("VENDIDO");
        case 3 -> vista.txtEstado3.setText("VENDIDO");
        case 4 -> vista.txtEstado4.setText("VENDIDO");
        case 5 -> vista.txtEstado5.setText("VENDIDO");
    }
}

    /**
     * Inicia un nuevo juego de bingo.
     * Limpia cartones, números jugados, reinicia la tómbola
     * y pone todos los estados de los cartones como "DISPONIBLE".
     */
    
    public void nuevoJuego() {
    juegoEnCurso = false;

    // detener hilos de cartones
    for (Carton c : cartones) {
        c.detener();
    }
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


     /**
     * Genera los 5 cartones de bingo si aún no existen.
     * Muestra un mensaje si ya fueron generados antes.
     */
    public void generarCartones() {
    if (!cartones.isEmpty()) {
        JOptionPane.showMessageDialog(vista, "Los cartones ya fueron generados.");
        return;
    }

    for (int i = 1; i <= 5; i++) {
        Carton c = new Carton(i, this);  
        c.generarCarton();
        cartones.add(c);

        JTable tabla = obtenerTablaCarton(i);
        configurarRendererCarton(tabla, c);
        llenarTablaCarton(tabla, c.getNumeros());

        c.start(); // arranca el hilo del cartón
    }
}

    
    /**
     * Comienza el juego de bingo.
     * Si no hay cartones generados, muestra un mensaje.
     */
    
    public void comenzarBingo() {
        if (cartones.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Primero genere los cartones.");
            return;
        }
        juegoEnCurso = true;
        inicioJuegoMs = System.currentTimeMillis();
    }

    /**
     * Saca una nueva bolita de la tómbola y actualiza el juego.
     * Verifica que el bingo haya comenzado, muestra la bolita en la vista,
     * la agrega a la tabla de números jugados, marca el número en los cartones
     * y revisa si hay algún cartón ganador.
     */
    
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

    // Enviar número a cada cartón (hilo) y repintar la tabla
    for (Carton c : cartones) {
        c.recibirNumero(n);

        JTable tabla = obtenerTablaCarton(c.getIdCarton());
        if (tabla != null) {
            tabla.repaint();
        }
    }
    
}

    

    
    //Va obtener la tabla de cada carton
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

    
    /**
     * Configura la tabla para que pinte en color
     * las casillas marcadas del cartón.
     *
     */
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

                // si la casilla está marcada en el cartón se pintara de amarillo
                if (carton.isMarcado(row, column)) {
                    comp.setBackground(Color.RED); 
                }

                return comp;
            }
        });
    }
    
    /**
    *   Limpia las 5 tablas de cartones dejándolas 5x5 vacías
    *
    */
   
    private void limpiarTablasCartones() {
        limpiarTablaCarton(vista.tblCarton1);
        limpiarTablaCarton(vista.tblCarton2);
        limpiarTablaCarton(vista.tblCarton3);
        limpiarTablaCarton(vista.tblCarton4);
        limpiarTablaCarton(vista.tblCarton5);
    }

    
    /**
     * Limpia una tabla de cartón, dejando todas las celdas vacías.
     *
     */
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

      /**
     * Llena una tabla con los números de un cartón (matriz 5x5)
     * y ajusta la altura de las filas.
     *
     */
    private void llenarTablaCarton(JTable tabla, int[][] datos) {
        DefaultTableModel m = (DefaultTableModel) tabla.getModel();

        // Rellenar valores 5x5
        for (int f = 0; f < 5; f++) {
            for (int c = 0; c < 5; c++) {
                int valor = datos[f][c];
                m.setValueAt(valor == 0 ? "" : valor, f, c);
            }
        }

        java.awt.Component parent = tabla.getParent(); 
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
    
     /**
     * Limpia la tabla de números jugados, dejando todas las celdas vacías.
     */
   
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
                    return;
                }
            }
        }
       
    }

    
    /**
     * Muestra un mensaje con la información del cartón ganador con el tipo de juagada y la duracion.
     * 
     */
    private void mostrarGanador(Carton carton, long duracionSegundos) {
    Persona p = carton.getComprador();

    String nombre   = (p != null) ? p.getNombre() + " " + p.getApellidos() : "Sin datos";
    String cedula   = (p != null) ? p.getCedula() : "Sin datos";
    String telefono = (p != null) ? p.getTelefono() : "Sin datos";

   
    ultimoHistorial = """
            Cartón ganador: %d
            Tipo de jugada: %s
            Tiempo de juego: %d segundos
            Nombre: %s
            Cédula: %s
            Teléfono: %s
            """.formatted(
            carton.getIdCarton(),
            carton.getTipoJugada(),
            duracionSegundos,
            nombre,
            cedula,
            telefono
    );

    // mensaje normal al ganar
    JOptionPane.showMessageDialog(vista, "¡BINGO!\n\n" + ultimoHistorial);
}

        /**
     * Método llamado por un cartón cuando gana.
     */
    public synchronized void cartonGano(Carton carton) {
    if (!juegoEnCurso) {
        return;
    }

    juegoEnCurso = false;
    long duracionSeg = (System.currentTimeMillis() - inicioJuegoMs) / 1000;

    // Guarda los datos del ganador
    Persona p = carton.getComprador();
    ultimoGanadorPersona = p;
    ultimoGanadorCarton = carton.getIdCarton();
    ultimoGanadorJugada = carton.getTipoJugada();
    ultimoGanadorTiempo = duracionSeg;

    
    mostrarGanador(carton, duracionSeg);

    // detener los demás cartones
    for (Carton c : cartones) {
        if (c != carton) {
            c.detener();
        }
    }
}

    public void verHistorial() {
    if (ultimoGanadorPersona == null) {
        JOptionPane.showMessageDialog(vista,
                "Todavía no hay ningún ganador registrado.");
        return;
    }

    // Crear el diálogo y pasarle los datos del último ganador
    DlgHistorial dlg = new DlgHistorial(vista, true);

    String nombreCompleto = ultimoGanadorPersona.getNombre()
            + " " + ultimoGanadorPersona.getApellidos();

    dlg.setDatos(
            nombreCompleto,
            ultimoGanadorPersona.getCedula(),
            ultimoGanadorPersona.getTelefono(),
            ultimoGanadorCarton,
            ultimoGanadorJugada
            
            );

    dlg.setVisible(true);
}



}
