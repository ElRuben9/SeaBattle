/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusEvent;

import java.net.Socket;

/**
 *
 * @author Ruben
 */
public class EventoConexionExitosa implements Evento {
    private final Socket socket;

    public EventoConexionExitosa(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }
}