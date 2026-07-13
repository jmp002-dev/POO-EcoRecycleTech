package modelo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Escribe el histórico de residuos depositados en recycle.log.
 */
public class RegistroProcesamiento {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Path rutaLog;

    public RegistroProcesamiento(Path rutaLog) {
        this.rutaLog = rutaLog;
    }

    public void registrar(Residuo residuo) throws IOException {
        Path parent = rutaLog.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                rutaLog,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(String.format(
                    Locale.US,
                    "%s | ID=%s | TIPO=%s | PESO=%.2f kg%n",
                    LocalDateTime.now().format(FORMATO),
                    residuo.getID(),
                    residuo.getTipo(),
                    residuo.getPeso()));
        }
    }
}
