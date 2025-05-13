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
 
    Orientacion orientacion = barco.getOrientacion();
    if (orientacion == Orientacion.HORIZONTAL) {
        if (y + barco.getTamaño() > 10) {
            return false; 
        }
        for (int i = 0; i < barco.getTamaño(); i++) {
            if (celdas[x][y + i].tieneBarco()) {
                return false; // Hay un barco en esta posición
            }
        }
        for (int i = 0; i < barco.getTamaño(); i++) {
            celdas[x][y + i].asignarBarco(barco); // Coloca el barco
        }
    } else if (orientacion == Orientacion.VERTICAL) {
        if (x + barco.getTamaño() > 10) {
            return false; // El barco no cabe
        }
        for (int i = 0; i < barco.getTamaño(); i++) {
            if (celdas[x + i][y].tieneBarco()) {
                return false; // Hay un barco en esta posición
            }
        }
        for (int i = 0; i < barco.getTamaño(); i++) {
            celdas[x + i][y].asignarBarco(barco); // Coloca el barco
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
public boolean hayBarcoEn(int x, int y) {
    return celdas[x][y].tieneBarco();
}
public String serializarBarcos() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            if (celdas[i][j].tieneBarco()) {
                sb.append(i).append(",").append(j).append(";");
            }
        }
    }
    return sb.toString();
}

public void deserializarBarcos(String data, Barco barcoTipo) {
    String[] posiciones = data.split(";");
    for (String pos : posiciones) {
        if (pos.isEmpty()) continue;
        String[] partes = pos.split(",");
        int x = Integer.parseInt(partes[0]);
        int y = Integer.parseInt(partes[1]);
        celdas[x][y].asignarBarco(barcoTipo);
    }
}

    public Celda[][] getCeldas() {
        return celdas;
    }
}
