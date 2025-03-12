/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vista;

import Controlador.filtrosAplicar;
import Controlador.registroControlador;
import Controlador.vehiculoControlador;
import DAO.PersonaDAO;
import DAO.RegistroDAO;
import DAO.VehiculoDAO;
import DAO.conexionBD;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.TransferHandler;

/**
 *
 * @author yosoy
 */
public class codigo extends JFrame {

    // Declaración de las variables
    private RegistroDAO rdao;
    private JPanel p2;
    private JTextField tNombre;
    private JTextField tMatriculacion;
    private JComboBox<String> cMarca;
    private JComboBox<String> cModelo;
    private VehiculoDAO vdao;
    private DefaultTableModel modeloTabla;
    private conexionBD cbd = new conexionBD();
    private Connection conex = cbd.conectar();
    private PersonaDAO pdao;
    private ventAsociar va;
    private filtrosAplicar fa;
    private int paginaInicial = 1;
    private final int limite = 10;
    private vehiculoControlador vc;
    private registroControlador rc;
    private ventCrearPersona vcp;
    private JTable tb;
    private JRadioButton rbChico;
    private JRadioButton rbChica;
    private JPanel pFiltrosAvan;
    JButton bAnterior;
    JButton bSiguiente;
    private int totalPaginas = 1;
    private static final String valido = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]+$";

    // Constructor de la clase, que configura la ventana principal de la aplicación
    public codigo() {
        // Inicialización de objetos de controladores y ventanas
        vdao = new VehiculoDAO(conex);
        pdao = new PersonaDAO();
        rc = new registroControlador();
        va  = new ventAsociar(pdao, vdao, conex);
        vcp = new ventCrearPersona(pdao, va);

        // Configuración de la ventana principal
        setTitle("Registro de Vehículos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLayout(new BorderLayout());

        // Panel principal de la interfaz gráfica
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        // Barra de menú con opciones para crear, asociar y realizar otras acciones
        JMenuBar mb = new JMenuBar();
        JMenu mOpciones = new JMenu("Opciones");
        JMenu mCrear = new JMenu("Crear");
        JMenuItem itAsociar = new JMenuItem("Asociar");
        JMenuItem itPersona = new JMenuItem("Persona");
        JMenuItem itVehiculo = new JMenuItem("Vehiculo");

        // Acciones de los ítems del menú
        itAsociar.addActionListener(e -> va.setVisible(true));
        itPersona.addActionListener(e -> {
            if (!vcp.isVisible()) {
                vcp.setVisible(true);
            }
        });
//accion combobox
        itVehiculo.addActionListener(e -> {
            if (vc == null) {
                JComboBox<String> cbMarca = new JComboBox<>();
                JComboBox<String> cbModelo = new JComboBox<>();
                vc = new vehiculoControlador(cbMarca, cbModelo);
            }
            VentanaCrearVehiculo vcv = new VentanaCrearVehiculo(vc);
            vcv.setVisible(true);
        });

        // Agregar los ítems al menú
        mCrear.add(itPersona);
        mCrear.add(itVehiculo);
        mOpciones.add(mCrear);
        mOpciones.add(itAsociar);
        mb.add(mOpciones);
        setJMenuBar(mb);
        add(panelPrincipal, BorderLayout.CENTER);

        // Panel para los filtros de búsqueda
        JPanel p1 = new JPanel(new FlowLayout());
        JLabel lNombre = new JLabel("Nombre: ");
        tNombre = new JTextField(10);
        // Configurar el campo tNombre con un KeyListener
        tNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                if (Character.isISOControl(c)) {
                    return; // No hacer nada si es una tecla de control
                }

                // Validar el carácter ingresado
                if (!Character.toString(c).matches(valido)) {
                    e.consume(); // No permite el carácter en el campo
                    JOptionPane.showMessageDialog(null, "Caracter no válido: " + c, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lMarca = new JLabel("Marca: ");
        cMarca = new JComboBox<>(new String[]{"Todas", "Toyota", "Ford", "BMW", "Audi"});
        pFiltrosAvan = new JPanel();
        JLabel lFiltros = new JLabel("Filtros Avanzados");
        pFiltrosAvan.add(lFiltros);
        JButton bFiltros = new JButton("Filtros");

        // JRadioButtons para seleccionar sexo (M/F)
        rbChico = new JRadioButton("M");
        rbChica = new JRadioButton("F");

        // Agrupar los JRadioButtons 
        ButtonGroup sexos = new ButtonGroup();
        sexos.add(rbChico);
        sexos.add(rbChica);

        // Evento para ocultar y desocultar los filtros
        pFiltrosAvan.addMouseListener(new MouseAdapter() {
            private boolean isP2Visible = false;

            @Override
            public void mouseClicked(MouseEvent e) {
                isP2Visible = !isP2Visible;
                p2.setVisible(isP2Visible);
                pFiltrosAvan.revalidate();
                pFiltrosAvan.repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                pFiltrosAvan.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                pFiltrosAvan.setBackground(null);
            }
        });

        // Agregar los componentes al panel de filtros
        p1.add(lNombre);
        p1.add(tNombre);
        p1.add(rbChico);
        p1.add(rbChica);
        p1.add(lMarca);
        p1.add(cMarca);
        p1.add(pFiltrosAvan);
        p1.add(bFiltros);

        // ComboBox para modelos de vehículos
        cModelo = new JComboBox<>(new String[]{
            "Todos", "Corolla", "Focus", "X3", "A4", "Camry", "320i", "Q5", "Mustang", "Prius",
            "X5", "A3", "Highlander", "Escape", "M3", "TT", "Edge", "RAV4", "Z4", "Q7", "Fiesta"
        });

        // Instanciar el objeto que maneja los filtros aplicados
        fa = new filtrosAplicar(cMarca, cModelo, vdao, tNombre, tMatriculacion, modeloTabla);
        cMarca.addActionListener(e -> fa.actuModelos());
        panelPrincipal.add(p1);

        // Panel para los filtros adicionales
        p2 = new JPanel(new FlowLayout());
        JLabel lModelo = new JLabel("Modelo: ");
        p2.add(lModelo);
        p2.add(cModelo);
        JLabel lMatriculacion = new JLabel("Año de Matriculación: ");
        tMatriculacion = new JTextField(5);
        tMatriculacion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();

                // Ignorar teclas especiale
                if (Character.isISOControl(c)) {
                    return;
                }

                // Validar que solo se permitan números
                if (!Character.isDigit(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten números", "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Validar longitud máxima (por ejemplo, 4 dígitos para un año)
                if (tMatriculacion.getText().length() >= 4) {
                    e.consume(); // Bloqueo mas de4 numeros
                    JOptionPane.showMessageDialog(null, "Solo se permiten 4 dígitos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        p2.add(lMatriculacion);
        p2.add(tMatriculacion);
        p2.setVisible(false);

        panelPrincipal.add(p2);

        // Crear la tabla para mostrar los datos
        String[] columnas = {"Nombre", "Sexo", "DNI", "Matrícula", "Año", "Marca", "Modelo", "Fecha Inicio", "Fecha Fin", "NumPropietarios"};
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda será editable
            }
        };
        tb = new JTable(modeloTabla);
        JScrollPane sp = new JScrollPane(tb);
        tb.setAutoCreateRowSorter(true);

        panelPrincipal.add(sp);

        // Panel para los botones de navegación y acciones
        JPanel p4 = new JPanel();
        bAnterior = new JButton("Anterior");
        bSiguiente = new JButton("Siguiente");
        JButton bEliminar = new JButton("Eliminar");
        JButton bVaciar = new JButton("Vaciar");
        p4.add(bAnterior);
        p4.add(bSiguiente);
        p4.add(bEliminar);
        p4.add(bVaciar);

        // Acción para el botón de filtros
        bFiltros.addActionListener(e -> aplicarFiltros());

        // Acciones para los botones
        bVaciar.addActionListener(e -> {
            // Vaciar los campos de texto y restablecer los filtros
            tNombre.setText("");
            tMatriculacion.setText("");
            cMarca.setSelectedIndex(0);
            cModelo.setSelectedIndex(0);
            sexos.clearSelection();
            obtenerDatos();  // Obtener los datos iniciales sin filtros
        });
        //bton de eliminar
        bEliminar.addActionListener(e -> {
            // Eliminar la fila seleccionada de la tabla
            eliminarFilaSeleccionada();
        });

//---------------------BOTONES ANTERIOR/SIGUIENTE--------------------------------//
//boton de pagina anterior
        bAnterior.addActionListener(e -> {
            if (paginaInicial > 1) {
                paginaInicial--;
                obtenerDatos(); // Recarga los datos de la página anterior
            }
        });
//boton de pagina siguiente
        bSiguiente.addActionListener(e -> {
            if (paginaInicial < totalPaginas) {
                paginaInicial++;
                obtenerDatos(); // Recarga los datos de la página siguiente
            }
        });

        //aplicar lso filtros con enter
        bFiltros.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "aplicarFiltros");

        bFiltros.getActionMap().put("aplicarFiltros", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bFiltros.doClick(); // Simula un clic en el botón
            }
        });

        //bloqueo de copy paste
        // Bloquear pegar contenido no válido
        bloquearPegado(tNombre, "^[a-zA-ZáéíóúÁÉÍÓÚñÑü' ]+$", "Solo se permiten letras y espacios");
        bloquearPegado(tMatriculacion, "^[0-9]{0,4}$", "Solo se permiten números (máximo 4 dígitos)");

        // Agregar panel de botones al final de la ventana
        add(p4, BorderLayout.SOUTH);

        obtenerDatos();

        pack();
        setVisible(true);  // Hacer visible la ventana principal (solo la principal se muestra al inicio)
    }

    public static void main(String[] args) {

        new codigo();  // Ejecutar el constructor de la clase

    }

    //------------------------------------------FUNCIONES---------------------------------------------//
    private void obtenerDatos() {
        if (tb != null) {  // Verificar si la tabla está inicializada
            // Normalizar el texto del nombre
            String nombre = tNombre.getText().trim().replaceAll("\\s+", " ");

            // Si el campo nombre está vacío después de normalizar, establecerlo como null
            nombre = nombre.isEmpty() ? null : nombre;

            String marca = (String) cMarca.getSelectedItem();
            String modeloFiltro = (String) cModelo.getSelectedItem();

            // Determinar el sexo seleccionado (M/F)
            char sexo = '?';  // Valor predeterminado
            if (rbChico.isSelected()) {
                sexo = 'M';
            } else if (rbChica.isSelected()) {
                sexo = 'F';
            }

            Integer anio = null;
            if (!tMatriculacion.getText().isEmpty()) {
                try {
                    anio = Integer.parseInt(tMatriculacion.getText());  // Validar que el año sea un número
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Llamar al controlador para mostrar los datos en la tabla con los filtros aplicados
            rc.mostrarDatos(modeloTabla, paginaInicial, nombre, marca, modeloFiltro, anio, sexo);

            calcularTotalPaginas();
            actualizarEstadoBotones();
        }
    }

    // Método para act o desactivar botones segun la pagina
    private void actualizarEstadoBotones() {
        bAnterior.setEnabled(paginaInicial > 1);
        bSiguiente.setEnabled(paginaInicial < totalPaginas);
    }

    //metodo de los filtros 
    public void aplicarFiltros() {
        // Normalizar el texto del nombre
        String nombre = tNombre.getText().trim().replaceAll("\\s+", " ");

        // Si el campo nombre está vacío después de normalizar, establecerlo como null
        nombre = nombre.isEmpty() ? null : nombre;

        String marca = (cMarca.getSelectedItem() != null && cMarca.getSelectedItem().equals("Todas"))
                ? null : (String) cMarca.getSelectedItem();

        // Determinar el sexo seleccionado (M/F)
        char sexo = '?';
        if (rbChico.isSelected()) {
            sexo = 'M';
        } else if (rbChica.isSelected()) {
            sexo = 'F';
        }

        String modelo = null;
        Integer anio = null;

        if (p2.isVisible()) { // Solo si p2 está visible se consideran estos filtros
            modelo = (cModelo.getSelectedItem() != null && cModelo.getSelectedItem().equals("Todos"))
                    ? null : (String) cModelo.getSelectedItem();

            // Validación para el año
            if (!tMatriculacion.getText().isEmpty()) {
                try {
                    anio = Integer.parseInt(tMatriculacion.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Salir del método si la entrada del año no es válida
                }
            }
        }

        // Llamamos al método para mostrar los datos con los filtros aplicados
        rc.mostrarDatos(modeloTabla, 1, nombre, marca, modelo, anio, sexo); // Forzamos a página 1

        calcularTotalPaginas();
        actualizarEstadoBotones();
    }

    // Método para bloquear el pegado de texto no válido
    private void bloquearPegado(JTextField campo, String regex, String mensajeError) {
        campo.setTransferHandler(new TransferHandler() {
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    String textoPegado = (String) t.getTransferData(DataFlavor.stringFlavor);

                    if (textoPegado != null && !textoPegado.matches(regex)) {
                        JOptionPane.showMessageDialog(null, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return super.importData(comp, t);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        });
    }

    // Método para eliminar la fila seleccionada en la tabla
    public void eliminarFilaSeleccionada() {
        int filaSeleccionada = tb.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                // Ajustar el índice si hay un TableRowSorter
                int filaModelo = tb.convertRowIndexToModel(filaSeleccionada);

                // Asegúrate de que los índices sean correctos
                String matricula = null;
                String dni = null;

                try {
                    matricula = modeloTabla.getValueAt(filaModelo, 3).toString(); // Índice 3 para matrícula
                    dni = modeloTabla.getValueAt(filaModelo, 2).toString(); // Índice 2 para DNI
                } catch (NullPointerException | ClassCastException e) {
                    JOptionPane.showMessageDialog(null, "Error al obtener datos de la fila seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
                    System.err.println("Error al obtener valores de la fila: " + e.getMessage());
                    e.printStackTrace();
                    return; // Salir si no se pueden obtener los datos
                }

                // Depuración de los valores obtenidos
                System.out.println("Fila seleccionada (modelo): " + filaModelo);
                System.out.println("Matrícula obtenida: " + matricula);
                System.out.println("DNI obtenido: " + dni);

                // Eliminar el registro
                boolean resultado = rc.eliminarRegistroVehiculoYPersona(matricula, dni);
                System.out.println("Resultado de la eliminación: " + resultado);

                if (resultado) {
                    modeloTabla.removeRow(filaModelo); // Eliminar del modelo
                    JOptionPane.showMessageDialog(null, "El vehículo, su registro y la persona se eliminaron correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "Hubo un problema al eliminar el vehículo, el registro o la persona.");
                }
            } catch (Exception ex) {
                // Mostrar mensaje de error en la interfaz
                JOptionPane.showMessageDialog(null, "Error al intentar eliminar la fila: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                // Imprimir el mensaje de error y el stack trace en la consola
                System.err.println("Error al intentar eliminar la fila:");
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona una fila para eliminar.");
        }
    }

    //contar las paginas con los datos que hay
    private void calcularTotalPaginas() {
        String nombre = tNombre.getText().isEmpty() ? null : tNombre.getText();
        String marca = cMarca.getSelectedItem() != null && cMarca.getSelectedItem().equals("Todas")
                ? null : (String) cMarca.getSelectedItem();
        String modelo = cModelo.getSelectedItem() != null && cModelo.getSelectedItem().equals("Todos")
                ? null : (String) cModelo.getSelectedItem();

        Integer anio = null;
        if (!tMatriculacion.getText().isEmpty()) {
            try {
                anio = Integer.parseInt(tMatriculacion.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        char sexo = '?';
        if (rbChico.isSelected()) {
            sexo = 'M';
        } else if (rbChica.isSelected()) {
            sexo = 'F';
        }

        int totalRegistros = rc.obtenerTotalRegistros(nombre, marca, modelo, anio, sexo); // Obtiene el total de registros según los filtros
        totalPaginas = (int) Math.ceil((double) totalRegistros / limite); // Calcula el total de páginas
    }

}
