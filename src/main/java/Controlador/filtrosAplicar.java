/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import DAO.VehiculoDAO;
import MODEL.datosPV;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yosoy
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class filtrosAplicar {

    private JComboBox<String> cMarca;
    private JComboBox<String> cModelo;
    private VehiculoDAO vdao;
    private JTextField tNombre;
    private JTextField tMatriculacion;
    private DefaultTableModel modeloTabla;
    private int paginaInicial = 1;
    private final int limite = 10;

    // Constructor que recibe todos los componentes necesarios para la clase
    public filtrosAplicar(JComboBox<String> cMarca, JComboBox<String> cModelo, VehiculoDAO vdao,
                           JTextField tNombre, JTextField tMatriculacion, DefaultTableModel modeloTabla) {
        this.cMarca = cMarca;
        this.cModelo = cModelo;
        this.vdao = vdao;
        this.tNombre = tNombre;
        this.tMatriculacion = tMatriculacion;
        this.modeloTabla = modeloTabla;
    }

    // Método para actualizar los modelos según la marca seleccionada
    public void actuModelos() {
        if (cMarca == null || cModelo == null) { // Verificar inicialización de ComboBoxes
            JOptionPane.showMessageDialog(null, "Uno o ambos ComboBoxes no están inicializados", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String marcaSeleccionada = (String) cMarca.getSelectedItem();
        cModelo.removeAllItems(); // Limpiar el ComboBox de modelos

        if (marcaSeleccionada == null || marcaSeleccionada.equals("Todas")) {
            // Mostrar "Todos" si no hay una marca seleccionada
            cModelo.addItem("Todos");
        } else {
            // Obtener modelos de la marca seleccionada desde VehiculoDAO
            List<String> modelos = vdao.obtenerVehiculosMarca(marcaSeleccionada);
            if (modelos.isEmpty()) {
                cModelo.addItem("Todos");
            } else {
                cModelo.addItem("Todos");
                for (String modelo : modelos) {
                    cModelo.addItem(modelo); // Añadir cada modelo al ComboBox
                }
            }
        }
    }

    // Validación de entrada con soporte para campos opcionales, tildes y espacios en nombres
    public boolean validarEntrada(JTextField tNombre, JTextField tMatriculacion, JComboBox<String> cMarca, JComboBox<String> cModelo) {
        // Validación del campo de año de matriculación
        if (!tMatriculacion.getText().isEmpty()) {
            try {
                Integer.parseInt(tMatriculacion.getText()); // Intentar parsear como entero
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // Validación del campo de nombre (solo letras, tildes y espacios)
        if (!tNombre.getText().isEmpty() && !tNombre.getText().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
            JOptionPane.showMessageDialog(null, "El nombre solo debe contener letras, tildes y espacios, sin caracteres especiales.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validación de marca seleccionada
        if (cMarca.getSelectedItem() == null || cMarca.getSelectedItem().equals("Seleccionar")) {
            JOptionPane.showMessageDialog(null, "Seleccione una marca válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validación del modelo si está seleccionado
        if (cModelo.getSelectedItem() != null && cModelo.getSelectedItem().equals("Seleccionar")) {
            JOptionPane.showMessageDialog(null, "Seleccione un modelo válido o deje el campo sin selección.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true; // Entrada válida si pasa todas las validaciones
    }

    // Actualizar la tabla con los resultados obtenidos
    public void actualizarTabla(List<datosPV> resultados) {
        modeloTabla.setRowCount(0); // Limpiar la tabla
        for (datosPV dpv : resultados) {
            modeloTabla.addRow(new Object[]{
                dpv.getNombre(),
                dpv.getDni(),
                dpv.getMatricula(),
                dpv.getAnio(),
                dpv.getMarca(),
                dpv.getModelo()
            });
        }
        System.out.println("Datos obtenidos: " + modeloTabla.getRowCount()); // Confirmar el número de filas agregadas
    }
}

