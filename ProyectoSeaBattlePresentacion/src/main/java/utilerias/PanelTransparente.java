/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 *
 * @author Arturo ITSON
 */
public class PanelTransparente extends JPanel{
    

    private float alpha = 0.35f; 
    private int arcWidth = 20;  
    private int arcHeight = 20;
    

    public PanelTransparente(float alpha) {
        setOpaque(false); 
        this.alpha = alpha;
    }
    
    
    public void setRoundedCorners(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(getBackground());

        // Dibuja el fondo con esquinas redondeadas
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arcWidth, arcHeight);

        g2.dispose();
        super.paintComponent(g);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }



}
