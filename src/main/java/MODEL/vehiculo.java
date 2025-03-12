/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MODEL;

/**
 *
 * @author yosoy
 */import java.util.Date;  // Asegúrate de importar Date

public class vehiculo {
    private int idVehiculo;
    private String matriculaVehiculo;
    private int anioVehiculo;
    private String marcaVehiculo;
    private String modeloVehiculo;
    private Date fecha_inicio;  // Nuevo atributo
    private Date fecha_fin;
    private int numPropietarios;// Nuevo atributo

    public vehiculo() {
    }

    public vehiculo(String matriculaVehiculo, String marcaVehiculo, String modeloVehiculo) {
        this.matriculaVehiculo = matriculaVehiculo;
        this.marcaVehiculo = marcaVehiculo;
        this.modeloVehiculo = modeloVehiculo;
    }

    public vehiculo(int idVehiculo, String matriculaVehiculo, int anioVehiculo, String marcaVehiculo, String modeloVehiculo, Date fecha_inicio, Date fecha_fin, int numPropietarios) {
        this.idVehiculo = idVehiculo;
        this.matriculaVehiculo = matriculaVehiculo;
        this.anioVehiculo = anioVehiculo;
        this.marcaVehiculo = marcaVehiculo;
        this.modeloVehiculo = modeloVehiculo;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.numPropietarios = numPropietarios;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMatriculaVehiculo() {
        return matriculaVehiculo;
    }

    public void setMatriculaVehiculo(String matriculaVehiculo) {
        this.matriculaVehiculo = matriculaVehiculo;
    }

    public int getAnioVehiculo() {
        return anioVehiculo;
    }

    public void setAnioVehiculo(int anioVehiculo) {
        this.anioVehiculo = anioVehiculo;
    }

    public String getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public String getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public int getNumPropietarios() {
        return numPropietarios;
    }

    public void setNumPropietarios(int numPropietarios) {
        this.numPropietarios = numPropietarios;
    }

   
}

