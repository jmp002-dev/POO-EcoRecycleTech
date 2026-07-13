package modelo;

import java.util.Locale;

/**
 * Clase base de los residuos procesados por la planta.
 */
public abstract class Residuo implements IResiduo {
    private final String id;
    private final double peso;
    private final double toxicidad;

    protected Residuo(String id, double peso, double toxicidad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El identificador no puede estar vacío");
        }
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero");
        }
        if (toxicidad < 0 || toxicidad > 1) {
            throw new IllegalArgumentException("La toxicidad debe estar entre 0 y 1");
        }
        this.id = id;
        this.peso = peso;
        this.toxicidad = toxicidad;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public double getPeso() {
        return peso;
    }

    public double getToxicidad() {
        return toxicidad;
    }

    /**
     * Indica si el residuo se puede depositar directamente.
     */
    public abstract boolean esReciclable();

    /**
     * Clave usada para localizar el contenedor compatible.
     */
    public abstract String getCategoriaContenedor();

    @Override
    public String toString() {
        return String.format(Locale.US, "%s | %s | %.2f kg", id, getTipo(), peso);
    }
}
