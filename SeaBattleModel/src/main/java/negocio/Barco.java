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

    public void setTamaño(int tamaño) {
        this.tamaño = tamaño;
    }

    public void setEstado(EstadoBarco estado) {
        this.estado = estado;
    }

    public void setOrientacion(Orientacion orientacion) {
        this.orientacion = orientacion;
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
