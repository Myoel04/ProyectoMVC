/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTO;

import java.util.Date;

/**
 *
 * @author yosoy
 */
public class VehiculoDTO {
    
    private String matricula;
    private int anio;
    private String marca;
    private String modelo;
    private Date fechaInicio;
    private Date fechaFin;

    public VehiculoDTO(String matricula, int anio, String marca, String modelo, Date fecha_inicio, Date fecha_fin) {
        this.matricula = matricula;
        this.anio = anio;
        this.marca = marca;
        this.modelo = modelo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public VehiculoDTO(String matricula, String marca) {
        this.matricula = matricula;
        this.marca = marca;
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

    

    


}
