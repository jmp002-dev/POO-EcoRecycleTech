package modelo;

/** Residuo de vidrio. */
public class ResiduoVidrio extends Residuo {
    public ResiduoVidrio(String id, double peso, double toxicidad) {
        super(id, peso, toxicidad);
    }

    @Override
    public String getTipo() {
        return "Botella de vidrio";
    }

    @Override
    public boolean esReciclable() {
        return getToxicidad() <= 0.20;
    }

    @Override
    public String getCategoriaContenedor() {
        return "VIDRIO";
    }
}
