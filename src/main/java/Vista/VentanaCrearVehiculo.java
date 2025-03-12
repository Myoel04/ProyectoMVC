package Vista;

import Controlador.vehiculoControlador;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class VentanaCrearVehiculo extends JFrame {

    private JTextField tMatricula, tAnio, tMarca, tModelo;
    private JButton bOk, bCancelar;
    private vehiculoControlador vControlador;

    public VentanaCrearVehiculo(vehiculoControlador vControlador) {
        if (vControlador == null) {
            throw new IllegalArgumentException("El controlador de vehículos no puede ser nulo");
        }
        this.vControlador = vControlador;

        setTitle("Crear Vehículo");
        setSize(300, 300);
        setLayout(new GridLayout(6, 2));

        JLabel lMatricula = new JLabel("   Matrícula: ");
        tMatricula = new JTextField();
        tMatricula.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten caracteres alfanuméricos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lAnio = new JLabel("   Año: ");
        tAnio = new JTextField();
        tAnio.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten números", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (tAnio.getText().length() >= 4) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "El año debe tener 4 dígitos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lMarca = new JLabel("   Marca: ");
        tMarca = new JTextField();
        tMarca.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.toString(c).matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]$") && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lModelo = new JLabel("   Modelo: ");
        tModelo = new JTextField();
        tModelo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.toString(c).matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]$") && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bloquear pegado no válido
        bloquearPegado(tMatricula, "^[a-zA-Z0-9 ]+$", "Solo se permiten caracteres alfanuméricos");
        bloquearPegado(tAnio, "^[0-9]{0,4}$", "Solo se permiten números de hasta 4 dígitos");
        bloquearPegado(tMarca, "^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]+$", "Solo se permiten letras y espacios");
        bloquearPegado(tModelo, "^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]+$", "Solo se permiten letras y espacios");

        add(lMatricula);
        add(tMatricula);
        add(lAnio);
        add(tAnio);
        add(lMarca);
        add(tMarca);
        add(lModelo);
        add(tModelo);

        JPanel pBotones = new JPanel();
        pBotones.setLayout(new GridBagLayout());

        bOk = new JButton("Ok");
        bCancelar = new JButton("Cancelar");

        pBotones.add(bOk);
        pBotones.add(bCancelar);
        add(pBotones);

        bCancelar.addActionListener(e -> dispose());
        bOk.addActionListener(e -> guardarVehiculo());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void guardarVehiculo() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas guardar este vehículo?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            String matricula = tMatricula.getText().trim();
            String marca = tMarca.getText().trim();
            String modelo = tModelo.getText().trim();
            Integer anio = null;

            try {
                anio = Integer.parseInt(tAnio.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El año debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (matricula.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anio == null) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            vControlador.agregarVehiculo(matricula, anio, marca, modelo);
            JOptionPane.showMessageDialog(this, "Vehículo creado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Operación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiarCampos() {
        tMatricula.setText("");
        tAnio.setText("");
        tMarca.setText("");
        tModelo.setText("");
    }

    // Método para bloquear el pegado de texto no válido
    private void bloquearPegado(JTextField campo, String regex, String mensajeError) {
        campo.setTransferHandler(new TransferHandler() {
            @Override
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    String textoPegado = (String) t.getTransferData(DataFlavor.stringFlavor);

                    if (textoPegado != null && !textoPegado.matches(regex)) {
                        JOptionPane.showMessageDialog(null, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    return super.importData(comp, t);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return false;
                }
            }
        });
    }
}
