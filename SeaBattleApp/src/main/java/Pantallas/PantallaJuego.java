/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import utilerias.BotonPersonalizado;
import utilerias.PanelTransparente;
import utilerias.PersonalizacionGeneral;
import negocio.*;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author ruben
 */
public class PantallaJuego extends javax.swing.JFrame {

    private ViewModels.JuegoViewModel vmJuego;
    private JButton[][] botonesTablero = new JButton[10][10];
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean esMiTurno = false;

    private boolean esServidor;
    private BufferedReader entrada;
    private PrintWriter salida;

    private Tablero tableroJugador;
    private Tablero tableroEnemigo;

    String fondo = "recursos/interfaz/fondoColocarBarcos.png";

    /**
     * Creates new form PantallaJuego
     */
    public PantallaJuego(Socket socket, boolean esServidor) {
        this.socket = socket;
        this.esServidor = esServidor;

        initComponents();

        inicializarComunicacion();

        if (esServidor) {
            setTitle("Servidor - Batalla Naval");
        } else {
            setTitle("Cliente - Batalla Naval");
        }

    }

    private void inicializarComunicacion() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Puedes iniciar un hilo para escuchar mensajes entrantes
            new Thread(() -> {
                try {
                    String mensaje;
                    while ((mensaje = in.readLine()) != null) {
                        procesarMensajeRecibido(mensaje);
                    }
                } catch (IOException ex) {
                    System.err.println("Error al leer del socket: " + ex.getMessage());
                }
            }).start();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al establecer comunicación con el otro jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje) {
        salida.println(mensaje);
    }


    private void procesarMensajeRecibido(String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);
       
    }

    private void escucharServidor() {
        try {
            while (true) {
                String mensaje = in.readUTF();
                if (mensaje.equals("TU_TURNO")) {
                    esMiTurno = true;
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, "¡Tu turno!")
                    );

                } else if (mensaje.equals("ESPERA")) {
                    esMiTurno = false;

                } else if (mensaje.startsWith("ENEMIGO_DISPARO")) {
                    String[] partes = mensaje.split(":");
                    String[] coords = partes[1].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    String resultado = partes[2];

                    if (resultado.equals("AGUA")) {
                        botonesTableroJugador[x][y].setBackground(Color.WHITE);
                    } else if (resultado.equals("IMPACTO")) {
                        botonesTableroJugador[x][y].setBackground(Color.RED);
                    }

                } else if (mensaje.startsWith("RESULTADO_DISPARO")) {
                    String[] partes = mensaje.split(":");
                    String[] coords = partes[1].split(",");
                    int x = Integer.parseInt(coords[0]);
                    int y = Integer.parseInt(coords[1]);
                    String resultado = partes[2];

                    Color color = switch (resultado) {
                        case "IMPACTO" ->
                            Color.RED;
                        case "HUNDIDO" ->
                            Color.ORANGE;
                        case "AGUA" ->
                            Color.WHITE;
                        default ->
                            Color.GRAY;
                    };
                    botonesTablero[x][y].setBackground(color);
                    botonesTablero[x][y].setEnabled(false);

                } else if (mensaje.equals("GANASTE") || mensaje.equals("PERDISTE")) {
                    SwingUtilities.invokeLater(()
                            -> JOptionPane.showMessageDialog(this, mensaje)
                    );
                    socket.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cargarInterfaz() {

        PersonalizacionGeneral.colocarImagenLabel(jblFondo, fondo);

        personazilarBotones();
        crearTableroJugador1();
        crearTableroJugador2();
    }

    private void personazilarBotones() {

        btnAbandonar.setContentAreaFilled(false);
        btnAbandonar.setBorderPainted(false);
        btnAbandonar.setOpaque(false);
        btnAbandonar.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255), 3));

        btnAtacar.setContentAreaFilled(false);
        btnAtacar.setBorderPainted(false);
        btnAtacar.setOpaque(false);
        btnAtacar.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255), 3));

    }
    private JButton[][] botonesTableroJugador = new JButton[10][10];
    private JButton[][] botonesTableroEnemigo = new JButton[10][10];

    private void crearTableroJugador1() {
        JPanel panelGrid = new JPanel(new java.awt.GridLayout(10, 10));
        panelGrid.setBounds(20, 140, 390, 325);
        panelGrid.setOpaque(false);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton boton = new JButton();
                boton.setBackground(new Color(173, 216, 230));
                boton.setFocusPainted(false);
                int x = i;
                int y = j;

                boton.addActionListener(e -> disparar(x, y));
                botonesTableroJugador[i][j] = boton;
                panelGrid.add(boton);
            }
        }

        this.getContentPane().add(panelGrid, 0);
    }

    private void crearTableroJugador2() {
        JPanel panelGrid = new JPanel(new java.awt.GridLayout(10, 10));
        panelGrid.setBounds(540, 140, 390, 325);
        panelGrid.setOpaque(false);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton boton = new JButton();
                boton.setBackground(new Color(173, 216, 230));
                boton.setFocusPainted(false);
                int x = i;
                int y = j;

                boton.addActionListener(e -> disparar(x, y));

                botonesTableroEnemigo[i][j] = boton;
                panelGrid.add(boton);
            }
        }

        this.getContentPane().add(panelGrid, 0);
    }

    private void disparar(int x, int y) {
        if (!esMiTurno) {
            JOptionPane.showMessageDialog(this, "No es tu turno.");
            return;
        }

        try {
            // Enviar disparo al servidor
            String mensaje = "DISPARO:" + x + "," + y;
            out.writeUTF(mensaje);
            out.flush();

            // Bloquear turno hasta recibir respuesta del servidor
            esMiTurno = false;

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al enviar disparo al servidor.");
        }
    }

    private void actualizarTurnoEnPantalla() {
        if (vmJuego.esTurnoJugador1()) {
            jblTurno.setText("Turno: Jugador 1");
        } else {
            jblTurno.setText("Turno: Jugador 2");
        }
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
        jblTurno = new javax.swing.JLabel();
        btnAbandonar = new javax.swing.JButton();
        jblTiempo = new javax.swing.JLabel();
        jPanelNombreJugador2 = new PanelTransparente((float) 0.35);
        jblNombreJugador2 = new javax.swing.JLabel();
        jPanelNombreJugador = new PanelTransparente((float) 0.35);
        jblNombreJugador = new javax.swing.JLabel();
        jPanelDerechos = new javax.swing.JPanel();
        jblDerechos = new javax.swing.JLabel();
        btnAtacar = new javax.swing.JButton();
        jblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelFondo.setLayout(null);

        jPanelHead.setBackground(new java.awt.Color(13, 26, 51));
        jPanelHead.setLayout(null);

        jblTurno.setFont(new java.awt.Font("Monospaced", 0, 22)); // NOI18N
        jblTurno.setForeground(new java.awt.Color(255, 255, 255));
        jblTurno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblTurno.setText("Turno: Jack Sparrow");
        jPanelHead.add(jblTurno);
        jblTurno.setBounds(235, 6, 412, 47);

        btnAbandonar.setBackground(new java.awt.Color(0, 166, 255));
        btnAbandonar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnAbandonar.setText("Abandonar");
        btnAbandonar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAbandonarMouseClicked(evt);
            }
        });
        jPanelHead.add(btnAbandonar);
        btnAbandonar.setBounds(20, 14, 142, 35);

        jblTiempo.setFont(new java.awt.Font("Monospaced", 1, 28)); // NOI18N
        jblTiempo.setForeground(new java.awt.Color(255, 255, 255));
        jblTiempo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblTiempo.setText("30");
        jPanelHead.add(jblTiempo);
        jblTiempo.setBounds(857, 7, 75, 47);

        jPanelFondo.add(jPanelHead);
        jPanelHead.setBounds(0, 0, 950, 60);

        jPanelNombreJugador2.setBackground(new java.awt.Color(255, 255, 255));
        jPanelNombreJugador2.setMinimumSize(new java.awt.Dimension(390, 32));

        jblNombreJugador2.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jblNombreJugador2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jblNombreJugador2.setText("Tablero de: Jack Sparrow");

        javax.swing.GroupLayout jPanelNombreJugador2Layout = new javax.swing.GroupLayout(jPanelNombreJugador2);
        jPanelNombreJugador2.setLayout(jPanelNombreJugador2Layout);
        jPanelNombreJugador2Layout.setHorizontalGroup(
            jPanelNombreJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugador2Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(jblNombreJugador2, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jPanelNombreJugador2Layout.setVerticalGroup(
            jPanelNombreJugador2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugador2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblNombreJugador2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFondo.add(jPanelNombreJugador2);
        jPanelNombreJugador2.setBounds(540, 90, 390, 32);

        jPanelNombreJugador.setBackground(new java.awt.Color(255, 255, 255));

        jblNombreJugador.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jblNombreJugador.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jblNombreJugador.setText("Tablero de: Jack Sparrow");

        javax.swing.GroupLayout jPanelNombreJugadorLayout = new javax.swing.GroupLayout(jPanelNombreJugador);
        jPanelNombreJugador.setLayout(jPanelNombreJugadorLayout);
        jPanelNombreJugadorLayout.setHorizontalGroup(
            jPanelNombreJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugadorLayout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jblNombreJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanelNombreJugadorLayout.setVerticalGroup(
            jPanelNombreJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugadorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblNombreJugador)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFondo.add(jPanelNombreJugador);
        jPanelNombreJugador.setBounds(20, 90, 390, 32);

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

        btnAtacar.setBackground(new java.awt.Color(0, 166, 255));
        btnAtacar.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnAtacar.setText("Atacar");
        btnAtacar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAtacarMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnAtacar);
        btnAtacar.setBounds(550, 490, 120, 35);

        jblFondo.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
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

    private void btnAbandonarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbandonarMouseClicked

        try {
            out.writeUTF("ABANDONAR");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();

    }//GEN-LAST:event_btnAbandonarMouseClicked

    private void btnAtacarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtacarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtacarMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbandonar;
    private javax.swing.JButton btnAtacar;
    private javax.swing.JPanel jPanelDerechos;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JPanel jPanelHead;
    private javax.swing.JPanel jPanelNombreJugador;
    private javax.swing.JPanel jPanelNombreJugador2;
    private javax.swing.JLabel jblDerechos;
    private javax.swing.JLabel jblFondo;
    private javax.swing.JLabel jblNombreJugador;
    private javax.swing.JLabel jblNombreJugador2;
    private javax.swing.JLabel jblTiempo;
    private javax.swing.JLabel jblTurno;
    // End of variables declaration//GEN-END:variables
}
