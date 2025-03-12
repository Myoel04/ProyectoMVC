/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package SERVICE;

import DAO.GenericDAO;
import DTO.PersonaDTO;
import MODEL.persona;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yosoy
 */
public class PersonaService extends GenericService<persona>{
     // Constructor
    public PersonaService(GenericDAO<persona> dao) {
        super(dao);
    }

    // Método para buscar una persona por ID y devolver un PersonaDTO
    public PersonaDTO findByID(int id) {
        persona pers = super.findById(id);  // Usa el método findById de GenericService
        if (pers != null) {
            return new PersonaDTO(pers);  // Usa el constructor de PersonaDTO que toma un objeto persona
        }
        return null;  // Si no se encuentra, retorna null
    }

    // Método para obtener todas las personas y devolver una lista de PersonaDTO
    public List<PersonaDTO> findAllS() {
        List<persona> perss = super.findAll();  // Obtiene la lista de personas usando el método findAll de GenericService
        List<PersonaDTO> result = new ArrayList<>();
        
        for (persona p : perss) {
            result.add(new PersonaDTO(p));  // Convierte cada persona en PersonaDTO y lo añade a la lista
        }
        return result;  // Retorna la lista de PersonaDTOs
    }

    // Método para crear una nueva persona a partir de un PersonaDTO
    public boolean create(PersonaDTO pDTO) {
        persona per = new persona();  // Crea una nueva instancia de persona
        per.setNombre(pDTO.getNombre());  // Asigna los valores del DTO a la entidad persona
        per.setDni(pDTO.getDni());
        
        // Genera el nuevo ID para la persona
        List<persona> perss = super.findAll();  // Obtiene todas las personas
        int newId = (perss.isEmpty() ? 1 : perss.get(perss.size() - 1).getIdPersona() + 1);  // Asigna el nuevo ID
        per.setIdPersona(newId);

        super.save(per);  
        return true;  // Retorna true indicando que se ha creado con éxito
    }

}
