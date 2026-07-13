package vista;

import modelo.Contenedor;
import modelo.Residuo;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Ventana principal de la aplicación.
 */
public class PlantaVista extends JFrame {
    private final DefaultListModel<String> modeloCinta = new DefaultListModel<>();
    private final JList<String> listaCinta = new JList<>(modeloCinta);
    private final JButton botonSimular = new JButton("Simular entrada de residuo");
    private final JButton botonProcesar = new JButton("Procesar primer residuo");
    private final JButton botonVaciar = new JButton("Vaciar contenedor seleccionado");
    private final JLabel etiquetaEstado = new JLabel("Sistema preparado.", SwingConstants.LEFT);
    private final Map<String, JProgressBar> barras = new LinkedHashMap<>();
    private final Map<String, JLabel> etiquetasContenedor = new LinkedHashMap<>();
    private final JList<String> listaContenedores = new JList<>(new String[]{"PLASTICO", "VIDRIO", "PAPEL", "METAL"});

    public PlantaVista() {
        super("EcoRecycle Tech - Control de planta");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setMinimumSize(new Dimension(900, 570));
        setSize(980, 620);
        setLocationRelativeTo(null);
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 16, 8, 16));
        JLabel titulo = new JLabel("EcoRecycle Tech SA - Planta de clasificación automatizada");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 20f));
        cabecera.add(titulo, BorderLayout.WEST);

        JPanel panelCinta = new JPanel(new BorderLayout(8, 8));
        panelCinta.setBorder(BorderFactory.createTitledBorder("Cinta transportadora"));
        panelCinta.add(new JScrollPane(listaCinta), BorderLayout.CENTER);
        JPanel botonesCinta = new JPanel(new GridLayout(2, 1, 6, 6));
        botonesCinta.add(botonSimular);
        botonesCinta.add(botonProcesar);
        panelCinta.add(botonesCinta, BorderLayout.SOUTH);

        JPanel panelDepositos = new JPanel();
        panelDepositos.setLayout(new BoxLayout(panelDepositos, BoxLayout.Y_AXIS));
        panelDepositos.setBorder(BorderFactory.createTitledBorder("Contenedores"));
        crearBarra(panelDepositos, "PLASTICO", "Depósito de plástico");
        crearBarra(panelDepositos, "VIDRIO", "Depósito de vidrio");
        crearBarra(panelDepositos, "PAPEL", "Depósito de papel");
        crearBarra(panelDepositos, "METAL", "Depósito de metal (ampliación)");

        JPanel selector = new JPanel(new BorderLayout(6, 6));
        selector.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 4));
        selector.add(new JLabel("Seleccionar depósito para vaciado:"), BorderLayout.NORTH);
        listaContenedores.setVisibleRowCount(4);
        listaContenedores.setSelectedIndex(0);
        selector.add(new JScrollPane(listaContenedores), BorderLayout.CENTER);
        selector.add(botonVaciar, BorderLayout.SOUTH);
        panelDepositos.add(selector);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelCinta, panelDepositos);
        split.setResizeWeight(0.48);
        split.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 14, 12, 14),
                BorderFactory.createEtchedBorder()));
        pie.add(new JLabel("Estado: "));
        pie.add(etiquetaEstado);

        add(cabecera, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        add(pie, BorderLayout.SOUTH);
    }

    private void crearBarra(JPanel panel, String codigo, String nombre) {
        JPanel bloque = new JPanel(new BorderLayout(6, 4));
        bloque.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel etiqueta = new JLabel(nombre + ": 0.00 / 0.00 kg");
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setStringPainted(true);
        bloque.add(etiqueta, BorderLayout.NORTH);
        bloque.add(barra, BorderLayout.CENTER);
        panel.add(bloque);
        etiquetasContenedor.put(codigo, etiqueta);
        barras.put(codigo, barra);
    }

    public void addSimularListener(ActionListener listener) {
        botonSimular.addActionListener(listener);
    }

    public void addProcesarListener(ActionListener listener) {
        botonProcesar.addActionListener(listener);
    }

    public void addVaciarListener(ActionListener listener) {
        botonVaciar.addActionListener(listener);
    }

    public void addWindowListenerPersonalizado(WindowAdapter listener) {
        addWindowListener(listener);
    }

    public String getContenedorSeleccionado() {
        return listaContenedores.getSelectedValue();
    }

    public void actualizarCinta(List<Residuo> residuos) {
        modeloCinta.clear();
        for (Residuo residuo : residuos) {
            modeloCinta.addElement(residuo.toString());
        }
    }

    public void actualizarContenedores(List<Contenedor> contenedores) {
        for (Contenedor contenedor : contenedores) {
            JProgressBar barra = barras.get(contenedor.getCodigo());
            JLabel etiqueta = etiquetasContenedor.get(contenedor.getCodigo());
            if (barra != null && etiqueta != null) {
                int porcentaje = (int) Math.round(contenedor.getPorcentajeLlenado());
                barra.setValue(porcentaje);
                barra.setString(porcentaje + "%");
                etiqueta.setText(String.format(
                        "%s: %.2f / %.2f kg",
                        contenedor.getNombre(),
                        contenedor.getLlenadoActual(),
                        contenedor.getCapacidadMaxima()));
            }
        }
    }

    public void mostrarEstado(String mensaje) {
        etiquetaEstado.setText(mensaje);
    }
}
