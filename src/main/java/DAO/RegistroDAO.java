package DAO;

import MODEL.datosPV;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class RegistroDAO {

    conexionBD cbd = new conexionBD();
    Connection conex = cbd.conectar();

    public void obtenerDatos(DefaultTableModel modeloTabla, int paginaInicial, String nombre, String marca, String modeloFiltro, Integer anio, char sexo) {
        modeloTabla.setRowCount(0);
        int limite = 10;  
        int offset = (paginaInicial - 1) * limite;

        String query = "SELECT p.nombre, p.dni, p.sexo, v.matricula, v.anio, v.marca, v.modelo, " +
                       "r.fecha_inicio, r.fecha_fin, v.numPropietarios " +
                       "FROM registro r " +
                       "JOIN personas p ON r.idPersona = p.idPersona " +
                       "JOIN vehiculos v ON r.idVehiculo = v.idVehiculo " +
                       "WHERE 1=1"; 

        if (nombre != null && !nombre.isEmpty()) {  
            query += " AND p.nombre LIKE ?";
        }
        if (marca != null && !marca.equals("Todas")) { 
            query += " AND v.marca = ?";
        }
        if (modeloFiltro != null && !modeloFiltro.equals("Todos")) { 
            query += " AND v.modelo = ?";
        }
        if (anio != null) {  
            query += " AND v.anio = ?";
        }
        if (sexo != '?') {  
            query += " AND p.sexo = ?";
        }
        query += " ORDER BY p.nombre LIMIT ? OFFSET ?";

        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            int index = 1;  
            if (nombre != null && !nombre.isEmpty()) {
                ps.setString(index++, "%" + nombre + "%");
            }
            if (marca != null && !marca.equals("Todas")) {
                ps.setString(index++, marca);
            }
            if (modeloFiltro != null && !modeloFiltro.equals("Todos")) {
                ps.setString(index++, modeloFiltro);
            }
            if (anio != null) {
                ps.setInt(index++, anio);
            }
            if (sexo != '?') {  
                ps.setString(index++, String.valueOf(sexo));
            }
            ps.setInt(index++, limite);
            ps.setInt(index++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    JOptionPane.showMessageDialog(null, "No se encontraron registros con los filtros aplicados", "Información", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    while (rs.next()) {
                        Object[] fila = new Object[10];
                        fila[0] = rs.getString("nombre");
                        fila[1] = rs.getString("sexo");
                        fila[2] = rs.getString("dni");
                        fila[3] = rs.getString("matricula");
                        fila[4] = rs.getInt("anio");
                        fila[5] = rs.getString("marca");
                        fila[6] = rs.getString("modelo");
                        fila[7] = rs.getDate("fecha_inicio");
                        fila[8] = rs.getDate("fecha_fin");
                        fila[9] = rs.getInt("numPropietarios");
                        modeloTabla.addRow(fila);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se puede obtener los datos debido a un error en la base de datos", "Error de conexión", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void agregarRegistro(int idPersona, int idVehiculo, Date fechaInicio, Date fechaFin) {
        // Primero actualiza la fecha fin del dueño anterior si existe
        actualizarFechaFinAlAgregar(idVehiculo, fechaFin);

        // Inserta el nuevo registro
        String query = "INSERT INTO registro (idPersona, idVehiculo, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            ps.setInt(1, idPersona);
            ps.setInt(2, idVehiculo);
            ps.setDate(3, new java.sql.Date(fechaInicio.getTime()));
            if (fechaFin != null) {
                ps.setDate(4, new java.sql.Date(fechaFin.getTime()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo agregar el registro", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<datosPV> obtenerHistorialPorVehiculo(int idVehiculo) {
        List<datosPV> historial = new ArrayList<>();
        String query = "SELECT p.idPersona, p.nombre, p.dni, v.idVehiculo, v.matricula, v.anio, v.marca, v.modelo, " +
                       "r.fecha_inicio, r.fecha_fin " +
                       "FROM registro r " +
                       "JOIN personas p ON r.idPersona = p.idPersona " +
                       "JOIN vehiculos v ON r.idVehiculo = v.idVehiculo " +
                       "WHERE v.idVehiculo = ? ORDER BY r.fecha_inicio DESC";

        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            ps.setInt(1, idVehiculo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    datosPV registro = new datosPV(
                        rs.getInt("idPersona"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getInt("idVehiculo"),
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                    );
                    historial.add(registro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "No se pudo obtener el historial de propietarios", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return historial;
    }

    public boolean eliminarRegistro(String matricula, String dni) {
        String query = "DELETE FROM registro WHERE idVehiculo = (SELECT idVehiculo FROM vehiculos WHERE matricula = ?) " +
                       "AND idPersona = (SELECT idPersona FROM personas WHERE dni = ?)";
        try (Connection con = cbd.conectar(); PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, matricula);
            ps.setString(2, dni);
            int filaEliminar = ps.executeUpdate();
            return filaEliminar > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
//metodo para contar los registros 
    public int contarRegistros(String nombre, String marca, String modelo, Integer anio, char sexo) {
        int total = 0;
        String query = "SELECT COUNT(*) AS total " +
                       "FROM registro r " +
                       "JOIN personas p ON r.idPersona = p.idPersona " +
                       "JOIN vehiculos v ON r.idVehiculo = v.idVehiculo " +
                       "WHERE 1=1";
        if (nombre != null && !nombre.isEmpty()) {
            query += " AND TRIM(p.nombre) LIKE ?";
        }
        if (marca != null && !marca.equals("Todas")) {
            query += " AND v.marca = ?";
        }
        if (modelo != null && !modelo.equals("Todos")) {
            query += " AND v.modelo = ?";
        }
        if (anio != null) {
            query += " AND v.anio = ?";
        }
        if (sexo != '?') {
            query += " AND p.sexo = ?";
        }

        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            int index = 1;
            if (nombre != null && !nombre.isEmpty()) {
                ps.setString(index++, "%" + nombre.trim().replaceAll("\\s+", " ") + "%");
            }
            if (marca != null && !marca.equals("Todas")) {
                ps.setString(index++, marca);
            }
            if (modelo != null && !modelo.equals("Todos")) {
                ps.setString(index++, modelo);
            }
            if (anio != null) {
                ps.setInt(index++, anio);
            }
            if (sexo != '?') {
                ps.setString(index++, String.valueOf(sexo));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al contar registros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
    public void actualizarFechaFinAlAgregar(int idVehiculo, Date fechaFin) {
    String query = "UPDATE registro SET fecha_fin = ? " +
                   "WHERE idVehiculo = ? AND fecha_fin IS NULL";

    try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
        ps.setDate(1, new java.sql.Date(fechaFin.getTime()));
        ps.setInt(2, idVehiculo);
        int affectedRows = ps.executeUpdate();
        if (affectedRows == 0) {
            System.out.println("No se encontró un registro activo para actualizar la fecha fin.");
        } else {
            System.out.println("Fecha fin actualizada correctamente para el registro anterior.");
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar la fecha fin: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
}

}
