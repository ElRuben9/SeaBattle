/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusEvent;

/**
 *
 * @author Ruben
 */
public class FinJuegoEvent implements Evento {

    public final boolean gane;

    public FinJuegoEvent(boolean gane) {
        this.gane = gane;
    }
}
