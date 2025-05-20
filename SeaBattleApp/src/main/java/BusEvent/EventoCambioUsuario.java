/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusEvent;

import java.awt.Color;

/**
 *
 * @author Ruben
 */
public class EventoCambioUsuario implements Evento {
    private final String nuevoNombre;
    private final Color nuevoColor;

    public EventoCambioUsuario(String nuevoNombre, Color nuevoColor) {
        this.nuevoNombre = nuevoNombre;
        this.nuevoColor = nuevoColor;
    }

    public String getNuevoNombre() {
        return nuevoNombre;
    }

    public Color getNuevoColor() {
        return nuevoColor;
    }
}

