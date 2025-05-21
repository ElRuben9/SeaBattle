/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import Factory.BarcoFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ruben
 */
public class Tablero {
    
    private List<Barco> barcosColocados = new ArrayList<>();

    private Celda[][] celdas;
    private Tablero tableroEnemigo;
    public Tablero() {
        celdas = new Celda[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                celdas[i][j] = new Celda(i, j);
            }
        }
    }

    private boolean verificarImpactoEnBarcoEnemigo(int x, int y) {
    return tableroEnemigo.realizarDisparo(x, y);
}
    
    public boolean colocarBarco(Barco barco, int x, int y) {

        Orientacion orientacion = barco.getOrientacion();
            for (int i = 0; i < barco.getTamaño(); i++) {
                if (orientacion == Orientacion.VERTICAL) {
                    celdas[x][y + i].asignarBarco(barco);
                } else {
                    celdas[x + i][y].asignarBarco(barco);
                }
            }
    barcosColocados.add(barco);
    return true;

    }

    public boolean realizarDisparo(int x, int y) {
        return celdas[x][y].disparar();
    }

    public boolean haPerdido() {
        return todosLosBarcosDestruidos();
    }

    public boolean todosLosBarcosDestruidos() {
        if (barcosColocados.isEmpty()) return false;

        for (Barco barco : barcosColocados) {
            if (barco.getEstado() != EstadoBarco.HUNDIDO) {
                    return false;
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

    public void marcarImpactoManual(int x, int y) {
        celdas[x][y].disparar();
    }

    public void deserializarBarcos(String data) {
        String[] lineas = data.split("\n");
        for (String linea : lineas) {
            if (linea.isEmpty()) {
                continue;
            }

            String[] partes = linea.split(":");
            String tipoStr = partes[0];
            String posicionesStr = partes[1];

            TipoBarco tipo = TipoBarco.valueOf(tipoStr); // Convierte el texto a enum
            Barco nuevoBarco = BarcoFactory.crearBarco(tipo, null);

            String[] posiciones = posicionesStr.split(";");
            for (String pos : posiciones) {
                if (pos.isEmpty()) {
                    continue;
                }
                String[] coords = pos.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                celdas[x][y].asignarBarco(nuevoBarco);
            }
        }
    }

    private boolean esCoordenadaValida(int x, int y) {
        return x >= 0 && x < 10 && y >= 0 && y < 10;
    }

    public void registrarImpacto(int x, int y) {
        if (!esCoordenadaValida(x, y)) {
            return;
        }

        Celda celda = celdas[x][y];
        if (!celda.fueImpactado()) {
            celdas[x][y].disparar();
        }
    }

    public Celda[][] getCeldas() {
        return celdas;
    }
}
