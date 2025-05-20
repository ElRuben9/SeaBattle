/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import BusEvent.AtaqueEvent;
import BusEvent.EventBus;
import BusEvent.Evento;
import BusEvent.FinJuegoEvent;
import BusEvent.RespuestaAtaqueEvent;
import Observer.ObservadorJuego;
import ViewModels.JuegoViewModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
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
public class PantallaJuego extends javax.swing.JFrame implements ObservadorJuego {

    private ViewModels.JuegoViewModel vmJuego;
    private JButton[][] botonesTablero = new JButton[10][10];

    private JButton[][] botonesTableroJugador = new JButton[10][10];
    private JButton[][] botonesTableroEnemigo = new JButton[10][10];

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean esMiTurno = false;

    private boolean esServidor;
    private boolean soyJugador1 = false;
    private Tablero tableroJugador;
    private Tablero tableroEnemigo;

    String fondo = "recursos/interfaz/fondoColocarBarcos.png";

    private int xSeleccionado = -1;
    private int ySeleccionado = -1;

    private EventBus.Subscription suscripcionAtaque;
    private EventBus.Subscription suscripcionRespuesta;
    private EventBus.Subscription suscripcionFinJuego;

    /**
     * Creates new form PantallaJuego
     */
    public PantallaJuego(Socket socket, boolean esServidor, Tablero tableroJugador, Tablero tableroDelOponente) {
        if (tableroJugador == null || tableroDelOponente == null) {
            throw new IllegalArgumentException("Los tableros no pueden ser nulos");
        }

        this.socket = socket;
        this.esServidor = esServidor;

        initComponents();
        // Primero se crea el ViewModel con los tableros

        this.tableroEnemigo = tableroDelOponente;
        this.tableroJugador = tableroJugador;
        this.vmJuego = new JuegoViewModel(tableroJugador, tableroEnemigo);
        this.vmJuego.agregarObservador(this);

        cargarDatos();
    }

    private void cargarDatos() {

        // Registra las suscripciones antes que cualquier publicación de evento.
        suscripcionAtaque = EventBus.suscribir(AtaqueEvent.class, this::onAtaqueRecibido);
        suscripcionRespuesta = EventBus.suscribir(RespuestaAtaqueEvent.class, this::onRespuestaAtaque);
        suscripcionFinJuego = EventBus.suscribir(FinJuegoEvent.class, this::onFinJuego);

        personazilarBotones();
        System.out.println("TABLERO JUGADOR:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(tableroJugador.hayBarcoEn(i, j) ? "B " : ". ");
            }
            System.out.println();
        }

        JPanel panelJugador = crearTablero(botonesTableroJugador, false);
        JPanel panelEnemigo = crearTablero(botonesTableroEnemigo, true);

        panelJugador.setBounds(jPanel1.getBounds());
        panelEnemigo.setBounds(jPanel2.getBounds());

        // El 0 es la prioridad al mostrarse.
        jPanelFondo.add(panelJugador, 0);
        jPanelFondo.add(panelEnemigo, 0);

        jPanelFondo.repaint();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Inicia la comunicación tras haber mostrado la UI.
        inicializarComunicacion();

        if (esServidor) {
            setTitle("Servidor - Batalla Naval");
            try {
                out.writeUTF("ERES_JUGADOR_1");
                out.flush();
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        } else {
            setTitle("Cliente - Batalla Naval");
            try {
                String mensajeInicial = in.readUTF();
                soyJugador1 = !mensajeInicial.equals("ERES_JUGADOR_1");
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
        }

        if (esServidor) {
            esMiTurno = true;
        }
        btnAtacar.setEnabled(esMiTurno); // Solo puede atacar si es su turno
        mostrarBarcosEnTablero();

    }

    private void onAtaqueRecibido(AtaqueEvent event) {

        // Registra el ataque y actualiza el estado interno
        boolean impacto = vmJuego.recibirAtaque(event.x, event.y);

        // Actualiza la UI en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            botonesTableroJugador[event.x][event.y].setBackground(impacto ? Color.RED : Color.BLUE);

            // Verifica en el EDT tras actualizar la UI y el modelo si todos los barcos están destruidos
            if (vmJuego.jugadorPerdio()) {

                EventBus.publicar(new FinJuegoEvent(false)); // Yo pierdo
                enviarMensaje("FIN_JUEGO:GANASTE"); // El otro gana
            }
        });

