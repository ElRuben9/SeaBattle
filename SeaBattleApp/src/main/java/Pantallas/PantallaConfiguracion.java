/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import BusEvent.EventBus;
import BusEvent.EventoCambioUsuario;
import ViewModels.ConfiguracionViewModel;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import utilerias.BotonPersonalizado;
import utilerias.PanelTransparente;
import utilerias.PersonalizacionGeneral;

/**
 *
 * @author ruben
 */
public class PantallaConfiguracion extends javax.swing.JFrame {

    private ConfiguracionViewModel viewModel;

    String fondo = "recursos/interfaz/fondoConfiguracion.png";
    String correcto = "recursos/interfaz/correcto.png";

    Border bordePorDefecto = BorderFactory.createLineBorder(Color.BLACK, 1);
    Border bordeSeleccion = BorderFactory.createMatteBorder(7, 7, 7, 7, Color.ORANGE);

    String seleccion = "";

    Color color;
    String nombre, colorString;

    PantallaPrincipal inicio;

    /**
     * Creates new form PantallaPrincipal
     */
    public PantallaConfiguracion(PantallaPrincipal inicio) {
        initComponents();

        this.inicio = inicio;

        this.viewModel = new ConfiguracionViewModel();
        cargarInterfaz();

    }

    private void cargarInterfaz() {
        viewModel.cargarConfiguracionDesdeArchivo();
        try {

            PersonalizacionGeneral.colocarImagenDesenfocadaLabel(jblFondo, fondo, 12);
            personazilarBotones();
            emitirEventoCambioUsuarioDesdeArchivo();

        } catch (IOException ex) {
            Logger.getLogger(PantallaConfiguracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void personazilarBotones() {

        btnGuardar.setContentAreaFilled(false);
        btnGuardar.setBorderPainted(false);
        btnGuardar.setOpaque(false);
        btnGuardar.setUI(new BotonPersonalizado(25, new Color(21, 255, 0), new Color(21, 255, 168), 3));

        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setOpaque(false);
        btnVolver.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255), 3));

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
        jPanelHead = new javax.swing.JPanel();
        jblConfiguracion = new javax.swing.JLabel();
        jPanelConfiguracion = new PanelTransparente((float)0.35);
        jPanelBaseColores = new PanelTransparente((float) 0.35);
        jPanelColores = new javax.swing.JPanel();
        jPanelColor1 = new javax.swing.JPanel();
        jPanelColor2 = new javax.swing.JPanel();
        jPanelColor3 = new javax.swing.JPanel();
        jPanelColor4 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Batalla Naval");
        setMinimumSize(new java.awt.Dimension(950, 600));
        setSize(new java.awt.Dimension(950, 600));

        jPanelFondo.setLayout(null);

        jPanelHead.setBackground(new java.awt.Color(13, 26, 51));

        jblConfiguracion.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
        jblConfiguracion.setForeground(new java.awt.Color(255, 255, 255));
        jblConfiguracion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblConfiguracion.setText("Configuracion");

        javax.swing.GroupLayout jPanelHeadLayout = new javax.swing.GroupLayout(jPanelHead);
        jPanelHead.setLayout(jPanelHeadLayout);
        jPanelHeadLayout.setHorizontalGroup(
            jPanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
            .addGroup(jPanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelHeadLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jblConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 950, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanelHeadLayout.setVerticalGroup(
            jPanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
            .addGroup(jPanelHeadLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelHeadLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jblConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanelFondo.add(jPanelHead);
        jPanelHead.setBounds(0, 0, 950, 60);

        jPanelConfiguracion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanelBaseColores.setBackground(new java.awt.Color(204, 204, 204));
        jPanelBaseColores.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanelColores.setOpaque(false);
        jPanelColores.setLayout(new java.awt.GridLayout(1, 4, 41, 50));

        jPanelColor1.setBackground(new java.awt.Color(0, 0, 255));
        jPanelColor1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelColor1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelColor1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelColor1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelColor1MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelColor1Layout = new javax.swing.GroupLayout(jPanelColor1);
        jPanelColor1.setLayout(jPanelColor1Layout);
        jPanelColor1Layout.setHorizontalGroup(
            jPanelColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelColor1Layout.setVerticalGroup(
            jPanelColor1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jPanelColores.add(jPanelColor1);

        jPanelColor2.setBackground(new java.awt.Color(51, 255, 51));
        jPanelColor2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelColor2.setPreferredSize(new java.awt.Dimension(88, 88));
        jPanelColor2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelColor2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelColor2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelColor2MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelColor2Layout = new javax.swing.GroupLayout(jPanelColor2);
        jPanelColor2.setLayout(jPanelColor2Layout);
        jPanelColor2Layout.setHorizontalGroup(
            jPanelColor2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelColor2Layout.setVerticalGroup(
            jPanelColor2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jPanelColores.add(jPanelColor2);

        jPanelColor3.setBackground(new java.awt.Color(204, 0, 204));
        jPanelColor3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelColor3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelColor3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelColor3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelColor3MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelColor3Layout = new javax.swing.GroupLayout(jPanelColor3);
        jPanelColor3.setLayout(jPanelColor3Layout);
        jPanelColor3Layout.setHorizontalGroup(
            jPanelColor3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelColor3Layout.setVerticalGroup(
            jPanelColor3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jPanelColores.add(jPanelColor3);

        jPanelColor4.setBackground(new java.awt.Color(0, 255, 255));
        jPanelColor4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelColor4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelColor4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelColor4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelColor4MouseExited(evt);
            }
        });

        javax.swing.GroupLayout jPanelColor4Layout = new javax.swing.GroupLayout(jPanelColor4);
        jPanelColor4.setLayout(jPanelColor4Layout);
        jPanelColor4Layout.setHorizontalGroup(
            jPanelColor4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 125, Short.MAX_VALUE)
        );
        jPanelColor4Layout.setVerticalGroup(
            jPanelColor4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jPanelColores.add(jPanelColor4);

        javax.swing.GroupLayout jPanelBaseColoresLayout = new javax.swing.GroupLayout(jPanelBaseColores);
        jPanelBaseColores.setLayout(jPanelBaseColoresLayout);
        jPanelBaseColoresLayout.setHorizontalGroup(
            jPanelBaseColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 671, Short.MAX_VALUE)
            .addGroup(jPanelBaseColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelBaseColoresLayout.createSequentialGroup()
                    .addGap(19, 19, 19)
                    .addComponent(jPanelColores, javax.swing.GroupLayout.PREFERRED_SIZE, 633, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );
        jPanelBaseColoresLayout.setVerticalGroup(
            jPanelBaseColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 124, Short.MAX_VALUE)
            .addGroup(jPanelBaseColoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanelBaseColoresLayout.createSequentialGroup()
                    .addGap(15, 15, 15)
                    .addComponent(jPanelColores, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(19, Short.MAX_VALUE)))
        );

        txtUsuario.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        txtUsuario.setText("Jack Sparrow");

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Color del Jugador");

        javax.swing.GroupLayout jPanelConfiguracionLayout = new javax.swing.GroupLayout(jPanelConfiguracion);
        jPanelConfiguracion.setLayout(jPanelConfiguracionLayout);
        jPanelConfiguracionLayout.setHorizontalGroup(
            jPanelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelConfiguracionLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jPanelBaseColores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
            .addGroup(jPanelConfiguracionLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConfiguracionLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(220, 220, 220))
        );
        jPanelConfiguracionLayout.setVerticalGroup(
            jPanelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelConfiguracionLayout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanelConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanelBaseColores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        jPanelFondo.add(jPanelConfiguracion);
        jPanelConfiguracion.setBounds(80, 120, 760, 340);

        btnVolver.setBackground(new java.awt.Color(0, 166, 255));
        btnVolver.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVolverMouseClicked(evt);
            }
        });
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        jPanelFondo.add(btnVolver);
        btnVolver.setBounds(80, 490, 200, 60);

        btnGuardar.setBackground(new java.awt.Color(21, 255, 0));
        btnGuardar.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGuardarMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnGuardar);
        btnGuardar.setBounds(640, 490, 200, 60);
        jPanelFondo.add(jblFondo);
        jblFondo.setBounds(0, 0, 950, 600);

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

    private void btnVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverMouseClicked
        // TODO add your handling code here:

        inicio.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_btnVolverMouseClicked

    private void btnGuardarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGuardarMouseClicked
        String nombreIngresado = txtUsuario.getText().trim();

        viewModel.setNombreUsuario(nombreIngresado);
        viewModel.setColorSeleccionado(color);

        if (viewModel.guardarConfiguracion()) {
            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, introduce un nombre y selecciona un color.", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnGuardarMouseClicked

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnVolverActionPerformed

    private void emitirEventoCambioUsuarioDesdeArchivo() {
        String nombre = "Usuario No Configurado";
        Color color = Color.GRAY;

        if (new File("jugador.txt").exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader("jugador.txt"))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split("=");
                    if (partes.length == 2) {
                        if (partes[0].equals("nombre")) {
                            nombre = partes[1];
                        } else if (partes[0].equals("color")) {
                            String[] rgb = partes[1].split(",");
                            if (rgb.length == 3) {
                                color = new Color(
                                        Integer.parseInt(rgb[0].trim()),
                                        Integer.parseInt(rgb[1].trim()),
                                        Integer.parseInt(rgb[2].trim())
                                );
                            }
                        }
                    }
                }
            } catch (IOException | NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        // Emitir el evento para que todos se actualicen
        EventBus.publicar(new EventoCambioUsuario(nombre, color));
    }


    private void jPanelColor1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor1MouseEntered
        // TODO add your handling code here:
        jPanelColor1.setBorder(bordeSeleccion);

    }//GEN-LAST:event_jPanelColor1MouseEntered

    private void jPanelColor1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor1MouseExited
        // TODO add your handling code here:

        jPanelColor1.setBorder(bordePorDefecto);
        decorarColor();
    }//GEN-LAST:event_jPanelColor1MouseExited

    private void jPanelColor1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor1MouseClicked
        // TODO add your handling code here:

        seleccion = "color1";
        decorarColor();

        color = jPanelColor1.getBackground();

    }//GEN-LAST:event_jPanelColor1MouseClicked

    private void jPanelColor2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor2MouseClicked
        // TODO add your handling code here:

        seleccion = "color2";
        decorarColor();

        color = jPanelColor2.getBackground();
    }//GEN-LAST:event_jPanelColor2MouseClicked

    private void jPanelColor2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor2MouseEntered
        // TODO add your handling code here:
        jPanelColor2.setBorder(bordeSeleccion);
    }//GEN-LAST:event_jPanelColor2MouseEntered

