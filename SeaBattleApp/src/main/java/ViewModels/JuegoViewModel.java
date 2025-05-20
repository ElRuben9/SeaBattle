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

   
    public void cambiarTurno() {
        turnoJugador1 = !turnoJugador1;
    }

    public boolean hayGanador() {
        return tableroJugador1.haPerdido() || tableroJugador2.haPerdido();
    }

    
    public boolean jugadorGano() {
        return tableroJugador1.todosLosBarcosDestruidos();
    }

    public boolean jugadorPerdio() {
        return tableroJugador2.todosLosBarcosDestruidos();
    }
    
    public String verificarVictoriaJugador() {
        if (tableroJugador2.todosLosBarcosDestruidos()) {
            return "Jugador 1";
        }
        if (tableroJugador1.todosLosBarcosDestruidos()) {
            return "Jugador 2";
        }
        return null;
    }

    public String obtenerGanador() {
        if (tableroJugador2.haPerdido()) {
            return "Jugador 1";
        }
        if (tableroJugador1.haPerdido()) {
            return "Jugador 2";
        }
        return null;
    }

    public boolean dispararYAvanzarTurno(int x, int y) {
        boolean acierto = disparar(x, y);
        cambiarTurno();
        return acierto;
    }

   public boolean disparar(int x, int y) {
        return tableroJugador2.realizarDisparo(x, y);
    }

    public boolean recibirAtaque(int x, int y) {
        return tableroJugador1.realizarDisparo(x, y);
    }
    
    public boolean esTurnoJugador1() {
        return turnoJugador1;
    }
}
