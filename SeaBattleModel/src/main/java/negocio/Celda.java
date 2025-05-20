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

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public Barco getBarco() {
        return barco;
    }

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
        if (estado == EstadoCelda.IMPACTO || estado == EstadoCelda.FALLO) {
            return false;
        }

        if (barco != null) {
            estado = EstadoCelda.IMPACTO;
            barco.tocar();
            return true;
        } else {
            estado = EstadoCelda.FALLO;
            return false;
        }
    }

    public void setImpactado(boolean impacto) {
        if (impacto) {
            estado = EstadoCelda.IMPACTO;
            if (barco != null) {
                barco.tocar();
            }
        } else {
            estado = EstadoCelda.FALLO;
        }
    }

    public boolean fueImpactado() {
        return estado == EstadoCelda.IMPACTO || estado == EstadoCelda.FALLO;
    }

    public EstadoCelda getEstado() {
        return estado;
    }
}