        EventBus.publicar(new RespuestaAtaqueEvent(event.x, event.y, impacto));
    }

    private void onRespuestaAtaque(RespuestaAtaqueEvent event) {
        if (!esMiTurno) {
            return;
        }
        SwingUtilities.invokeLater(() -> {
            botonesTableroEnemigo[event.x][event.y]
                    .setBackground(event.impacto ? Color.RED : Color.BLUE);

            // Si fue impacto, revisa si hundiste todos los barcos enemigos
            if (event.impacto) {

                if (tableroEnemigo.todosLosBarcosDestruidos()) {
                    // Le avisas al otro jugador que perdió
                    try {
                        out.writeUTF("HE_PERDIDO");
                        out.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    // Publicas el evento local de victoria
                    EventBus.publicar(new FinJuegoEvent(true));
                    return;
                }
                // Si no terminó el juego, te quedas el turno
                JOptionPane.showMessageDialog(this, "¡Impacto! Puedes volver a disparar.");
                esMiTurno = true;
                btnAtacar.setEnabled(true);
            } else {
                // Fallaste: cedes el turno
                esMiTurno = false;
                btnAtacar.setEnabled(false);
                try {
                    out.writeUTF("TU_TURNO");
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void onFinJuego(FinJuegoEvent event) {
        SwingUtilities.invokeLater(() -> {
          
            suscripcionAtaque.cancel();
            suscripcionRespuesta.cancel();
            suscripcionFinJuego.cancel();
        });
    }

    private void mostrarBarcosEnTablero() {
        // Recorremos el tablero del jugador y actualizamos el color de los botones.
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (tableroJugador.hayBarcoEn(i, j)) {
                    botonesTableroJugador[i][j].setBackground(Color.DARK_GRAY); // Color para las casillas donde hay barcos.
                } else {
                    botonesTableroJugador[i][j].setBackground(Color.GRAY); // Color para las casillas vacías.
                }
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                botonesTableroEnemigo[i][j].setBackground(Color.LIGHT_GRAY);
            }
        }

    }

    private JPanel crearTablero(JButton[][] botones, boolean esTableroEnemigo) {
        JPanel panel = new JPanel(new GridLayout(10, 10));

        if (esTableroEnemigo) {
            panel.setBounds(jPanel2.getBounds());
        } else {
            panel.setBounds(jPanel1.getBounds());
        }
        // panel.setPreferredSize(new Dimension(400, 400));  // Tamaño del tablero
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                JButton boton = new JButton();
                boton.setPreferredSize(new Dimension(40, 40)); // Tamaño de cada celda
                boton.setBackground(Color.GRAY);
                if (esTableroEnemigo) {
                    boton.setBackground(Color.LIGHT_GRAY);
                    int finalX = x, finalY = y;
                    boton.addActionListener(e -> {

                        if (!esMiTurno) {
                            JOptionPane.showMessageDialog(this, "¡Espera tu turno!");
                            return;
                        }

                        limpiarSeleccionAnterior();
                        xAnteriorSeleccionado = finalX;
                        yAnteriorSeleccionado = finalY;
                        xSeleccionado = finalX;
                        ySeleccionado = finalY;
                        botonesTableroEnemigo[finalX][finalY].setBackground(Color.YELLOW);

                        xSeleccionado = finalX;
                        ySeleccionado = finalY;
                        System.out.println(finalX);

                        // Opcional: resaltar visualmente la celda seleccionada
                        botonesTableroEnemigo[finalX][finalY].setBackground(Color.YELLOW);
                    });
                }
                botones[x][y] = boton;
                panel.add(boton);
            }
        }
        return panel;
    }

    private void inicializarComunicacion() {
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // Hilo para escuchar al oponente
            new Thread(() -> {
                try {
                    while (true) {
                        String mensaje = in.readUTF();
                        procesarMensajeRecibido(mensaje);
                    }
                } catch (IOException e) {
                    System.err.println("Conexión cerrada: " + e.getMessage());
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarMensaje(String mensaje) {
        try {
            out.writeUTF(mensaje);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarMensajeRecibido(String mensaje) {
        System.out.println("Mensaje recibido: " + mensaje);

        if (mensaje.startsWith("ATAQUE:")) {
            String[] partes = mensaje.substring(7).split(",");
            int x = Integer.parseInt(partes[0]);
            int y = Integer.parseInt(partes[1]);
            EventBus.publicar(new AtaqueEvent(x, y));

            boolean impacto = tableroJugador.hayBarcoEn(x, y);

            try {
                String respuesta = "RESPUESTA:" + x + "," + y + "," + (impacto ? "IMPACTO" : "AGUA");
                out.writeUTF(respuesta);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Retrasar la verificación para que se refleje el impacto en el tablero
            SwingUtilities.invokeLater(() -> {
                System.out.println("¿Todos los barcos destruidos? " + tableroJugador.todosLosBarcosDestruidos());
                if (tableroJugador.todosLosBarcosDestruidos()) {
                    try {
                        out.writeUTF("HE_PERDIDO");
                        out.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    terminarJuego(false);
                }
            });

        } else if (mensaje.startsWith("RESPUESTA:")) {
            String[] partes = mensaje.substring(10).split(",");
            int x = Integer.parseInt(partes[0]);
            int y = Integer.parseInt(partes[1]);
            String resultado = partes[2];
            boolean impacto = resultado.equals("IMPACTO");

            EventBus.publicar(new RespuestaAtaqueEvent(x, y, impacto));

            if (impacto) {
                esMiTurno = true;
                btnAtacar.setEnabled(true);
                SwingUtilities.invokeLater(()
                        -> JOptionPane.showMessageDialog(this, "¡Impacto! Puedes volver a disparar.")
                );
            } else {
                esMiTurno = false;
                btnAtacar.setEnabled(false);
                try {
                    out.writeUTF("TU_TURNO");
                    out.flush();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } else if (mensaje.equals("HE_PERDIDO")) {
            // El oponente informa que ha perdido
            terminarJuego(true);
            enviarMensaje("FIN_JUEGO:GANASTE"); // Informo al otro que ganó
        } else if (mensaje.equals("TU_TURNO")) {
            esMiTurno = true;
            btnAtacar.setEnabled(true);
            SwingUtilities.invokeLater(()
                    -> JOptionPane.showMessageDialog(this, "¡Tu turno!")
            );
        } else if (mensaje.startsWith("FIN_JUEGO:")) {
            String resultado = mensaje.substring("FIN_JUEGO:".length());
            boolean gane = resultado.equals("GANASTE");
            terminarJuego(gane);
        }
    }

    private void terminarJuego(boolean gane) {
        // Publicamos el evento de fin de juego, confiando en que la suscripción ya se realizó
        EventBus.publicar(new FinJuegoEvent(gane));

        SwingUtilities.invokeLater(() -> {
            String mensaje = gane ? "¡Ganaste!" : "¡Has perdido! Todos tus barcos han sido destruidos.";
            JOptionPane.showMessageDialog(this, mensaje);
            // Cierra la ventana y cancela las suscripciones
            this.dispose();
            suscripcionAtaque.cancel();
            suscripcionRespuesta.cancel();
            suscripcionFinJuego.cancel();
            dispose();
        });

    }

    @Override
    public void dispose() {
        vmJuego.quitarObservador(this);
        super.dispose();
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

    private int xAnteriorSeleccionado = -1;
    private int yAnteriorSeleccionado = -1;

    private void limpiarSeleccionAnterior() {
        if (xAnteriorSeleccionado != -1 && yAnteriorSeleccionado != -1) {
            Color colorDefault = new Color(173, 216, 230); // o Color.LIGHT_GRAY si así estaba
            botonesTableroEnemigo[xAnteriorSeleccionado][yAnteriorSeleccionado].setBackground(colorDefault);
        }
    }

    private void disparar(int x, int y) {
        try {
            String mensaje = "ATAQUE:" + x + "," + y;

            out.writeUTF(mensaje);
            out.flush();
            esMiTurno = false;
            botonesTableroEnemigo[x][y].setBackground(Color.GRAY); // Marca intento de ataque

            boolean impacto = tableroJugador.hayBarcoEn(x, y);
            System.out.println("Ataque recibido en (" + x + "," + y + ") - ¿Hay barco? " + impacto);

        } catch (IOException e) {
            e.printStackTrace();
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
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
        jPanelHead.setBounds(0, 0, 950, 70);

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        jPanelFondo.add(jPanel1);
        jPanel1.setBounds(20, 160, 390, 290);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 290, Short.MAX_VALUE)
        );

        jPanelFondo.add(jPanel2);
        jPanel2.setBounds(540, 160, 390, 290);

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

    private void btnAtacarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAtacarMouseClicked
        // TODO add your handling code here:
        if (esMiTurno && xSeleccionado != -1 && ySeleccionado != -1) {
            disparar(xSeleccionado, ySeleccionado);
            xSeleccionado = -1;
            ySeleccionado = -1;
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una casilla antes de atacar o espera tu turno.");
        }

    }//GEN-LAST:event_btnAtacarMouseClicked

    private void btnAbandonarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAbandonarMouseClicked

        try {
            out.writeUTF("ABANDONAR");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        dispose();
    }//GEN-LAST:event_btnAbandonarMouseClicked

    @Override
    public void onCambioTurno(boolean esTurnoJugador1) {
        SwingUtilities.invokeLater(() -> {
            esMiTurno = (esTurnoJugador1 == soyJugador1);

            jblTurno.setText(esMiTurno ? "¡Es tu turno!" : "Turno del enemigo...");

        });
    }

    @Override
    public void onImpacto(int x, int y, boolean acierto, boolean esAtaqueEntrante) {
        SwingUtilities.invokeLater(() -> {
            JButton boton;
            if (esAtaqueEntrante) {
                boton = botonesTableroJugador[x][y];
            } else {
                boton = botonesTableroEnemigo[x][y];
            }

            if (acierto) {
                boton.setBackground(Color.RED);
                boton.setText("X");
            } else {
                boton.setBackground(Color.BLUE);
                boton.setText("O");
            }

            boton.setEnabled(false);
        });
    }

    @Override
    public void onFinJuego(String ganador) {
        SwingUtilities.invokeLater(() -> {
            boolean yoGane = (ganador.equals("Jugador 1") && soyJugador1)
                    || (ganador.equals("Jugador 2") && !soyJugador1);

            String mensaje = yoGane ? "¡Ganaste la partida!" : "Has perdido...";
            JOptionPane.showMessageDialog(this, mensaje, "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAbandonar;
    private javax.swing.JButton btnAtacar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
