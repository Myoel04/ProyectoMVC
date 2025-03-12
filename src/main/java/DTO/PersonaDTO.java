/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import MODEL.persona;

/**
 *
 * @author yosoy
 */
public class PersonaDTO {
  private int idPersona;
    private String nombre;
    private String dni;
    private char sexo;

    public PersonaDTO(persona p) {
    }

    public PersonaDTO(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public PersonaDTO(String nombre, String dni, char sexo) {
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }
    

    public PersonaDTO(int idPersona, String nombre, String dni, char sexo) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

 public String getSexoString() {
        if (sexo == 'M') {
            return "Masculino";
        } else if (sexo == 'F') {
            return "Femenino";
        }
        return "Desconocido"; // En caso de que el sexo sea un valor inválido
    }
    
}
