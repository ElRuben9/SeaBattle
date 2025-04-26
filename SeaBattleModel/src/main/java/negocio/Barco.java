/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author ruben
 */
public class Barco {
    private TipoBarco tipo;
    private int tamaño;
    private EstadoBarco estado;
    private String orientacion;
    private int impactosRecibidos;

    public Barco(TipoBarco tipo, int tamaño, String orientacion) {
        this.tipo = tipo;
        this.tamaño = tamaño;
        this.orientacion = orientacion;
        this.estado = EstadoBarco.INTEGRO;
        this.impactosRecibidos = 0;
    }

    public void tocar() {
        impactosRecibidos++;
        if (impactosRecibidos >= tamaño) {
            estado = EstadoBarco.HUNDIDO;
        } else {
            estado = EstadoBarco.TOCADO;
        }
    }

    public int getTamaño() {
        return tamaño;
    }

    public String getOrientacion() {
        return orientacion;
    }

    public EstadoBarco getEstado() {
        return estado;
    }
}
