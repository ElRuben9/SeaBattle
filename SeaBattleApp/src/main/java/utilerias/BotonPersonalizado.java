/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 *
 * @author Arturo ITSON
 */
public class BotonPersonalizado extends BasicButtonUI {
    
    
    private int radio;
    private Color colorNormal;
    private Color colorPresionado;
    private int grosorBorde;
    

    public BotonPersonalizado(int radio, Color normal, Color presionado, int grosorBorde) {
        this.radio = radio;
        this.colorNormal = normal;
        this.colorPresionado = presionado;
        this.grosorBorde = grosorBorde;
    }


    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();

        // Antialias para suavizar bordes
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Color de fondo dependiendo del estado
        Color color = b.getModel().isRollover() ? colorPresionado : colorNormal;
        g2.setColor(color);
        g2.fillRoundRect(0, 0, b.getWidth()- 1, b.getHeight()- 1, radio, radio);

        // Grosor y color del borde
        g2.setStroke(new BasicStroke(grosorBorde));
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(grosorBorde / 2, grosorBorde / 2, 
                         b.getWidth() - grosorBorde, b.getHeight() - grosorBorde, radio, radio);

        g2.dispose();

        // Pinta texto e íconos por defecto
        super.paint(g, c);
    }
}
    
   
    

