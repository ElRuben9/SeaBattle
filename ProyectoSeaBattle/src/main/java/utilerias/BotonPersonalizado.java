/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    

    public BotonPersonalizado(int radio, Color normal, Color presionado) {
        this.radio = radio;
        this.colorNormal = normal;
        this.colorPresionado = presionado;
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        Graphics2D g2 = (Graphics2D) g.create();

        Color color = b.getModel().isRollover() ? colorPresionado : colorNormal;
        g2.setColor(color);
        g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), radio, radio);

        // Borde pixelado
        g2.setColor(Color.BLACK);
        g2.drawRoundRect(0, 0, b.getWidth() - 1, b.getHeight() - 1, radio, radio);

        g2.dispose();
        super.paint(g, c); // sigue pintando el texto, íconos, etc.
    }
    
    
}
    

