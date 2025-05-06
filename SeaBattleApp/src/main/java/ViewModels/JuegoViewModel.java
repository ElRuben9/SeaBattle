/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;

import negocio.Tablero;

/**
 *
 * @author ruben
 */
public class JuegoViewModel {
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;
    private boolean turnoJugador1; // true = turno Jugador 1, false = turno Jugador 2

    public JuegoViewModel(Tablero jugador1, Tablero jugador2) {
        this.tableroJugador1 = jugador1;
        this.tableroJugador2 = jugador2;
        this.turnoJugador1 = true; // Empieza el jugador 1
    }

    public boolean disparar(int x, int y) {
        if (turnoJugador1) {
            return tableroJugador2.realizarDisparo(x, y); // Jugador 1 dispara al tablero de Jugador 2
        } else {
            return tableroJugador1.realizarDisparo(x, y); // Jugador 2 dispara al tablero de Jugador 1
        }
    }

    public void cambiarTurno() {
        turnoJugador1 = !turnoJugador1;
    }

   public boolean hayGanador() {
    return tableroJugador1.verificarGanador() || tableroJugador2.verificarGanador();
}

public String obtenerGanador() {
    if (tableroJugador2.verificarGanador()) return "Jugador 1";
    if (tableroJugador1.verificarGanador()) return "Jugador 2";
    return null;
}


    public boolean esTurnoJugador1() {
        return turnoJugador1;
    }
}
