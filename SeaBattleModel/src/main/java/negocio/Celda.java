/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import state.AguaState;
import state.BarcoState;
import state.FalloState;
import state.IEstadoCeldaState;
import state.ImpactoState;

/**
 *
 * @author ruben
 */
public class Celda {


        private int coordenadaX;
    private int coordenadaY;
    private IEstadoCeldaState estado;
    private Barco barco;

    public Celda(int x, int y) {
        this.coordenadaX = x;
        this.coordenadaY = y;
        this.estado = new AguaState();
    }

    public boolean disparar() {
        return estado.disparar(this);
    }

    public void asignarBarco(Barco barco) {
        this.barco = barco;
        this.estado = new BarcoState();
    }

    public boolean fueImpactado() {
        return estado instanceof ImpactoState || estado instanceof FalloState;
    }

    public boolean tieneBarco() {
        return barco != null;
    }

    // Getters y setters
    public Barco getBarco() {
        return barco;
    }

    public void setEstado(IEstadoCeldaState nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public IEstadoCeldaState getEstado() {
        return estado;
    }

    public int getCoordenadaX() {
        return coordenadaX;
    }

    public int getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaX(int coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public void setCoordenadaY(int coordenadaY) {
        this.coordenadaY = coordenadaY;
    }
    
    
    
    
    
}
