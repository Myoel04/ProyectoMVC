package Vista;

import Controlador.personaControlador;
import DAO.PersonaDAO;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ventCrearPersona extends JFrame {

    private PersonaDAO pdao;
    private ventAsociar ventanaAsociar;

    public ventCrearPersona(PersonaDAO pdao, ventAsociar ventanaAsociar) {
        this.pdao = pdao;
        this.ventanaAsociar = ventanaAsociar;

        setTitle("Crear Persona");
        setLayout(new GridLayout(4, 2));

        // Creación de los componentes de la interfaz
        JLabel lNombre = new JLabel("   Nombre: ");
        JTextField tNombre = new JTextField();
        tNombre.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.toString(c).matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]$") && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lDni = new JLabel("   DNI: ");
        JTextField tDni = new JTextField();
        tDni.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isLetterOrDigit(c) && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten letras y números", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JLabel lSexo = new JLabel("   Sexo (M/F): ");
        JTextField tSexo = new JTextField();
        tSexo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.toString(c).matches("^[MFmf]$") && !Character.isISOControl(c)) {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten las letras 'M' o 'F'", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Bloquear pegado no válido
        bloquearPegado(tNombre, "^[a-zA-ZáéíóúÁÉÍÓÚñÑ'ü ]+$", "Solo se permiten letras y espacios");
        bloquearPegado(tDni, "^[a-zA-Z0-9]+$", "Solo se permiten letras y números");
        bloquearPegado(tSexo, "^[MFmf]{0,1}$", "Solo se permiten las letras 'M' o 'F'");

        JButton bGuardar = new JButton("Guardar");
        JButton bCancelar = new JButton("Cancelar");

        // Agregar los componentes al layout
        add(lNombre);
        add(tNombre);
        add(lDni);
        add(tDni);
        add(lSexo);
        add(tSexo);
        add(bGuardar);
        add(bCancelar);

        // Acción del botón Guardar
        bGuardar.addActionListener(e -> confirmarYGuardarPersona(tNombre, tDni, tSexo));

        // Acción del botón Cancelar
        bCancelar.addActionListener(e -> dispose());

        // Configuración de la ventana
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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

    // Método para confirmar antes de guardar la persona
    private void confirmarYGuardarPersona(JTextField tNombre, JTextField tDni, JTextField tSexo) {
        int respuesta = JOptionPane.showConfirmDialog(
            ventCrearPersona.this,
            "¿Estás seguro de que deseas crear esta persona?",
            "Confirmación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (respuesta == JOptionPane.YES_OPTION) {
            String nombre = tNombre.getText().trim();
            String dni = tDni.getText().trim();
            String sexo = tSexo.getText().toUpperCase().trim();

            if (nombre.isEmpty() || dni.isEmpty() || sexo.isEmpty()) {
                JOptionPane.showMessageDialog(ventCrearPersona.this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (!(sexo.equals("M") || sexo.equals("F"))) {
                JOptionPane.showMessageDialog(ventCrearPersona.this, "Sexo inválido. Solo se permite 'M' o 'F'.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                personaControlador controlador = new personaControlador();

                if (controlador.agregarPersona(nombre, dni, sexo.charAt(0))) {
                    JOptionPane.showMessageDialog(ventCrearPersona.this, "Persona creada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    if (ventanaAsociar != null) {
                        ventanaAsociar.recargarTablaPersonas(pdao);
                    }

                    tNombre.setText("");
                    tDni.setText("");
                    tSexo.setText("");
                } else {
                    JOptionPane.showMessageDialog(ventCrearPersona.this, "Error al crear la persona.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(ventCrearPersona.this, "Operación cancelada.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
