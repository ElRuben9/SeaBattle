/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;

import Observer.ObservadorJuego;
import java.util.ArrayList;
import java.util.List;
import negocio.Tablero;

/**
 *
 * @author ruben
 */
public class JuegoViewModel {

    private Tablero tableroJugador1;
    private Tablero tableroJugador2;
    private boolean turnoJugador1; // true = turno Jugador 1, false = turno Jugador 2
    private final List<ObservadorJuego> observadores = new ArrayList<>();

    public JuegoViewModel(Tablero jugador1, Tablero jugador2) {
        this.tableroJugador1 = jugador1;
        this.tableroJugador2 = jugador2;
        this.turnoJugador1 = true; // Empieza el jugador 1
    }

    public void cambiarTurno() {
        turnoJugador1 = !turnoJugador1;

        notificarCambioTurno();
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
        notificarImpacto(x, y, acierto, false); // ataque saliente

        if (hayGanador()) {
            String ganador = obtenerGanador();
            notificarFinJuego(ganador);
            return acierto;
        }

        cambiarTurno();
        return acierto;
    }

    public boolean disparar(int x, int y) {
        return tableroJugador2.realizarDisparo(x, y);
    }

    public boolean recibirAtaque(int x, int y) {
        boolean acierto = tableroJugador1.realizarDisparo(x, y);
        notificarImpacto(x, y, acierto, true); // ataque entrante

        if (hayGanador()) {
            String ganador = obtenerGanador();
            notificarFinJuego(ganador);
        }

        return acierto;
    }

    public boolean esTurnoJugador1() {
        return turnoJugador1;
    }

    // ------------------ MÉTODOS OBSERVER ------------------
    public void agregarObservador(ObservadorJuego obs) {
        observadores.add(obs);
    }

    public void quitarObservador(ObservadorJuego obs) {
        observadores.remove(obs);
    }

    private void notificarCambioTurno() {
        for (ObservadorJuego obs : observadores) {
            obs.onCambioTurno(turnoJugador1);
        }
    }

    private void notificarImpacto(int x, int y, boolean acierto, boolean esAtaqueEntrante) {
        for (ObservadorJuego obs : observadores) {
            obs.onImpacto(x, y, acierto, esAtaqueEntrante);
        }
    }

    private void notificarFinJuego(String ganador) {
        for (ObservadorJuego obs : observadores) {
            obs.onFinJuego(ganador);
        }
    }
}
