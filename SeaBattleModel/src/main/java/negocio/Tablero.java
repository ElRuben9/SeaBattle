/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        if (orientacion == Orientacion.VERTICAL) {
            if (y + barco.getTamaño() > 10) {
                System.out.println("primer if");
                return false;
            }
            for (int i = 0; i < barco.getTamaño(); i++) {
                if (celdas[x][y + i].tieneBarco()) {
                    System.out.println("segundo if");
                    return false; // Hay un barco en esta posición
                }
            }
            for (int i = 0; i < barco.getTamaño(); i++) {
                celdas[x][y + i].asignarBarco(barco); // Coloca el barco
            }
        } else if (orientacion == Orientacion.HORIZONTAL) {
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

    public boolean haPerdido() {
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
    Map<Barco, List<Celda>> barcos = new HashMap<>();
    
    // Agrupar celdas por barco
    for (int i = 0; i < 10; i++) {
        for (int j = 0; j < 10; j++) {
            if (celdas[i][j].tieneBarco()) {
                Barco barco = celdas[i][j].getBarco();
                barcos.computeIfAbsent(barco, k -> new ArrayList<>()).add(celdas[i][j]);
            }
        }
    }

    // Serializar los barcos
    for (Map.Entry<Barco, List<Celda>> entry : barcos.entrySet()) {
        Barco barco = entry.getKey();
        List<Celda> celdasBarco = entry.getValue();
        sb.append(barco.getTipo()).append(":");
        for (Celda celda : celdasBarco) {
            sb.append(celda.getCoordenadaX()).append(",").append(celda.getCoordenadaY()).append(";");
        }
        sb.append("\n");
    }

    return sb.toString();
}


    public void deserializarBarcos(String data) {
    String[] lineas = data.split("\n");
    for (String linea : lineas) {
        if (linea.isEmpty()) continue;
        
        String[] partes = linea.split(":");
        String tipoStr = partes[0];
        String posicionesStr = partes[1];

        TipoBarco tipo = TipoBarco.valueOf(tipoStr); // Convierte el texto a enum
        Barco nuevoBarco = new Barco(tipo);

        String[] posiciones = posicionesStr.split(";");
        for (String pos : posiciones) {
            if (pos.isEmpty()) continue;
            String[] coords = pos.split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            celdas[x][y].asignarBarco(nuevoBarco);
        }
    }
}

    public Celda[][] getCeldas() {
        return celdas;
    }
}
