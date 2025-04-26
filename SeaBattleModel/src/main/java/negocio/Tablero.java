/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author ruben
 */
public class Tablero {
    private Celda[][] celdas;

    public Tablero() {
        celdas = new Celda[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j] = new Celda(i, j);
            }
        }
    }

    public boolean colocarBarco(Barco barco, int x, int y) {
        String orientacion = barco.getOrientacion();

        if (orientacion.equalsIgnoreCase("horizontal")) {
            if (x + barco.getTamaño() > 10) return false;
            for (int i = 0; i < barco.getTamaño(); i++) {
                if (celdas[x + i][y].tieneBarco()) return false;
            }
            for (int i = 0; i < barco.getTamaño(); i++) {
                celdas[x + i][y].asignarBarco(barco);
            }
        } else if (orientacion.equalsIgnoreCase("vertical")) {
            if (y + barco.getTamaño() > 10) return false;
            for (int i = 0; i < barco.getTamaño(); i++) {
                if (celdas[x][y + i].tieneBarco()) return false;
            }
            for (int i = 0; i < barco.getTamaño(); i++) {
                celdas[x][y + i].asignarBarco(barco);
            }
        }

        return true;
    }

    public boolean realizarDisparo(int x, int y) {
        return celdas[x][y].disparar();
    }

    public boolean verificarGanador() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (celdas[i][j].tieneBarco() && !celdas[i][j].fueImpactado()) {
                    return false;
                }
            }
        }
        return true;
    }

    public Celda[][] getCeldas() {
        return celdas;
    }
}
