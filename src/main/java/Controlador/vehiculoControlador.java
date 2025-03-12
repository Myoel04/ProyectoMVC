/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.VehiculoDAO;
import DTO.VehiculoDTO;
import MODEL.datosPV;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yosoy
 */
public class vehiculoControlador {

    private VehiculoDAO vehiculoDAO;
    private JComboBox<String> cbMarca;
    private JComboBox<String> cbModelo;

    public vehiculoControlador(JComboBox<String> cbMarca, JComboBox<String> cbModelo) {
        if (cbMarca == null || cbModelo == null) {
            // Mostrar un mensaje de advertencia si alguno de los JComboBox es null
            JOptionPane.showMessageDialog(null, "Uno o ambos JComboBox son nulos, valor por defecto.",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            // valor por defecto
            this.cbMarca = new JComboBox<>();
            this.cbModelo = new JComboBox<>();
        } else {
            this.cbMarca = cbMarca;
            this.cbModelo = cbModelo;
        }
        this.vehiculoDAO = new VehiculoDAO();
    }

  public vehiculoControlador(Connection conex, JComboBox<String> cbMarca, JComboBox<String> cbModelo) {
    // Verificar si alguno de los parámetros es nulo
    if (cbMarca == null || cbModelo == null) {
        JOptionPane.showMessageDialog(null, "Uno o ambos JComboBox son nulos, se asignarán valores predeterminados.",
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
        
        // Asignar valores predeterminados a los JComboBox
        cbMarca = new JComboBox<>();
        cbModelo = new JComboBox<>();
    }

    if (conex == null) {
        JOptionPane.showMessageDialog(null, "La conexión no puede ser nula. Se utilizará una conexión predeterminada.",
                                      "Advertencia", JOptionPane.WARNING_MESSAGE);
        conex = obtenerConexionPredeterminada();  
    }

    this.vehiculoDAO = new VehiculoDAO(conex);
    this.cbMarca = cbMarca;
    this.cbModelo = cbModelo;
}

// Método ficticio para obtener una conexión predeterminada
private Connection obtenerConexionPredeterminada() {
    try {
        // Puedes retornar una conexión de prueba o establecer una conexión predeterminada si es necesario
        return DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"); // Ejemplo usando H2 (base de datos en memoria)
    } catch (SQLException e) {
        e.printStackTrace();
        return null;  // En caso de error, retornar null
    }
}


    // Método para mostrar todos los vehículos en una tabla
    public void mostrarVehiculos(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de añadir datos
        List<VehiculoDTO> vehiculos = vehiculoDAO.obtenerVehiculos();

        // Agregar cada vehículo al modelo de tabla
        for (VehiculoDTO vehiculo : vehiculos) {
            modeloTabla.addRow(new Object[]{
                vehiculo.getMatricula(),
                vehiculo.getAnio(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                vehiculo.getFechaInicio(),
                vehiculo.getFechaFin()
            });
        }
    }

    // Método para agregar un nuevo vehículo a la base de datos
    public void agregarVehiculo(String matricula, int anio, String marca, String modelo) {
        // Validación de datos de entrada
        if (matricula == null || matricula.isEmpty() || marca == null || modelo == null || anio <= 0) {
            JOptionPane.showMessageDialog(null, "Datos de vehículo no válidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crea un objeto VehiculoDTO con los datos y lo agrega a la base de datos
        VehiculoDTO vdto = new VehiculoDTO(matricula, anio, marca, modelo, null, null);
        vehiculoDAO.agregarVehiculo(vdto);
        JOptionPane.showMessageDialog(null, "Vehículo agregado con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);

        // Actualiza los JComboBox de marca y modelo si la nueva marca o modelo no está en ellos
        actualizarComboboxes(marca, modelo);

        // Actualiza el número de propietarios en el DAO de vehículos
        vehiculoDAO.actualizarNumPropietarios();
    }

    // Método para eliminar un vehículo por su matrícula
    public void eliminarVehiculo(String matricula) {
        // Validación de la matrícula
        if (matricula == null || matricula.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La matrícula no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Elimina el vehículo y muestra mensajes de confirmación o error según el resultado
        boolean eliminado = vehiculoDAO.eliminarVehiculo(matricula);
        if (eliminado) {
            JOptionPane.showMessageDialog(null, "Vehículo eliminado", "Información", JOptionPane.INFORMATION_MESSAGE);

            // Actualiza el número de propietarios después de la eliminación
            vehiculoDAO.actualizarNumPropietarios();
        } else {
            JOptionPane.showMessageDialog(null, "No se pudo eliminar el vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para mostrar vehículos con filtros aplicados en la tabla
    public void mostrarVehiculosConFiltros(DefaultTableModel modeloTabla, String nombre, String marca, String modelo, Integer anio, int pagina, int limit) {
        modeloTabla.setRowCount(0);  // Limpiar la tabla

        // Obtiene vehículos filtrados y los muestra en la tabla
        List<datosPV> vehiculos = vehiculoDAO.obtenerVehiculosConFiltros(nombre, marca, modelo, anio, pagina, limit, modeloTabla);

        // Si no hay resultados, muestra mensaje informativo
        if (vehiculos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No se encontraron vehículos con los filtros aplicados", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para obtener el ID de un vehículo mediante su matrícula
    public Integer obtenerIdVehiculo(String matricula) {
        // Validación de la matrícula
        if (matricula == null || matricula.isEmpty()) {
            JOptionPane.showMessageDialog(null, "La matrícula no puede estar vacía", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        // Retorna el ID del vehículo o null si no se encuentra
        return vehiculoDAO.obtenerIdVehiculo(matricula);
    }

    // Método para actualizar los JComboBox de marca y modelo
    private void actualizarComboboxes(String marca, String modelo) {
        // Verifica y añade la marca al JComboBox si aún no está presente
        if (!elementoEnComboBox(cbMarca, marca)) {
            cbMarca.addItem(marca);
        }

        // Verifica y añade el modelo al JComboBox si aún no está presente
        if (!elementoEnComboBox(cbModelo, modelo)) {
            cbModelo.addItem(modelo);
        }
    }

    // Método auxiliar para verificar si un elemento ya está en el JComboBox
    private boolean elementoEnComboBox(JComboBox<String> comboBox, String elemento) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (comboBox.getItemAt(i).equals(elemento)) {
                return true; // El elemento ya está presente
            }
        }
        return false; // El elemento no está en el JComboBox
    }
}
