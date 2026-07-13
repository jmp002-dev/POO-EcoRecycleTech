package modelo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Guarda y recupera el nivel de los contenedores en un JSON sencillo.
 */
public class PersistenciaPlanta {
    private static final Pattern ENTRADA = Pattern.compile(
            "\\\"([A-Z]+)\\\"\\s*:\\s*\\{\\s*\\\"actual\\\"\\s*:\\s*([0-9.]+)\\s*,\\s*\\\"capacidad\\\"\\s*:\\s*([0-9.]+)\\s*}");

    private final Path rutaEstado;

    public PersistenciaPlanta(Path rutaEstado) {
        this.rutaEstado = rutaEstado;
    }

    public void guardar(Collection<Contenedor> contenedores) throws IOException {
        StringBuilder json = new StringBuilder("{\n");
        int indice = 0;
        for (Contenedor contenedor : contenedores) {
            json.append(String.format(
                    Locale.US,
                    "  \"%s\": {\"actual\": %.2f, \"capacidad\": %.2f}",
                    contenedor.getCodigo(),
                    contenedor.getLlenadoActual(),
                    contenedor.getCapacidadMaxima()));
            if (++indice < contenedores.size()) {
                json.append(',');
            }
            json.append('\n');
        }
        json.append("}\n");
        Files.writeString(rutaEstado, json.toString(), StandardCharsets.UTF_8);
    }

    public void cargar(Collection<Contenedor> contenedores) throws IOException {
        if (!Files.exists(rutaEstado)) {
            return;
        }
        String contenido = Files.readString(rutaEstado, StandardCharsets.UTF_8);
        Matcher matcher = ENTRADA.matcher(contenido);
        while (matcher.find()) {
            String codigo = matcher.group(1);
            double actual = Double.parseDouble(matcher.group(2));
            contenedores.stream()
                    .filter(c -> c.getCodigo().equals(codigo))
                    .findFirst()
                    .ifPresent(c -> c.restaurarNivel(Math.min(actual, c.getCapacidadMaxima())));
        }
    }
}