    private void jPanelColor2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor2MouseExited
        // TODO add your handling code here:

        jPanelColor2.setBorder(bordePorDefecto);
        decorarColor();

    }//GEN-LAST:event_jPanelColor2MouseExited

    private void jPanelColor3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor3MouseClicked
        // TODO add your handling code here:

        seleccion = "color3";
        decorarColor();

        color = jPanelColor3.getBackground();

    }//GEN-LAST:event_jPanelColor3MouseClicked

    private void jPanelColor3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor3MouseEntered
        // TODO add your handling code here:
        jPanelColor3.setBorder(bordeSeleccion);
    }//GEN-LAST:event_jPanelColor3MouseEntered

    private void jPanelColor3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor3MouseExited
        // TODO add your handling code here:
        jPanelColor3.setBorder(bordePorDefecto);
        decorarColor();
    }//GEN-LAST:event_jPanelColor3MouseExited

    private void jPanelColor4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor4MouseClicked
        // TODO add your handling code here:

        seleccion = "color4";
        decorarColor();

        color = jPanelColor4.getBackground();
    }//GEN-LAST:event_jPanelColor4MouseClicked

    private void jPanelColor4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor4MouseEntered
        // TODO add your handling code here:
        jPanelColor4.setBorder(bordeSeleccion);
    }//GEN-LAST:event_jPanelColor4MouseEntered

    private void jPanelColor4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelColor4MouseExited
        // TODO add your handling code here:
        jPanelColor4.setBorder(bordePorDefecto);
        decorarColor();
    }//GEN-LAST:event_jPanelColor4MouseExited

    private void decorarColor() {

        if (seleccion.equalsIgnoreCase("")) {
            return;
        } else {

            if (seleccion.equalsIgnoreCase("color1")) {
                jPanelColor2.setBorder(bordePorDefecto);
                jPanelColor3.setBorder(bordePorDefecto);
                jPanelColor4.setBorder(bordePorDefecto);
                jPanelColor1.setBorder(bordeSeleccion);
                return;

            }

            if (seleccion.equalsIgnoreCase("color2")) {
                jPanelColor1.setBorder(bordePorDefecto);
                jPanelColor3.setBorder(bordePorDefecto);
                jPanelColor4.setBorder(bordePorDefecto);
                jPanelColor2.setBorder(bordeSeleccion);
                return;

            }

            if (seleccion.equalsIgnoreCase("color3")) {
                jPanelColor1.setBorder(bordePorDefecto);
                jPanelColor2.setBorder(bordePorDefecto);
                jPanelColor4.setBorder(bordePorDefecto);
                jPanelColor3.setBorder(bordeSeleccion);
                return;

            }

            if (seleccion.equalsIgnoreCase("color4")) {
                jPanelColor1.setBorder(bordePorDefecto);
                jPanelColor2.setBorder(bordePorDefecto);
                jPanelColor3.setBorder(bordePorDefecto);
                jPanelColor4.setBorder(bordeSeleccion);
                return;

            }
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanelBaseColores;
    private javax.swing.JPanel jPanelColor1;
    private javax.swing.JPanel jPanelColor2;
    private javax.swing.JPanel jPanelColor3;
    private javax.swing.JPanel jPanelColor4;
    private javax.swing.JPanel jPanelColores;
    private javax.swing.JPanel jPanelConfiguracion;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JPanel jPanelHead;
    private javax.swing.JLabel jblConfiguracion;
    private javax.swing.JLabel jblFondo;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
