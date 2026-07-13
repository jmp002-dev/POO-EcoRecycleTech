package modelo;

/**
 * Extensión añadida para demostrar que el modelo admite nuevos materiales.
 */
public class ResiduoMetal extends Residuo {
    public ResiduoMetal(String id, double peso, double toxicidad) {
        super(id, peso, toxicidad);
    }

    @Override
    public String getTipo() {
        return "Lata metálica";
    }

    @Override
    public boolean esReciclable() {
        return getToxicidad() <= 0.25;
    }

    @Override
    public String getCategoriaContenedor() {
        return "METAL";
    }
}
