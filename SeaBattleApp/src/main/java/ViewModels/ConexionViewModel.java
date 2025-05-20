/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ViewModels;
import BusEvent.EventBus;
import BusEvent.EventoConexionExitosa;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ruben
 */

public class ConexionViewModel {

    public void conectar(String ip, int puerto) {
        new Thread(() -> {
            try {
                String colorString = "";
                String nombre = "";

                File archivoJugador = new File("jugador.txt");
                if (archivoJugador.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(archivoJugador))) {
                        String linea;
                        while ((linea = reader.readLine()) != null) {
                            String[] partes = linea.split("=");
                            if (partes.length == 2) {
                                if (partes[0].equals("nombre")) {
                                    nombre = partes[1];
                                } else if (partes[0].equals("color")) {
                                    colorString = partes[1];
                                }
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ConexionViewModel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                Socket socket = new Socket(ip, puerto);
                EventBus.publicar(new EventoConexionExitosa(socket));
            } catch (IOException e) {
                EventBus.publicar(new EventoConexionExitosa(null)); // null como señal de error
            }
        }).start();
    }
}

