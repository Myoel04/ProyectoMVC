/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePropio;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author yosoy
 */
public class Temporizador extends JButton implements Serializable {

    private int tiempo = 10;
    private Timer timer;
    private CapturaListener listener;
    String texto = "Cancelar";

//constructor de la clase 
    public Temporizador() {
        super("Púlsame(10)");
        //colores del boton
        setForeground(Color.red);
        setBackground(Color.white);
        // Acción del botón
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (tiempo == 10) { // Si el tiempo es 10, comienza el Temporizador
                    comenzarTemporizador();
                }
            }
        });
    }

//metodo para empezar a contar 
    private void comenzarTemporizador() {
        //declaro el timer ya declarado antes(el 1000 == 1 segundo)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tiempo--;//contador para que quite tiempo
                setText("Púlsame (" + tiempo + ")");
                //si llega a 0 que pare de contar acia atras
                if (tiempo == 0) {
                    timer.stop();
                    //linea importante
                    if (listener != null) {
                        listener.capturarFinCuentaAtras(new BotonEvento(this));
                    }
                }
            }
        });
        timer.start();
    }

    public void setColorTexto(Color color) {
        setBackground(Color.red);
    }

    //un set del tiempo
    public void setTiempo(int tiemporestante) {

        this.tiempo = tiemporestante;
        setText("Púlsame(" + tiemporestante + ")");

    }

    public void setTextoNuevo(String textoNuevo) {
        this.texto = textoNuevo;
    }

    //el public interface obligatorio si no no se hace componente
    public interface CapturaListener extends EventListener {

        void capturarFinCuentaAtras(BotonEvento evt);

    }

    //un add del listener
    public void addCapturaListener(CapturaListener l) {
        this.listener = l;

    }

    //un remove del listener
    public void removeCapturaListener(CapturaListener l) {
        if (this.listener == l) {
            this.listener = null;
        }

    }

    //un set del listener
    public void setCapturaListener(CapturaListener listener) {
        this.listener = listener;
    }

}

