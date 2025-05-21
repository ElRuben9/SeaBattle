/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import Pantallas.PantallaPrincipal;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author Arturo ITSON
 */
public class Metodos {
    
    
    public static String obtenerNombre(){
    
        String colorString = "";
        String[] partes = null;
        String nombre = null;
            
            if(new File("jugador.txt").exists()){
            
                try (BufferedReader reader = new BufferedReader(new FileReader("jugador.txt"))) {
                    String linea;
                    while ((linea = reader.readLine()) != null) {
                        partes = linea.split("=");
                        if (partes.length == 2) {
                            if (partes[0].equals("nombre")) {
                                nombre = partes[1];
                               // jblNombreUsuario.setText(partes[1]);
                            } else if (partes[0].equals("color")) {
                                colorString = partes[1];
                            }
                        }
                    }
                    
                   


            
                return nombre; 
                
            }
                
                  
            catch (FileNotFoundException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            catch (IOException ex) {
                Logger.getLogger(PantallaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
            return null;
    }
    
    
}
