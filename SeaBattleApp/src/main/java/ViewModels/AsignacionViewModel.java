/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;
import negocio.*;

/**
 *
 * @author ruben
 */


public class AsignacionViewModel {
    private Tablero tableroJugador;

    public AsignacionViewModel() {
        tableroJugador = new Tablero();
    }

    public boolean colocarBarco(TipoBarco tipo, int tamaño, int x, int y, String orientacion) {
        Barco barco = new Barco(tipo, tamaño, orientacion);
        return tableroJugador.colocarBarco(barco, x, y);
    }

    public Tablero getTablero() {
        return tableroJugador;
    }
}


