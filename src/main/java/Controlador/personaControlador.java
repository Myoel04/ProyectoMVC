package Controlador;

import DAO.PersonaDAO;
import DTO.PersonaDTO;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class personaControlador {
    PersonaDAO pdao = new PersonaDAO();

        //METODO DE OBTENER PERSONAS
    public List<PersonaDTO> obtenerPersonas() {
        return pdao.obtenerPersonas();
    }
    //METODO DE AGREGAR PERSONAS
    public boolean agregarPersona(String nombre, String dni, char sexo) {
        if (sexo != 'M' && sexo != 'F') {
            System.err.println("Sexo inválido. Solo se permite 'M' o 'F'.");
            return false;
        }
        
        PersonaDTO persona = new PersonaDTO(nombre, dni, sexo);
        return pdao.agregarPersona(persona);
    }
    //METODO DE ELIMINAR PERSONAS
    public boolean eliminarPersona(String dni) {
        pdao.eliminarPersona(dni);
        return true;
    }
    //METODO PARA ASOCIAR
    public boolean asociarPersonaConVehiculo(String dni, String matricula, Date fechaInicio) {
        try {
            return pdao.asociarPersonaConVehiculo(dni, matricula, fechaInicio);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    //METODO DE ACTUALIZAR FECHA AL ASOCIAR
    public boolean actualizarFechaFin(String dniPersona, int idVehiculo, Date fechaFin) {
        if (dniPersona == null || dniPersona.isEmpty()) {
            System.out.println("El campo DNI está vacío");
            return false;
        }
        if (fechaFin == null) {
            System.out.println("El campo fecha fin no puede ser nulo");
            return false;
        }
        pdao.actualizarFechaFin(dniPersona, idVehiculo, fechaFin);
        return true;
    }
    //METODO DE OBTENER LOS PROPIERTARIOS
    public String obtenerPropietarioVehiculos(int idVehiculo) {
        try {
            return pdao.obtenerPropietarioVehiculo(idVehiculo);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
