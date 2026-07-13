package modelo;

import java.util.Locale;
import java.util.Random;
import java.util.UUID;

/**
 * Factoría centralizada para crear residuos sin acoplar el controlador a sus clases concretas.
 */
public class ResiduoFactory {
    public enum TipoResiduo {
        PLASTICO, VIDRIO, PAPEL, METAL
    }

    private final Random random;

    public ResiduoFactory() {
        this(new Random());
    }

    public ResiduoFactory(Random random) {
        this.random = random;
    }

    public Residuo crearAleatorio() {
        TipoResiduo[] tipos = TipoResiduo.values();
        return crear(tipos[random.nextInt(tipos.length)]);
    }

    public Residuo crear(TipoResiduo tipo) {
        String id = "R-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase(Locale.ROOT);
        double peso = redondear(0.5 + random.nextDouble() * 7.5);
        double toxicidad = redondear(random.nextDouble() * 0.45);

        return switch (tipo) {
            case PLASTICO -> new ResiduoPlastico(id, peso, toxicidad);
            case VIDRIO -> new ResiduoVidrio(id, peso, toxicidad);
            case PAPEL -> new ResiduoPapel(id, peso, toxicidad);
            case METAL -> new ResiduoMetal(id, peso, toxicidad);
        };
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
