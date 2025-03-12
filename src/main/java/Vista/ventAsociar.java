package Vista;

import DAO.PersonaDAO;
import DAO.RegistroDAO;
import DAO.VehiculoDAO;
import DTO.PersonaDTO;
import DTO.VehiculoDTO;
import MODEL.datosPV;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ventAsociar extends JFrame {

    private DefaultTableModel mtPersona;
    private DefaultTableModel mtVehiculo;
    private JTable tabPersona;
    private JTable tabVehiculo;
    private JButton bAsociar;
    private JButton bVerHistorial;
    private JCheckBox conDueno;
    private JCheckBox sinDueno;

    private RegistroDAO rdao;
    private Connection conex;

    public ventAsociar(PersonaDAO pdao, VehiculoDAO vdao, Connection conex) {
        setTitle("Asociar Vehículos");
        setSize(1000, 500);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.rdao = new RegistroDAO();
        this.conex = conex;

        JPanel ventanaPrincipal = new JPanel(new BorderLayout());
        JPanel pv1 = new JPanel(new GridLayout(1, 2)); // Panel para las tablas y checkboxes

        // Configuración de tabla de personas
        configurarTablaPersona(pdao);
        pv1.add(new JScrollPane(tabPersona));

        // Checkboxes de filtrado
        JPanel panelCheckBoxes = new JPanel(new FlowLayout());
        conDueno = new JCheckBox("Con dueño", true);
        sinDueno = new JCheckBox("Sin dueño");
        panelCheckBoxes.add(conDueno);
        panelCheckBoxes.add(sinDueno);
        pv1.add(panelCheckBoxes);

        // Configuración de tabla de vehículos
        configurarTablaVehiculo();
        pv1.add(new JScrollPane(tabVehiculo));

        ventanaPrincipal.add(pv1, BorderLayout.CENTER);

        // Panel de botones
        JPanel pv2 = new JPanel(new GridBagLayout());
        bAsociar = new JButton("Asociar");
        bVerHistorial = new JButton("Ver Historial");
        pv2.add(bAsociar);
        pv2.add(bVerHistorial);

        ventanaPrincipal.add(pv2, BorderLayout.SOUTH);
        add(ventanaPrincipal, BorderLayout.CENTER);

        // Cargar datos iniciales en tablas
        cargarDatos(pdao, vdao);

        // Listeners de los checkboxes
        conDueno.addItemListener(e -> {
            if (conDueno.isSelected()) {
                sinDueno.setSelected(false);
                cargarConDueno(vdao);
            } else if (!sinDueno.isSelected()) {
                try {
                    cargarVehiculos(vdao);
                } catch (SQLException ex) {
                    Logger.getLogger(ventAsociar.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error al cargar los vehículos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        sinDueno.addItemListener(e -> {
            if (sinDueno.isSelected()) {
                conDueno.setSelected(false);
                cargarSinDueno(vdao);
            } else if (!conDueno.isSelected()) {
                try {
                    cargarVehiculos(vdao);
                } catch (SQLException ex) {
                    Logger.getLogger(ventAsociar.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(this, "Error al cargar los vehículos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        bAsociar.addActionListener(e -> asociarPersonaVehiculo(pdao));
        bVerHistorial.addActionListener(this::verHistorialVehiculo);
    }

  private void configurarTablaPersona(PersonaDAO pdao) {
    String[] columnasPersona = {"Nombre", "DNI"};
    mtPersona = new DefaultTableModel(null, columnasPersona) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Todas las celdas no serán editables
        }
    };
    tabPersona = new JTable(mtPersona);
    tabPersona.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}

private void configurarTablaVehiculo() {
    String[] columnasVehiculo = {"Matrícula", "Año", "Marca", "Modelo", "Inicio", "Final"};
    mtVehiculo = new DefaultTableModel(null, columnasVehiculo) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Todas las celdas no serán editables
        }
    };
    tabVehiculo = new JTable(mtVehiculo);
    tabVehiculo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
}

    private void cargarDatos(PersonaDAO pdao, VehiculoDAO vdao) {
        try {
            cargarPersonas(pdao);
            cargarVehiculos(vdao);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los datos. Por favor, intente nuevamente.",
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ventAsociar.class.getName()).log(Level.SEVERE, "Error al cargar los datos", ex);
        }
    }

    private void cargarPersonas(PersonaDAO pdao) throws SQLException {
        mtPersona.setRowCount(0); // Limpia la tabla antes de cargar
        List<PersonaDTO> personas = pdao.obtenerPersonas();
        if (personas != null && !personas.isEmpty()) {
            for (PersonaDTO persona : personas) {
                mtPersona.addRow(new Object[]{persona.getNombre(), persona.getDni()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron personas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarVehiculos(VehiculoDAO vdao) throws SQLException {
        mtVehiculo.setRowCount(0); // Limpia la tabla antes de cargar
        List<VehiculoDTO> vehiculos = vdao.obtenerVehiculos();
        if (vehiculos != null && !vehiculos.isEmpty()) {
            for (VehiculoDTO vehiculo : vehiculos) {
                mtVehiculo.addRow(new Object[]{
                        vehiculo.getMatricula(), vehiculo.getAnio(), vehiculo.getMarca(),
                        vehiculo.getModelo(), vehiculo.getFechaInicio(), vehiculo.getFechaFin()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron vehículos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarConDueno(VehiculoDAO vdao) {
        mtVehiculo.setRowCount(0); // Limpia la tabla antes de cargar
        List<VehiculoDTO> vehiculosConDueno = vdao.obtenerVehiculosConDueno();
        if (vehiculosConDueno != null && !vehiculosConDueno.isEmpty()) {
            for (VehiculoDTO vehiculo : vehiculosConDueno) {
                mtVehiculo.addRow(new Object[]{
                        vehiculo.getMatricula(), vehiculo.getAnio(), vehiculo.getMarca(),
                        vehiculo.getModelo(), vehiculo.getFechaInicio(), vehiculo.getFechaFin()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron vehículos con dueño.");
        }
    }

    private void cargarSinDueno(VehiculoDAO vdao) {
        mtVehiculo.setRowCount(0); // Limpia la tabla antes de cargar
        List<VehiculoDTO> vehiculosSinDueno = vdao.obtenerVehiculosSinDueno();
        if (vehiculosSinDueno != null && !vehiculosSinDueno.isEmpty()) {
            for (VehiculoDTO vehiculo : vehiculosSinDueno) {
                mtVehiculo.addRow(new Object[]{
                        vehiculo.getMatricula(), vehiculo.getAnio(), vehiculo.getMarca(),
                        vehiculo.getModelo(), vehiculo.getFechaInicio(), vehiculo.getFechaFin()
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se encontraron vehículos sin dueño.");
        }
    }

    private void asociarPersonaVehiculo(PersonaDAO pdao) {
        int filaPersona = tabPersona.getSelectedRow();
        int filaVehiculo = tabVehiculo.getSelectedRow();

        if (filaPersona == -1 || filaVehiculo == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona una persona y un vehículo.",
                    "Error de asociación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dniPersona = (String) mtPersona.getValueAt(filaPersona, 1);
        String matriculaVehiculo = (String) mtVehiculo.getValueAt(filaVehiculo, 0);

        VehiculoDAO vehiculoDAO = new VehiculoDAO(conex);
        int idVehiculo = vehiculoDAO.obtenerIdVehiculo(matriculaVehiculo);
        String propietarioAnterior = pdao.obtenerPropietarioVehiculo(idVehiculo);
        if (propietarioAnterior != null) {
            pdao.actualizarFechaFin(propietarioAnterior, idVehiculo, new java.sql.Date(System.currentTimeMillis()));
        }
        boolean exito = pdao.asociarPersonaConVehiculo(dniPersona, matriculaVehiculo, new java.sql.Date(System.currentTimeMillis()));
        if (exito) {
            JOptionPane.showMessageDialog(this, "Asociación realizada con éxito.", "Asociación completada", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error al asociar persona con vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verHistorialVehiculo(ActionEvent e) {
        int filaVehiculo = tabVehiculo.getSelectedRow();
        if (filaVehiculo == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecciona un vehículo para ver el historial.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String matriculaVehiculo = (String) mtVehiculo.getValueAt(filaVehiculo, 0);
        VehiculoDAO vehiculoDAO = new VehiculoDAO(conex);
        int idVehiculo = vehiculoDAO.obtenerIdVehiculo(matriculaVehiculo);

        List<datosPV> historial = rdao.obtenerHistorialPorVehiculo(idVehiculo);
        mostrarHistorial(historial);
    }

    private void mostrarHistorial(List<datosPV> historial) {
        JFrame historialFrame = new JFrame("Historial de Propietarios");
        DefaultTableModel modeloHistorial = new DefaultTableModel(
                new String[]{"Nombre", "DNI", "Matrícula", "Año", "Marca", "Modelo", "Fecha Inicio", "Fecha Fin"}, 0);
        JTable tablaHistorial = new JTable(modeloHistorial);

        for (datosPV registro : historial) {
            modeloHistorial.addRow(new Object[]{
                    registro.getNombre(), registro.getDni(), registro.getMatricula(),
                    registro.getAnio(), registro.getMarca(), registro.getModelo(),
                    registro.getFechaInicio(), registro.getFechaFin()
            });
        }

        historialFrame.add(new JScrollPane(tablaHistorial));
        historialFrame.setSize(800, 400);
        historialFrame.setVisible(true);
    }
    
    public void recargarTablaPersonas(PersonaDAO pdao) {
    try {
        mtPersona.setRowCount(0); // Limpia los datos actuales de la tabla
        cargarPersonas(pdao);     // Carga los datos más recientes desde la base de datos
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error al recargar la tabla de personas.", "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
}

    
}
