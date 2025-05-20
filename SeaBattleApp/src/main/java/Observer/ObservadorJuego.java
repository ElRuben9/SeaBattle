/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Observer;

/**
 *
 * @author ruben
 */

public interface ObservadorJuego {
    void onCambioTurno(boolean esTurnoJugador1);
    void onImpacto(int x, int y, boolean acierto, boolean esAtaqueEntrante);
    void onFinJuego(String ganador);
}

