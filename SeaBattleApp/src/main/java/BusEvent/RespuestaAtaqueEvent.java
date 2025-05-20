/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusEvent;

/**
 *
 * @author Ruben
 */
public class RespuestaAtaqueEvent implements Evento {
    public final int x, y;
    public final boolean impacto;
    public RespuestaAtaqueEvent(int x, int y, boolean impacto) {
        this.x = x; this.y = y; this.impacto = impacto;
    }
}
