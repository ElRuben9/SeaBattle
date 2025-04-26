/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import negocio.Tablero;

/**
 *
 * @author ruben
 */
public class JuegoDAO {
    
    private static Tablero tableroJugador = new Tablero();
    private static Tablero tableroCPU = new Tablero();

    public static Tablero obtenerTableroJugador() {
        return tableroJugador;
    }

    public static Tablero obtenerTableroCPU() {
        return tableroCPU;
    }

    public static void reiniciarTableros() {
        tableroJugador = new Tablero();
        tableroCPU = new Tablero();
    }
}
