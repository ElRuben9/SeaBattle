/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Factory;

import negocio.Barco;
import negocio.Orientacion;
import negocio.TipoBarco;

/**
 *
 * @author Arturo ITSON
 */
public class BarcoFactory {
    
    public static Barco crearBarco(TipoBarco tipo, Orientacion orientacion) {
        int tamaño;

        switch (tipo) {
            case PORTAAVIONES -> tamaño = 5;
            case CRUCERO -> tamaño = 4;
            case SUBMARINO -> tamaño = 3;
            case BARCO -> tamaño = 2;
            default -> throw new IllegalArgumentException("Tipo de barco desconocido: " + tipo);
        }

        return new Barco(tipo, tamaño, orientacion);
    }
    
    
    public static Barco crearBarcoDesdeTipo(TipoBarco tipo) {
    return crearBarco(tipo, null); // porque la horientacio no es necesaria aqui
}
    
}
