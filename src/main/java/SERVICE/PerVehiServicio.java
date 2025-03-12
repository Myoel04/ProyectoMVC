/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICE;

import DAO.PersonaDAO;
import DAO.VehiculoDAO;
import DTO.PersonaDTO;
import DTO.VehiculoDTO;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author yosoy
 */
public class PerVehiServicio {
    PersonaDAO pdao = new PersonaDAO();
    VehiculoDAO vdao = new VehiculoDAO();
    
    public List<PersonaDTO> obtenerPersonas() throws SQLException{
    return pdao.obtenerPersonas();
    } 
    public List<VehiculoDTO> obtenerVehiculos() throws SQLException{
    return vdao.obtenerVehiculos();
    } 
    
}
