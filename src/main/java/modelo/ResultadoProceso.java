package modelo;

/** Resultado de un intento de procesado. */
public record ResultadoProceso(boolean correcto, String mensaje, Residuo residuo) {
}
