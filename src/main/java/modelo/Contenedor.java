package modelo;

import java.util.Locale;

/**
 * Representa un depósito final de la planta.
 */
public class Contenedor {
    private final String codigo;
    private final String nombre;
    private final double capacidadMaxima;
    private double llenadoActual;

    public Contenedor(String codigo, String nombre, double capacidadMaxima) {
        if (codigo == null || codigo.isBlank() || nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El código y el nombre son obligatorios");
        }
        if (capacidadMaxima <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        this.codigo = codigo;
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public double getLlenadoActual() {
        return llenadoActual;
    }

    public double getPorcentajeLlenado() {
        return Math.min(100.0, (llenadoActual / capacidadMaxima) * 100.0);
    }

    public boolean puedeAceptar(double peso) {
        return peso > 0 && llenadoActual + peso <= capacidadMaxima;
    }

    public void depositar(double peso) {
        if (!puedeAceptar(peso)) {
            throw new IllegalStateException("No hay capacidad suficiente en " + nombre);
        }
        llenadoActual += peso;
    }

    public void vaciar() {
        llenadoActual = 0;
    }

    public void restaurarNivel(double nivel) {
        if (nivel < 0 || nivel > capacidadMaxima) {
            throw new IllegalArgumentException("Nivel inválido para " + nombre);
        }
        llenadoActual = nivel;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%s: %.2f / %.2f kg", nombre, llenadoActual, capacidadMaxima);
    }
}
