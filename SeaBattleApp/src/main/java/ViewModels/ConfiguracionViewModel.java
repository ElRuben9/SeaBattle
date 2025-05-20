/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;

import BusEvent.EventBus;
import BusEvent.EventoCambioUsuario;
import Model.Jugador;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author ruben
 */
public class ConfiguracionViewModel {
    private String nombreUsuario;
    private Color colorSeleccionado;

    public void setNombreUsuario(String nombre) {
        this.nombreUsuario = nombre;
    }

    public void setColorSeleccionado(Color color) {
        this.colorSeleccionado = color;
    }

    public boolean guardarConfiguracion() {
        if (nombreUsuario == null || nombreUsuario.isEmpty() || colorSeleccionado == null) {
            return false;
        }

        try (FileWriter writer = new FileWriter("jugador.txt")) {
            String colorStr = colorSeleccionado.getRed() + "," + colorSeleccionado.getGreen() + "," + colorSeleccionado.getBlue();
            writer.write("nombre=" + nombreUsuario + "\n");
            writer.write("color=" + colorStr + "\n");
            EventBus.publicar(new EventoCambioUsuario(nombreUsuario, colorSeleccionado));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cargarConfiguracionDesdeArchivo() {
        nombreUsuario = "Usuario No Configurado";
        colorSeleccionado = Color.GRAY;

        File archivo = new File("jugador.txt");
        if (archivo.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split("=");
                    if (partes.length == 2) {
                        if (partes[0].equals("nombre")) {
                            nombreUsuario = partes[1];
                        } else if (partes[0].equals("color")) {
                            String[] rgb = partes[1].split(",");
                            colorSeleccionado = new Color(
                                Integer.parseInt(rgb[0]),
                                Integer.parseInt(rgb[1]),
                                Integer.parseInt(rgb[2])
                            );
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        EventBus.publicar(new EventoCambioUsuario(nombreUsuario, colorSeleccionado));
    }
}

