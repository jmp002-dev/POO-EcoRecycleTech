package controlador;

import modelo.PlantaReciclaje;
import modelo.ResiduoFactory;
import vista.PlantaVista;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.io.IOException;
import java.nio.file.Path;

/** Punto de entrada de la aplicación. */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlantaReciclaje modelo = new PlantaReciclaje(
                    Path.of("recycle.log"),
                    Path.of("estado_planta.json"));
            try {
                modelo.cargarEstado();
            } catch (IOException | RuntimeException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "No se pudo recuperar el estado anterior. Se iniciará la planta vacía.\n" + ex.getMessage(),
                        "Aviso",
                        JOptionPane.WARNING_MESSAGE);
            }

            PlantaVista vista = new PlantaVista();
            new PlantaControlador(modelo, vista, new ResiduoFactory());
            vista.setVisible(true);
        });
    }
}
