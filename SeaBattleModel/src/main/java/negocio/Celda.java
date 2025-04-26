/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author ruben
 */
public class Celda {
    private int coordenadaX;
    private int coordenadaY;
    private EstadoCelda estado;
    private Barco barco;

    public Celda(int x, int y) {
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.estado = EstadoCelda.AGUA;
    }

    public boolean tieneBarco() {
        return barco != null;
    }

    public void asignarBarco(Barco barco) {
        this.barco = barco;
        this.estado = EstadoCelda.BARCO;
    }

    public boolean disparar() {
        if (barco != null) {
            estado = EstadoCelda.IMPACTO;
            barco.tocar();
            return true;
        } else {
            estado = EstadoCelda.FALLO;
            return false;
        }
    }

    public boolean fueImpactado() {
        return estado == EstadoCelda.IMPACTO;
    }

    public EstadoCelda getEstado() {
        return estado;
    }
}
