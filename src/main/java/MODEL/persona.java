/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author yosoy
 */
public class persona {
    private int idPersona;
    private String nombre;
    private char sexo;
    private String dni;

    public persona() {
    }

    public persona(String nombre, String dni) {
        this.nombre = nombre;
        this.dni = dni;
    }

    public persona(int idPersona, String nombre, char sexo, String dni) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.sexo = sexo;
        this.dni = dni;
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

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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
