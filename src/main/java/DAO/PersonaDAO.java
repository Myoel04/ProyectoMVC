/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.PersonaDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Date;

/**
 *
 * @author yosoy
 */
public class PersonaDAO {

    private VehiculoDAO vdao;
    private final conexionBD cbd;
    private Connection conex;

    public PersonaDAO() {
        this.cbd = new conexionBD();
    }

    // Obtiene todas las personas almacenadas en la base de datos
    public List<PersonaDTO> obtenerPersonas() {
        List<PersonaDTO> personas = new ArrayList<>();
        String query = "SELECT nombre, dni, sexo FROM personas ORDER BY nombre";

        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            // Ejecuta la consulta y procesa el resultado
            while (rs.next()) {
                // Se crea un objeto PersonaDTO a partir de los resultados
                PersonaDTO persona = new PersonaDTO(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("sexo").charAt(0) // Convertimos el sexo a un char
                );
                personas.add(persona);
            }
        } catch (SQLException e) {

            System.err.println("Error SQL al obtener personas: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {

            System.err.println("Error inesperado al obtener personas: " + e.getMessage());
            e.printStackTrace();
        }

        return personas;  // Retorna la lista de personas, aunque si ocurrió un error, la lista podría estar vacía
    }

    // Verifica si ya existe una persona con el DNI proporcionado
    public boolean existeDni(String dni) {
        String sql = "SELECT COUNT(*) FROM personas WHERE dni = ?";
        try (PreparedStatement stmt = conex.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar el DNI: " + e.getMessage());
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }

        return false;  // Retorna false si no existe el DNI
    }

    // Agrega una nueva persona a la base de datos
    public boolean agregarPersona(PersonaDTO persona) {
        if (conex == null) {
            conex = cbd.conectar();  // Si la conexión es nula, crea una nueva conexión
        }
        String sql = "INSERT INTO personas (nombre, dni, sexo) VALUES (?, ?, ?)";  // Consulta SQL para insertar una persona

        try (PreparedStatement ps = conex.prepareStatement(sql)) {
            ps.setString(1, persona.getNombre());  // Establece el nombre de la persona
            ps.setString(2, persona.getDni());

            // Validamos que el sexo sea 'M' o 'F'
            char sexo = persona.getSexo();
            if (sexo == 'M' || sexo == 'F') {
                ps.setString(3, String.valueOf(sexo));  // lo guarda como String
            } else {
                ps.setString(3, "M");  // Si es inválido, asigna 'M' por defecto
                System.err.println("Sexo inválido. Asignando valor por defecto (M).");
            }

            ps.executeUpdate();  // Ejecuta la consulta
            return true;  // Si se ejecutó correctamente, retorna true
        } catch (SQLException e) {
            System.err.println("Error al guardar persona: " + e.getMessage());
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return false;  // Si ocurre algún error, retorna false
    }

    // Elimina una persona de la base de datos solo si no tiene registros asociados en otras tablas
    public boolean eliminarPersonaSiNoTieneRegistro(String dni) {
        if (dni == null || dni.isEmpty()) {
            System.err.println("DNI inválido: " + dni);
            return false;
        }

        // Imprime el valor de 'dni' para depuración
        System.out.println("DNI a eliminar: " + dni);

        String query = "SELECT COUNT(*) FROM registro WHERE idPersona = (SELECT idPersona FROM personas WHERE dni = ?)";
        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            // Establece el valor del parámetro
            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("Persona con DNI " + dni + " tiene registros asociados.");
                    return false;
                }
            }

            // Si no tiene registros asociados, procedemos a eliminar la persona
            String elimQuery = "DELETE FROM personas WHERE dni = ?";
            try (PreparedStatement psDelete = conex.prepareStatement(elimQuery)) {
                psDelete.setString(1, dni);  // Asigna el valor de 'dni' al parámetro en la consulta de eliminación
                int filasEliminadas = psDelete.executeUpdate();
                System.out.println("Filas eliminadas en personas: " + filasEliminadas);
                return filasEliminadas > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar persona con DNI: " + dni + ". Error: " + e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
        return false;
    }

