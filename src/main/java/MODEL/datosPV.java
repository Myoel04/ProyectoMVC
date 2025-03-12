/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

import java.util.Date;

/**
 *
 * @author yosoy
 */
public class datosPV {

    private int idPersona;
    private String nombre;
    private char sexo;
    private String dni;
    
    private int idVehiculo;
    private String matricula;
    private int anio;
    private String marca;
    private String modelo;
    private Date fechaInicio;
    private Date fechaFin;

    public datosPV() {
    }

    public datosPV(int idPersona, String nombre, String dni, int idVehiculo, String matricula, int anio, String marca, String modelo, Date fechaInicio, Date fechaFin) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.dni = dni;
        this.idVehiculo = idVehiculo;
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public datosPV(int idPersona, String nombre, char sexo, String dni, int idVehiculo, String matricula, int anio, String marca, String modelo, Date fechaInicio, Date fechaFin) {
        this.idPersona = idPersona;
        this.nombre = nombre;
        this.sexo = sexo;
        this.dni = dni;
        this.idVehiculo = idVehiculo;
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
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

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

   
    
    
}
