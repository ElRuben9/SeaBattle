/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import java.awt.Color;
import utilerias.BotonPersonalizado;
import utilerias.PersonalizacionGeneral;

/**
 *
 * @author ruben
 */
public class PantallaPrincipal extends javax.swing.JFrame {

    
    String fondo = "recursos/interfaz/fondoPantallaInicial.png";
    
    
    /**
     * Creates new form PantallaPrincipal
     */
    public PantallaPrincipal() {
        initComponents();
        
        
        
        cargarInterfaz();
        
    }

    
    
    private void cargarInterfaz(){
        PersonalizacionGeneral.colocarImagenLabel(jblFondo, fondo);
        
        
        personazilarBotones();
    }
    
    
    private void personazilarBotones(){
        
        btnJugar.setContentAreaFilled(false);
        btnJugar.setBorderPainted(false);
        btnJugar.setOpaque(false);
        btnJugar.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255) ,3 ));
        
        
        btnOpciones.setContentAreaFilled(false);
        btnOpciones.setBorderPainted(false);
        btnOpciones.setOpaque(false);
        btnOpciones.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255) ,3));
        
        
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.setOpaque(false);
        btnSalir.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255) ,3));
        
    }
    
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelFondo = new javax.swing.JPanel();
        jPanelDerechos = new javax.swing.JPanel();
        jblDerechos = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        btnJugar = new javax.swing.JButton();
        btnOpciones = new javax.swing.JButton();
        jblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalla Naval");
        setMinimumSize(new java.awt.Dimension(950, 600));
        setSize(new java.awt.Dimension(950, 600));

        jPanelFondo.setLayout(null);

        jPanelDerechos.setBackground(new java.awt.Color(13, 26, 51));

        jblDerechos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jblDerechos.setForeground(new java.awt.Color(255, 255, 255));
        jblDerechos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblDerechos.setText("© 2025 Batalla Naval. Todos los derechos reservados.");

        javax.swing.GroupLayout jPanelDerechosLayout = new javax.swing.GroupLayout(jPanelDerechos);
        jPanelDerechos.setLayout(jPanelDerechosLayout);
        jPanelDerechosLayout.setHorizontalGroup(
            jPanelDerechosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDerechosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jblDerechos, javax.swing.GroupLayout.DEFAULT_SIZE, 938, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelDerechosLayout.setVerticalGroup(
            jPanelDerechosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jblDerechos, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
        );

        jPanelFondo.add(jPanelDerechos);
        jPanelDerechos.setBounds(0, 550, 950, 50);

        btnSalir.setBackground(new java.awt.Color(0, 166, 255));
        btnSalir.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSalirMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnSalir);
        btnSalir.setBounds(340, 400, 250, 80);

        btnJugar.setBackground(new java.awt.Color(0, 166, 255));
        btnJugar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnJugar.setText("Jugar");
        btnJugar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnJugarMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnJugar);
        btnJugar.setBounds(340, 120, 250, 80);

        btnOpciones.setBackground(new java.awt.Color(0, 166, 255));
        btnOpciones.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnOpciones.setText("Opciones");
        btnOpciones.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnOpcionesMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnOpciones);
        btnOpciones.setBounds(340, 260, 250, 80);
        jPanelFondo.add(jblFondo);
        jblFondo.setBounds(0, 0, 950, 550);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelFondo, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseClicked
        // TODO add your handling code here:
        
        this.dispose();
    }//GEN-LAST:event_btnSalirMouseClicked

    private void btnJugarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnJugarMouseClicked
        // TODO add your handling code here:
        
        PantallaEscogerPartida partida = new PantallaEscogerPartida(this);
        partida.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnJugarMouseClicked

    private void btnOpcionesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnOpcionesMouseClicked
        // TODO add your handling code here:
        
        PantallaConfiguracion confi = new PantallaConfiguracion(this);
        confi.setVisible(true);
        
        this.setVisible(false);
    }//GEN-LAST:event_btnOpcionesMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnJugar;
    private javax.swing.JButton btnOpciones;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanelDerechos;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JLabel jblDerechos;
    private javax.swing.JLabel jblFondo;
    // End of variables declaration//GEN-END:variables
}
