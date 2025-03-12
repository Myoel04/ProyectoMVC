/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.PersonaDAO;
import DAO.RegistroDAO;
import DAO.VehiculoDAO;
import MODEL.datosPV;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yosoy
 */
public class registroControlador {

    private RegistroDAO rdao;
    private PersonaDAO pdao;
    private VehiculoDAO vdao;
    private JTable tabla;

    // Constructor que inicializa los DAOs proporcionados
    public registroControlador(VehiculoDAO vdao, RegistroDAO rdao, PersonaDAO pdao) {
        this.vdao = vdao;
        this.rdao = rdao;
        this.pdao = pdao;
    }

    // Constructor por defecto, inicializa las instancias DAO necesarias
    public registroControlador() {
        this.rdao = new RegistroDAO();
        this.pdao = new PersonaDAO();
        this.vdao = new VehiculoDAO();
    }

    // Setter para la tabla de datos
    public void setTabla(JTable tabla) {
        this.tabla = tabla;
    }

    // Método para mostrar datos en la tabla usando filtros

    public void mostrarDatos(DefaultTableModel modeloTabla, int paginaInicial, String nombre, String marca, String modeloFiltro, Integer anio, char sexo) {
        if (modeloTabla == null) { // Verifica que la talba se inicialice correctamente
            JOptionPane.showMessageDialog(null, "El modelo de la tabla no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Requiere al menos un filtro para realizar la búsqueda
        if (nombre == null && marca == null && modeloFiltro == null && anio == null && sexo == '?') {
            JOptionPane.showMessageDialog(null, "Debe aplicar al menos un filtro", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtiene datos filtrados y verifica si hay resultados
        rdao.obtenerDatos(modeloTabla, paginaInicial, nombre, marca, modeloFiltro, anio, sexo);
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No se encontraron registros con los filtros aplicados", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Método para agregar un nuevo registro
    public void agregarRegistro(int idPersona, int idVehiculo, Date fechaInicio, Date fechaFin) {
        // Validaciones básicas de ID y fecha
        if (idPersona <= 0 || idVehiculo <= 0) {
            JOptionPane.showMessageDialog(null, "IDs de persona y vehículo deben ser válidos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (fechaInicio == null) {
            JOptionPane.showMessageDialog(null, "La fecha de inicio es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Llama al DAO para agregar el registro, muestra mensaje de éxito y actualiza propietarios
        rdao.agregarRegistro(idPersona, idVehiculo, fechaInicio, fechaFin);
        JOptionPane.showMessageDialog(null, "Registro agregado con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
        vdao.actualizarNumPropietarios(); // Actualiza número de propietarios del vehículo
    }

    // Método para mostrar el historial de un vehículo en el modelo de tabla
    public void mostrarHistorialPorVehiculo(DefaultTableModel modeloTabla, int idVehiculo) {
        if (modeloTabla == null) {
            JOptionPane.showMessageDialog(null, "El modelo de la tabla no puede estar vacío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Limpiar la tabla antes de agregar los nuevos datos
        modeloTabla.setRowCount(0);

        // Llama al DAO para obtener el historial y agrega cada registro al modelo de la tabla
        List<datosPV> historial = rdao.obtenerHistorialPorVehiculo(idVehiculo);
        for (datosPV registro : historial) {
            modeloTabla.addRow(new Object[]{
                registro.getNombre(),
                registro.getDni(),
                registro.getMatricula(),
                registro.getAnio(),
                registro.getMarca(),
                registro.getModelo(),
                registro.getFechaInicio(),
                registro.getFechaFin()
            });
        }

        // Mensaje informativo si no hay historial
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "No se encontró historial para el vehículo especificado", "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

 public boolean eliminarRegistroVehiculoYPersona(String matricula, String dni) {
    if (matricula == null || matricula.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Matrícula no válida para eliminación.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    if (dni == null || dni.isEmpty()) {
        JOptionPane.showMessageDialog(null, "DNI no válido para eliminación.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    try {
        // Elimina el vehículo
        boolean eliminadoVehiculo = vdao.eliminarVehiculo(matricula);
        if (!eliminadoVehiculo) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el vehículo con matrícula: " + matricula, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Actualiza el número de propietarios tras la eliminación
        vdao.actualizarNumPropietarios();

        // Elimina la persona si no tiene otros registros asociados
        boolean eliminadoPersona = pdao.eliminarPersonaSiNoTieneRegistro(dni);
        if (!eliminadoPersona) {
            JOptionPane.showMessageDialog(null, "Error al eliminar la persona con DNI: " + dni + ". Es posible que tenga otros vehículos asociados.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // Todo se eliminó correctamente
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error inesperado durante la eliminación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Imprime el stack trace para depuración
        return false;
    }
}

    // Método para contar el número total de registros según los filtros
public int obtenerTotalRegistros(String nombre, String marca, String modelo, Integer anio, char sexo) {
    try {
        // Llama al método correspondiente del DAO para contar los registros
        return rdao.contarRegistros(nombre, marca, modelo, anio, sexo);
    } catch (Exception ex) {
        // Manejo de errores
        JOptionPane.showMessageDialog(null, "Error al contar los registros: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
        return 0; // Devuelve 0 en caso de error
    }
}

    
}