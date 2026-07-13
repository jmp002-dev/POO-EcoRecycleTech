package modelo;

/** Residuo de plástico. */
public class ResiduoPlastico extends Residuo {
    public ResiduoPlastico(String id, double peso, double toxicidad) {
        super(id, peso, toxicidad);
    }

    @Override
    public String getTipo() {
        return "Envase de plástico";
    }

    @Override
    public boolean esReciclable() {
        return getToxicidad() <= 0.35;
    }

    @Override
    public String getCategoriaContenedor() {
        return "PLASTICO";
    }
}
