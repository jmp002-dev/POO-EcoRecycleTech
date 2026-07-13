package controlador;

import modelo.PlantaReciclaje;
import modelo.Residuo;
import modelo.ResiduoFactory;
import modelo.ResultadoProceso;
import vista.PlantaVista;

import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * Conecta las acciones de la vista con la lógica del modelo.
 */
public class PlantaControlador {
    private final PlantaReciclaje modelo;
    private final PlantaVista vista;
    private final ResiduoFactory factory;

    public PlantaControlador(PlantaReciclaje modelo, PlantaVista vista, ResiduoFactory factory) {
        this.modelo = modelo;
        this.vista = vista;
        this.factory = factory;
        configurarEventos();
        refrescarVista();
    }

    private void configurarEventos() {
        vista.addSimularListener(e -> simularEntrada());
        vista.addProcesarListener(e -> procesarResiduo());
        vista.addVaciarListener(e -> vaciarContenedor());
        vista.addWindowListenerPersonalizado(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarAplicacion();
            }
        });
    }

    private void simularEntrada() {
        try {
            Residuo residuo = factory.crearAleatorio();
            modelo.añadirResiduo(residuo);
            vista.mostrarEstado("Entrada simulada: " + residuo.getID() + " - " + residuo.getTipo());
            refrescarVista();
        } catch (RuntimeException ex) {
            mostrarError("No se pudo crear el residuo: " + ex.getMessage());
        }
    }

    private void procesarResiduo() {
        ResultadoProceso resultado = modelo.procesarPrimero();
        vista.mostrarEstado(resultado.mensaje());
        refrescarVista();
        if (!resultado.correcto() && resultado.residuo() != null && resultado.mensaje().contains("lleno")) {
            JOptionPane.showMessageDialog(vista, resultado.mensaje(), "Aviso de capacidad", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void vaciarContenedor() {
        String codigo = vista.getContenedorSeleccionado();
        if (codigo == null) {
            mostrarError("Selecciona un contenedor antes de vaciarlo.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Vaciar el contenedor " + codigo + "?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            modelo.vaciarContenedor(codigo);
            vista.mostrarEstado("Contenedor " + codigo + " vaciado.");
            refrescarVista();
        }
    }

    private void refrescarVista() {
        vista.actualizarCinta(modelo.getCinta());
        vista.actualizarContenedores(modelo.getContenedores());
    }

    private void cerrarAplicacion() {
        try {
            modelo.guardarEstado();
            vista.dispose();
            System.exit(0);
        } catch (IOException ex) {
            int respuesta = JOptionPane.showConfirmDialog(
                    vista,
                    "No se pudo guardar el estado: " + ex.getMessage() + "\n¿Cerrar de todas formas?",
                    "Error de persistencia",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            if (respuesta == JOptionPane.YES_OPTION) {
                vista.dispose();
                System.exit(1);
            }
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
