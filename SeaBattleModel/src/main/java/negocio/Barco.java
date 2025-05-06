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
    private int impactosRecibidos;
 private Orientacion orientacion;  
 
    public Barco(TipoBarco tipo, int tamaño, Orientacion orientacion) {
        this.tipo = tipo;
        this.tamaño = tamaño;
        this.orientacion = orientacion;
        this.estado = EstadoBarco.INTEGRO;
        this.impactosRecibidos = 0;
    }


    public Barco(TipoBarco tipo) {
        this.tipo = tipo;
        switch (tipo) {
            case PORTAAVIONES -> tamaño = 5;
            case CRUCERO -> tamaño = 4;
            case SUBMARINO -> tamaño = 3;
            case BARCO -> tamaño = 2;
        }
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

    public Orientacion getOrientacion() {
        return orientacion;
    }

    public EstadoBarco getEstado() {
        return estado;
    }

    public TipoBarco getTipo() {
        return tipo;
    }

    public void setTipo(TipoBarco tipo) {
        this.tipo = tipo;
    }

    public int getImpactosRecibidos() {
        return impactosRecibidos;
    }

    public void setImpactosRecibidos(int impactosRecibidos) {
        this.impactosRecibidos = impactosRecibidos;
    }
    
}