    // Elimina una persona de la base de datos por su DNI sin verificar registros asociados
    public void eliminarPersona(String dni) {
        String query = "DELETE FROM personas WHERE dni = ?";  // Consulta SQL para eliminar por DNI
        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
            ps.setString(1, dni);  // Establece el DNI en la consulta
            ps.executeUpdate();  // Ejecuta la eliminación
        } catch (SQLException e) {
            e.printStackTrace();  // Captura errores y los muestra
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }
    }

    // Asocia una persona con un vehículo mediante el DNI y matrícula, y establece la fecha de inicio
  public boolean asociarPersonaConVehiculo(String dni, String matricula, Date fechaInicio) {
    conex = cbd.conectar();
    VehiculoDAO vdao = new VehiculoDAO(conex);

    int idPersona = obtenerIdPersonaPorDni(dni);
    int idVehiculo = vdao.obtenerIdVehiculo(matricula);

    if (idPersona == -1 || idVehiculo == -1) {
        return false;
    }

    // Actualizar la fecha de fin del propietario anterior, si existe
    String propietarioAnteriorDni = obtenerPropietarioVehiculo(idVehiculo);
    if (propietarioAnteriorDni != null) {
        actualizarFechaFin(propietarioAnteriorDni, idVehiculo, new java.sql.Date(System.currentTimeMillis()));
    }

    // Insertar el nuevo registro
    String consulta = "INSERT INTO registro (idPersona, idVehiculo, fecha_inicio, fecha_fin) VALUES (?, ?, ?, NULL)";
    try (PreparedStatement ps = conex.prepareStatement(consulta)) {
        ps.setInt(1, idPersona);
        ps.setInt(2, idVehiculo);
        ps.setDate(3, new java.sql.Date(fechaInicio.getTime()));
        boolean insertado = ps.executeUpdate() > 0;

        if (insertado) {
            vdao.actualizarNumPropietarios();
        }
        return insertado;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    // Obtiene la matrícula de un vehículo asociado a un DNI
    public String obtenerMatriculaPorDni(String dni) {
        String matricula = null;
        String query = "SELECT matricula FROM registro WHERE idPersona = (SELECT idPersona FROM personas WHERE dni = ?)";

        try (Connection conn = cbd.conectar(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                matricula = rs.getString("matricula");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener matrícula por DNI: " + e.getMessage());
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }

        return matricula;
    }

    // Obtiene el ID de persona usando su DNI
    private int obtenerIdPersonaPorDni(String dni) {
        String query = "SELECT idPersona FROM personas WHERE dni = ?";
        try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {

            ps.setString(1, dni);  // Establece correctamente el valor del parámetro

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idPersona");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // O manejar el error de otra manera
        }catch (Exception e) {
        // Manejo genérico para otros tipos de excepciones
        System.err.println("Error inesperado: " + e.getMessage());
        e.printStackTrace();
    }

        return -1;  // Si no se encuentra el DNI
    }

    // Actualiza la fecha de fin de un registro cuando cambia el propietario de un vehículo
   public void actualizarFechaFin(String dniPersona, int idVehiculo, Date fechaFin) {
    String query = "UPDATE registro SET fecha_fin = ? "
                 + "WHERE idPersona = (SELECT idPersona FROM personas WHERE dni = ? LIMIT 1) "
                 + "AND idVehiculo = ? AND fecha_fin IS NULL";

    try (Connection conex = cbd.conectar(); PreparedStatement ps = conex.prepareStatement(query)) {
        ps.setDate(1, new java.sql.Date(fechaFin.getTime()));
        ps.setString(2, dniPersona);
        ps.setInt(3, idVehiculo);
        int rowsUpdated = ps.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Fecha fin actualizada correctamente.");
        } else {
            System.out.println("No se actualizó la fecha fin.");
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar la fecha fin: " + e.getMessage());
    }
}


    // Obtiene el DNI del propietario actual de un vehículo
  public String obtenerPropietarioVehiculo(int idVehiculo) {
    String query = "SELECT p.dni FROM registro r "
                 + "JOIN personas p ON r.idPersona = p.idPersona "
                 + "WHERE r.idVehiculo = ? AND r.fecha_fin IS NULL";
    try (Connection conex = cbd.conectar(); 
         PreparedStatement ps = conex.prepareStatement(query)) {
        ps.setInt(1, idVehiculo);  // Establece el parámetro antes de ejecutar el query
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("dni");
            }
        }
    } catch (SQLException e) {
        System.err.println("Error al obtener propietario de vehículo: " + e.getMessage());
    }
    return null;
}

}
