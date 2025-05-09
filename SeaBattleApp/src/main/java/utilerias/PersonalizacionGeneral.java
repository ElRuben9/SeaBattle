/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import com.jhlabs.image.GaussianFilter;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Arturo ITSON
 */
public class PersonalizacionGeneral {
    
    
    
    public PersonalizacionGeneral(){
    
    }
    
    
    
    /**
     * Metodo que coloca una imagen a un jbl
     * @param nombreJlb el jlabel que sera reemplazado por una imagen
     * @param ruta la direccion donde se encuentra la imagen
     */
    public static void colocarImagenLabel(JLabel nombreJlb, String ruta){

        ImageIcon imageIcon = new ImageIcon(ruta);
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(nombreJlb.getWidth(),
                                  nombreJlb.getHeight(), Image.SCALE_SMOOTH));
        
        nombreJlb.setIcon(icon); 

        nombreJlb.repaint();
        
    }
    
    
    
     /**
     * Metodo que coloca una imagen con desenfoque a un jbl
     * @param nombreJlb el jlabel que sera reemplazado por una imagen
     * @param ruta la direccion donde se encuentra la imagen
     * @param radioDesenfoque es el nivel de desenfoque que se le aplicara a la imagen
     */
    public static void colocarImagenDesenfocadaLabel(JLabel nombreJlb, String ruta, int radioDesenfoque) throws IOException{
        
        GaussianFilter gaussianFilter = new GaussianFilter(radioDesenfoque);
        BufferedImage imagenDesenfocada = gaussianFilter.filter(ImageIO.read(new File(ruta)), null);
        
        
        ImageIcon imageIcon = new ImageIcon(imagenDesenfocada);
        
        Icon icon = new ImageIcon(imageIcon.getImage().getScaledInstance(nombreJlb.getWidth(),
                                  nombreJlb.getHeight(), Image.SCALE_SMOOTH));
        
        nombreJlb.setIcon(icon); 

        nombreJlb.repaint();
        
    }
    
    
    
//    public static void imagenAJLabelMaxCalidad(JLabel label, String ruta){
//        
//        try {
//            BufferedImage imagenOriginal = ImageIO.read(new File(ruta));
//
//            int anchoLabel = label.getWidth();
//            int altoLabel = label.getHeight();
//
//
//            BufferedImage imagenEscalada = new BufferedImage(anchoLabel, altoLabel, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D g2d = imagenEscalada.createGraphics();
//
//            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//            g2d.drawImage(imagenOriginal, 0, 0, anchoLabel, altoLabel, null);
//            g2d.dispose();
//
//            label.setIcon(new ImageIcon(imagenEscalada));
//
//        }
//         catch (Exception e) {
//        }
//    
//    }
    
    
    
}
