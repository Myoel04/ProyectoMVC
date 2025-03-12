/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICE;

import DAO.GenericDAO;
import DTO.VehiculoDTO;
import MODEL.vehiculo;
import java.util.List;

/**
 *
 * @author yosoy
 */
public class VehiculoService extends GenericService<vehiculo> {

    public VehiculoService(GenericDAO<vehiculo> dao) {
        super(dao);
    }

    public VehiculoDTO buscarId(int id) {
        vehiculo v = super.findById(id);
        if (v != null) {
            return new VehiculoDTO(
                    v.getMarcaVehiculo(),
                    v.getAnioVehiculo(),
                    v.getMarcaVehiculo(),
                    v.getModeloVehiculo(),
                    v.getFecha_inicio(), // Aquí obtenemos la fecha de inicio
                    v.getFecha_fin() // Aquí obtenemos la fecha de fin
            );
        }
        return null;
    }

    //metodo para crear vehiculo
    public boolean crearVehiculo(VehiculoDTO vdto) {
        vehiculo v = new vehiculo();
        v.setMatriculaVehiculo(vdto.getMatricula());
        v.setAnioVehiculo(vdto.getAnio());
        v.setMarcaVehiculo(vdto.getMarca());
        v.setModeloVehiculo(vdto.getModelo());

        List<vehiculo> vehiculos = super.findAll();
        int nuevoId = (vehiculos.isEmpty() ? 1 : vehiculos.get(vehiculos.size() - 1).getIdVehiculo() + 1);
        v.setIdVehiculo(nuevoId);

        super.save(v);
        return true;

    }

    //metodo para eliminar vehiculo
    public boolean delete(int id) {
        return super.delete(id);

    }

    //metodo de actualizar
    public boolean actuVehiculo(int id, VehiculoDTO vdto) {
        vehiculo v = super.findById(id);
        if (v != null) {
            v.setMatriculaVehiculo(vdto.getMatricula());
            v.setAnioVehiculo(vdto.getAnio());
            v.setMarcaVehiculo(vdto.getMarca());
            v.setModeloVehiculo(vdto.getModelo());

            return super.update(id, v);

        }
        return false;

    }

}
