/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package state;

import negocio.Celda;

/**
 *
 * @author Arturo ITSON
 */
public class BarcoState implements IEstadoCeldaState{
    
    
    @Override
    public boolean disparar(Celda celda) {
        celda.setEstado(new ImpactoState());
        if (celda.getBarco() != null) {
            celda.getBarco().tocar();
        }
        return true;
    }

    public String nombre() { return "BARCO"; }
    
}
