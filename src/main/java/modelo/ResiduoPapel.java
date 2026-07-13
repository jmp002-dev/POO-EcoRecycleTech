package modelo;

/** Residuo de papel o cartón. */
public class ResiduoPapel extends Residuo {
    public ResiduoPapel(String id, double peso, double toxicidad) {
        super(id, peso, toxicidad);
    }

    @Override
    public String getTipo() {
        return "Papel y cartón";
    }

    @Override
    public boolean esReciclable() {
        return getToxicidad() <= 0.15;
    }

    @Override
    public String getCategoriaContenedor() {
        return "PAPEL";
    }
}
