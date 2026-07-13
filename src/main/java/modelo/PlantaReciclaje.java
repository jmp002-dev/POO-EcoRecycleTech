package modelo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo principal. Gestiona la cinta, los contenedores, el log y la persistencia.
 */
public class PlantaReciclaje {
    private final List<Residuo> cinta = new ArrayList<>();
    private final Map<String, Contenedor> contenedores = new LinkedHashMap<>();
    private final RegistroProcesamiento registro;
    private final PersistenciaPlanta persistencia;

    public PlantaReciclaje(Path rutaLog, Path rutaEstado) {
        contenedores.put("PLASTICO", new Contenedor("PLASTICO", "Depósito de plástico", 50));
        contenedores.put("VIDRIO", new Contenedor("VIDRIO", "Depósito de vidrio", 65));
        contenedores.put("PAPEL", new Contenedor("PAPEL", "Depósito de papel", 45));
        contenedores.put("METAL", new Contenedor("METAL", "Depósito de metal", 70));
        registro = new RegistroProcesamiento(rutaLog);
        persistencia = new PersistenciaPlanta(rutaEstado);
    }

    public void cargarEstado() throws IOException {
        persistencia.cargar(contenedores.values());
    }

    public void guardarEstado() throws IOException {
        persistencia.guardar(contenedores.values());
    }

    public void añadirResiduo(Residuo residuo) {
        if (residuo == null) {
            throw new IllegalArgumentException("El residuo no puede ser nulo");
        }
        cinta.add(residuo);
    }

    public ResultadoProceso procesarPrimero() {
        if (cinta.isEmpty()) {
            return new ResultadoProceso(false, "La cinta está vacía.", null);
        }

        Residuo residuo = cinta.get(0);
        if (!residuo.esReciclable()) {
            cinta.remove(0);
            return new ResultadoProceso(
                    false,
                    "El residuo " + residuo.getID() + " requiere tratamiento especial por su toxicidad.",
                    residuo);
        }

        Contenedor contenedor = contenedores.get(residuo.getCategoriaContenedor());
        if (contenedor == null) {
            return new ResultadoProceso(false, "No existe un contenedor compatible.", residuo);
        }
        if (!contenedor.puedeAceptar(residuo.getPeso())) {
            return new ResultadoProceso(
                    false,
                    "El " + contenedor.getNombre().toLowerCase() + " está lleno o no tiene espacio suficiente.",
                    residuo);
        }

        try {
            contenedor.depositar(residuo.getPeso());
            registro.registrar(residuo);
            cinta.remove(0);
            return new ResultadoProceso(
                    true,
                    residuo.getID() + " depositado en " + contenedor.getNombre().toLowerCase() + ".",
                    residuo);
        } catch (IOException ex) {
            return new ResultadoProceso(false, "El residuo se depositó, pero no se pudo escribir el log: " + ex.getMessage(), residuo);
        }
    }

    public void vaciarContenedor(String codigo) {
        Contenedor contenedor = contenedores.get(codigo);
        if (contenedor == null) {
            throw new IllegalArgumentException("Contenedor no encontrado: " + codigo);
        }
        contenedor.vaciar();
    }

    public List<Residuo> getCinta() {
        return Collections.unmodifiableList(cinta);
    }

    public List<Contenedor> getContenedores() {
        return List.copyOf(contenedores.values());
    }
}
