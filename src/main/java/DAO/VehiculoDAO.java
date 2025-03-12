/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.VehiculoDTO;
import MODEL.datosPV;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.sql.Types.NULL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yosoy
 */
public class VehiculoDAO {
    private conexionBD cbd = new conexionBD();  // Instancia de la clase conexionBD
    private Connection conex;  // Conexión a la base de datos

    // Constructor que recibe una conexión existente
  public VehiculoDAO(Connection conex) {
    if (conex == null) {
        System.err.println("Error: La conexión no puede ser nula");
        this.conex = null;  
    } else {
        this.conex = conex;
    }
}

    // Constructor sin parámetros que inicializa `conex` llamando a `conexionBD`
   public VehiculoDAO() {
    this.conex = cbd.conectar();  // Llama a conectar() para obtener la conexión
    if (this.conex == null) {
        System.err.println("No se pudo establecer la conexión con la base de datos");
    }
}

    // Método para obtener el ID del vehículo basado en la matrícula
public Integer obtenerIdVehiculo(String matricula) {
    if (conex == null) {
        System.err.println("Error: La conexión de BBDD no está inicializada");
        return null; 
    }
    
    String query = "SELECT idVehiculo FROM vehiculos WHERE matricula = ?";

    try (PreparedStatement ps = conex.prepareStatement(query)) {
        ps.setString(1, matricula);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("idVehiculo");  // Retorna el ID del vehículo si existe
            }
        }
    } catch (SQLException e) {
        // Manejo de la excepción: registrar el error
        e.printStackTrace();
        System.out.println("Error al obtener el id del vehiculo: " + e);
    }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
    
    return null;  // Retorna null si no encuentra el vehículo o si hay un error
}


    // Método para obtener todos los vehículos y sus datos
    public List<VehiculoDTO> obtenerVehiculos() {
        List<VehiculoDTO> vehiculos = new ArrayList<>();
        String query = "SELECT v.matricula, v.anio, v.marca, v.modelo, r.fecha_inicio, r.fecha_fin " +
                       "FROM vehiculos v " +
                       "JOIN registro r ON v.idVehiculo = r.idVehiculo";

        try (PreparedStatement ps = conex.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VehiculoDTO vehiculo = new VehiculoDTO(
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"));
                vehiculos.add(vehiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return vehiculos;
    }

    // Método para agregar un vehículo nuevo a la base de datos
    public void agregarVehiculo(VehiculoDTO vehiculo) {
        String query = "INSERT INTO vehiculos(matricula, anio, marca, modelo) VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps = conex.prepareStatement(query)) {
            ps.setString(1, vehiculo.getMatricula());
            ps.setInt(2, vehiculo.getAnio());
            ps.setString(3, vehiculo.getMarca());
            ps.setString(4, vehiculo.getModelo());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
    }

    // Método para eliminar un vehículo de la base de datos usando su matrícula
   public boolean eliminarVehiculo(String matricula) {
    if (matricula == null || matricula.isEmpty()) {
        System.err.println("Matrícula inválida: " + matricula);
        return false;
    }

    String query = "DELETE FROM vehiculos WHERE matricula = ?";
    try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
        ps.setString(1, matricula);
        int filasEliminadas = ps.executeUpdate();
        System.out.println("Filas eliminadas en vehiculos: " + filasEliminadas);
        return filasEliminadas > 0;
    } catch (SQLException e) {
        System.err.println("Error al eliminar vehículo con matrícula: " + matricula + ". Error: " + e.getMessage());
        e.printStackTrace();
        
    }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }return false;
}


    // Método para obtener los modelos de vehículos por una marca específica
    public List<String> obtenerVehiculosMarca(String marca) {
        List<String> modelos = new ArrayList<>();
        String query = "SELECT DISTINCT modelo FROM vehiculos WHERE marca = ?";

        try (PreparedStatement ps = conex.prepareStatement(query)) {
            ps.setString(1, marca);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    modelos.add(rs.getString("modelo"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return modelos;
    }

    // Método para obtener vehículos con filtros y agregar resultados a un modelo de tabla
    public List<datosPV> obtenerVehiculosConFiltros(String nombre, String marca, String modelo, Integer anio, int pagina, int limit, DefaultTableModel modeloTabla) {
        List<datosPV> vehiculos = new ArrayList<>();
        int offset = (pagina - 1) * limit;

        String sql = "SELECT p.idPersona, p.nombre, p.dni, v.matricula, v.anio, v.marca, v.modelo, r.fecha_inicio, r.fecha_fin " +
                     "FROM personas p " +
                     "JOIN registro r ON p.idPersona = r.idPersona " +
                     "JOIN vehiculos v ON r.idVehiculo = v.idVehiculo " +
                     "WHERE 1=1";

        if (nombre != null && !nombre.isEmpty()) sql += " AND LOWER(p.nombre) LIKE LOWER(?)";
        if (marca != null && !marca.equals("Todas")) sql += " AND v.marca = ?";
        if (modelo != null && !modelo.equals("Todos")) sql += " AND v.modelo = ?";
        if (anio != null) sql += " AND v.anio = ?";
        sql += " LIMIT ? OFFSET ?";

        try (PreparedStatement stmt = conex.prepareStatement(sql)) {
            int index = 1;
            if (nombre != null && !nombre.isEmpty()) stmt.setString(index++, "%" + nombre + "%");
            if (marca != null && !marca.equals("Todas")) stmt.setString(index++, marca);
            if (modelo != null && !modelo.equals("Todos")) stmt.setString(index++, modelo);
            if (anio != null) stmt.setInt(index++, anio);
            stmt.setInt(index++, limit);
            stmt.setInt(index++, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    datosPV vp = new datosPV(
                        rs.getInt("idPersona"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        0,
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                    );
                    vehiculos.add(vp);

                    Object[] fila = new Object[] {
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin")
                    };
                    modeloTabla.addRow(fila);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return vehiculos;
    }

    // Método para obtener vehículos sin dueño
    public List<VehiculoDTO> obtenerVehiculosSinDueno() {
        List<VehiculoDTO> vehiculosSinDueno = new ArrayList<>();
        String query = "SELECT v.matricula, v.anio, v.marca, v.modelo " +
                       "FROM vehiculos v " +
                       "LEFT JOIN registro r ON v.idVehiculo = r.idVehiculo " +
                       "WHERE r.idVehiculo IS NULL";

        try (PreparedStatement ps = conex.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VehiculoDTO vehiculo = new VehiculoDTO(
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        null,
                        null);
                vehiculosSinDueno.add(vehiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return vehiculosSinDueno;
    }

    // Método para obtener vehículos con dueño
    public List<VehiculoDTO> obtenerVehiculosConDueno() {
        List<VehiculoDTO> vehiculosConDueno = new ArrayList<>();
        String query = "SELECT v.matricula, v.anio, v.marca, v.modelo, r.fecha_inicio, r.fecha_fin " +
                       "FROM vehiculos v " +
                       "JOIN registro r ON v.idVehiculo = r.idVehiculo";

        try (PreparedStatement ps = conex.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VehiculoDTO vehiculo = new VehiculoDTO(
                        rs.getString("matricula"),
                        rs.getInt("anio"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getDate("fecha_inicio"),
                        rs.getDate("fecha_fin"));
                vehiculosConDueno.add(vehiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return vehiculosConDueno;
    }

    // Método para actualizar el número de propietarios en la tabla de vehículos
    public void actualizarNumPropietarios() {
        String query = "UPDATE vehiculos v " +
                       "JOIN (" +
                       "    SELECT v.idVehiculo, COUNT(r.idRegistro) AS numPropietarios " +
                       "    FROM vehiculos v " +
                       "    LEFT JOIN registro r ON v.idVehiculo = r.idVehiculo " +
                       "    GROUP BY v.idVehiculo" +
                       ") AS propietariosCount ON v.idVehiculo = propietariosCount.idVehiculo " +
                       "SET v.numPropietarios = propietariosCount.numPropietarios";

        try (PreparedStatement stmt = conex.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
    }
}

