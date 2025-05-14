/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

import java.awt.Color;
import utilerias.BotonPersonalizado;
import utilerias.PanelTransparente;
import utilerias.PersonalizacionGeneral;
import negocio.*;
import DAOs.JuegoDAO;
import Pantallas.PantallaJuego;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author ruben
 */
public class PantallaAsignacion extends javax.swing.JFrame {

    private PantallaJuego nuevaPantallaJuego;
    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    private String nombre;
    private String colorString;
    private Color colorJugador;

    private boolean esServidor;
    private boolean esJugador1;
    private boolean yoListo = false;
    private boolean oponenteListo = false;

    private Tablero tableroJugador;
    private Tablero tableroOponente;
    private JButton[][] botonesTablero = new JButton[10][10];
    private String orientacionActual = "horizontal";
    private TipoBarco tipoSeleccionado = TipoBarco.BARCO;
    private Barco barcoActual;

    private final Map<TipoBarco, Integer> maximos = Map.of(
            TipoBarco.BARCO, 4,
            TipoBarco.CRUCERO, 3,
            TipoBarco.SUBMARINO, 2,
            TipoBarco.PORTAAVIONES, 1
    );

    private final Map<TipoBarco, Integer> colocados = new HashMap<>();

    private ViewModels.AsignacionViewModel vmAsignacion;
    private PantallaEscogerPartida escoger;
    private final String fondo = "recursos/interfaz/fondoPantallaPartida.png";

    /**
     * Creates new form PantallaAsignacion
     *
     * @param escoger
     */
    public PantallaAsignacion(PantallaEscogerPartida escoger, boolean esServidor) {
        initComponents();

        this.escoger = escoger;
        this.esServidor = esServidor;
        this.esJugador1 = esServidor;

        tableroJugador = new Tablero();
        tableroOponente = new Tablero();
        for (TipoBarco tipo : TipoBarco.values()) {
            colocados.put(tipo, 0);
        }

        cargarInterfaz();
        actualizarContadores();

        try {
            jblConfiguracion1.setText("ID partida: " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException ex) {
            Logger.getLogger(PantallaAsignacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            jblConfiguracion1.setText("ID partida: " + socket.getInetAddress().getHostAddress());
            hiloTodosListos();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void esperarConexionDelOponente(ServerSocket servidor) {
        new Thread(() -> {
            try {
                socket = servidor.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String nombreOponente = in.readLine();
                String segundoMensaje = in.readLine();

                txtNombreOponente3.setText(nombreOponente);

                // Verificar si el segundo mensaje es "LISTO" o un color
                if ("LISTO".equals(segundoMensaje)) {
                    // El oponente ya está listo, pero aún no tenemos su color
                    jPanelColorOponente.setBackground(Color.GRAY); // Color provisional
                    esperarListoYContinuar();
                } else if ("AMBOS_LISTOS".equals(segundoMensaje)) {
                    // Si el mensaje es "AMBOS_LISTOS", ya no procesamos como color
                    esperarListoYContinuar();
                } else {
                    // El segundo mensaje es un color en formato hexadecimal
                    try {
                        jPanelColorOponente.setBackground(Color.decode(segundoMensaje));
                    } catch (NumberFormatException ex) {
                        // Maneja el caso donde el color no es válido
                        System.out.println("Error al decodificar color: " + segundoMensaje);
                        jPanelColorOponente.setBackground(Color.GRAY); // o el color que desees
                    }

                    esperarListoYContinuar();
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void esperarListoYContinuar() {
        new Thread(() -> {
            try {
                while (true) {
                    String recibido = in.readLine();
                    if ("LISTO".equals(recibido)) {
                        oponenteListo = true;
                        if (yoListo) {
                            out.println("AMBOS_LISTOS");
                            out.flush();
                            SwingUtilities.invokeLater(() -> iniciarJuego(tableroJugador, tableroOponente));
                            break;
                        }
                    } else if ("AMBOS_LISTOS".equals(recibido)) {
                        SwingUtilities.invokeLater(() -> iniciarJuego(tableroJugador, tableroOponente));
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void iniciarJuego(Tablero miTablero, Tablero tableroOponente) {
        if (nuevaPantallaJuego == null) {
            nuevaPantallaJuego = new PantallaJuego(socket, esJugador1, miTablero, tableroOponente);
        }

        this.setVisible(false);
        nuevaPantallaJuego.setVisible(true);
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private void actualizarContadores() {
        txtContadorBarco.setText(colocados.get(TipoBarco.BARCO) + "/" + maximos.get(TipoBarco.BARCO));
        txtContadorCrucero.setText(colocados.get(TipoBarco.CRUCERO) + "/" + maximos.get(TipoBarco.CRUCERO));
        txtContadorSubmarino.setText(colocados.get(TipoBarco.SUBMARINO) + "/" + maximos.get(TipoBarco.SUBMARINO));
        txtContadorPortaAviones.setText(colocados.get(TipoBarco.PORTAAVIONES) + "/" + maximos.get(TipoBarco.PORTAAVIONES));
    }

    private void cargarInterfaz() {
        PersonalizacionGeneral.colocarImagenLabel(jblFondo, fondo);

        vmAsignacion = new ViewModels.AsignacionViewModel();
        crearTablero();

        btnConfirmar.setEnabled(false);
        btnConfirmar.setVisible(false);

        cargarDatosUsuario();
    }

    private void cargarDatosUsuario() {
        try (BufferedReader reader = new BufferedReader(new FileReader("jugador.txt"))) {
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

            jblNombreJugador.setText("Tablero de: " + nombre);
            asignarColorJugador(colorString);

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
    }

    private void asignarColorJugador(String colorString) {
        switch (colorString) {
            case "0,0,255":
                colorJugador = new Color(0, 0, 255);
                break;
            case "51,255,51":
                colorJugador = new Color(51, 255, 51);
                break;
            case "204,0,204":
                colorJugador = new Color(204, 0, 204);
                break;
            case "0,255,255":
                colorJugador = new Color(0, 255, 255);
                break;
            default:
                colorJugador = Color.GRAY;
                break;
        }
    }

    private void hiloTodosListos() {
        new Thread(() -> {
            try {
                while (true) {
                    String mensaje = in.readLine();
                    if ("LISTO".equals(mensaje)) {
                        String tableroRecibido = in.readLine();

                        Tablero tableroEnemigo = new Tablero();
                        tableroEnemigo.deserializarBarcos(tableroRecibido);

                        oponenteListo = true;
                        if (yoListo) {
                            out.println("AMBOS_LISTOS");
                            out.flush();
                            SwingUtilities.invokeLater(() -> iniciarJuego(tableroJugador, tableroEnemigo));
                            break;
                        }

                    } else if ("AMBOS_LISTOS".equals(mensaje)) {
                        SwingUtilities.invokeLater(() -> iniciarJuego(tableroJugador, tableroOponente));
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void colocarBarco(int x, int y) {

        if (tipoSeleccionado.equals(TipoBarco.BARCO) && colocados.get(TipoBarco.BARCO) == 4) {
            System.out.println(colocados.get(TipoBarco.BARCO));
            JOptionPane.showMessageDialog(this, "Maximo numero de barcos alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoSeleccionado.equals(TipoBarco.CRUCERO) && colocados.get(TipoBarco.CRUCERO) == 3) {
            JOptionPane.showMessageDialog(this, "Maximo numero de cruceros alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoSeleccionado.equals(TipoBarco.SUBMARINO) && colocados.get(TipoBarco.SUBMARINO) == 2) {
            JOptionPane.showMessageDialog(this, "Maximo numero de submarinos alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tipoSeleccionado.equals(TipoBarco.PORTAAVIONES) && colocados.get(TipoBarco.PORTAAVIONES) == 1) {
            JOptionPane.showMessageDialog(this, "Maximo numero de portaviones alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (orientacionActual.equals("horizontal") && x + barcoActual.getTamaño() > 10) {
            JOptionPane.showMessageDialog(this, "El barco no cabe aquí.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } else if (orientacionActual.equals("vertical") && y + barcoActual.getTamaño() > 10) {
            JOptionPane.showMessageDialog(this, "El barco no cabe aquí.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < barcoActual.getTamaño(); i++) {
            if (orientacionActual.equals("horizontal") && botonesTablero[x + i][y].getBackground().equals(colorJugador)) {
                JOptionPane.showMessageDialog(this, "El espacio ya esta ocupado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (orientacionActual.equals("vertical") && botonesTablero[x][y + i].getBackground().equals(colorJugador)) {
                JOptionPane.showMessageDialog(this, "El espacio ya esta ocupado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Si el barco cabe, lo colocamos
        Barco nuevoBarco = new Barco(tipoSeleccionado);
        nuevoBarco.setOrientacion(orientacionActual.equals("horizontal") ? Orientacion.HORIZONTAL : Orientacion.VERTICAL);

        boolean colocado = tableroJugador.colocarBarco(nuevoBarco, x, y);

        System.out.println("Intentando colocar barco: " + barcoActual.getTipo() + " en " + x + "," + y);

        if (colocado) {
            // Aumentar contador
            colocados.put(barcoActual.getTipo(), colocados.get(barcoActual.getTipo()) + 1);
            actualizarContadores();
           System.out.println("Colocando barco " + nuevoBarco.getTipo() + " en (" + x + "," + y + ") - hashCode: " + nuevoBarco.hashCode());


            // Pintar las celdas
            for (int i = 0; i < barcoActual.getTamaño(); i++) {
                if (orientacionActual.equals("horizontal")) {
                    botonesTablero[x + i][y].setBackground(colorJugador);
                } else {
                    botonesTablero[x][y + i].setBackground(colorJugador);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo colocar el barco aquí", "Error", JOptionPane.ERROR_MESSAGE);
        }
// Verifica si ya colocó todos los barcos
        boolean todosListos = colocados.entrySet().stream()
                .allMatch(entry -> entry.getValue().equals(maximos.get(entry.getKey())));

        btnConfirmar.setEnabled(todosListos);  // Habilita el botón solo si todo está listo
        btnConfirmar.setVisible(todosListos);

    }

    // Suponiendo que ya tienes los objetos barcoActual y tableroJugador
    private void crearTablero() {
        JPanel panelGrid = new JPanel(new java.awt.GridLayout(10, 10));
        panelGrid.setBounds(20, 170, 355, 325);
        panelGrid.setOpaque(false);

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JButton boton = new JButton();
                boton.setBackground(new Color(173, 216, 230));
                boton.setFocusPainted(false);
                int x = i;
                int y = j;

                // Cambiar el ActionListener para incluir la llamada a colocarBarco
                boton.addActionListener(e -> {
                    if (barcoActual != null) {
                        // Llamamos a colocarBarco pasando solo el barcoActual y las coordenadas
                        colocarBarco(x, y);

                    } else {
                        System.out.println("No se ha seleccionado un barco.");
                    }
                });

                // Listener para cambiar color al pasar el mouse
                boton.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        if (barcoActual != null) {

                            if (orientacionActual.equals("horizontal") && x + barcoActual.getTamaño() > 10) {
                                return;
                            } else if (orientacionActual.equals("vertical") && y + barcoActual.getTamaño() > 10) {
                                return;
                            }

                            for (int i = 0; i < barcoActual.getTamaño(); i++) {
                                try {
                                    if (orientacionActual.equals("horizontal")) {
                                        if (botonesTablero[x + i][y].getBackground().equals(colorJugador)) {
                                            return;
                                        }
                                        botonesTablero[x + i][y].setBackground(Color.LIGHT_GRAY);

                                    } else {
                                        if (botonesTablero[x][y + i].getBackground().equals(colorJugador)) {
                                            return;
                                        }
                                        botonesTablero[x][y + i].setBackground(Color.LIGHT_GRAY);
                                    }
                                } catch (Exception ex) {

                                    botonesTablero[x + i][y].setBackground(new Color(173, 216, 230));
                                }
                            }
                        }
                    }

                    public void mouseExited(MouseEvent e) {

                        if (barcoActual != null) {

                            if (orientacionActual.equals("horizontal") && x + barcoActual.getTamaño() > 10) {
                                return;
                            } else if (orientacionActual.equals("vertical") && y + barcoActual.getTamaño() > 10) {
                                return;
                            }

                            for (int i = 0; i < barcoActual.getTamaño(); i++) {
                                try {
                                    if (orientacionActual.equals("horizontal")) {
                                        if (botonesTablero[x + i][y].getBackground().equals(colorJugador)) {
                                            return;
                                        }
                                        botonesTablero[x + i][y].setBackground(new Color(173, 216, 230));

                                    } else {
                                        if (botonesTablero[x][y + i].getBackground().equals(colorJugador)) {
                                            return;
                                        }
                                        botonesTablero[x][y + i].setBackground(new Color(173, 216, 230));
                                    }
                                } catch (Exception ex) {
                                    botonesTablero[x + i][y].setBackground(new Color(173, 216, 230));
                                }
                            }
                        }
                    }

                });

                panelListaBarcos.add(boton); // Ese es el panel donde dice "Tipos de nave"
                botonesTablero[i][j] = boton;
                panelGrid.add(boton);
            }
        }

        this.add(panelGrid, 0);
    }

    private void personazilarBotones() {

        btnConfirmar.setContentAreaFilled(false);
        btnConfirmar.setBorderPainted(false);
        btnConfirmar.setOpaque(false);
        btnConfirmar.setUI(new BotonPersonalizado(25, new Color(21, 255, 0), new Color(21, 255, 168), 3));

        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setOpaque(false);
        btnVolver.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255), 3));

        btnGirar.setContentAreaFilled(false);
        btnGirar.setBorderPainted(false);
        btnGirar.setOpaque(false);
        btnGirar.setUI(new BotonPersonalizado(25, new Color(0, 166, 255), new Color(82, 250, 255), 3));

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
        jblConfiguracion1 = new javax.swing.JLabel();
        btnVolver = new javax.swing.JButton();
        txtNombreOponente3 = new javax.swing.JLabel();
        jPanelColorOponente = new javax.swing.JPanel();
        txtNombreOponente = new javax.swing.JLabel();
        jPanelTiposNaves = new PanelTransparente((float) 0.35);
        jblTiposNave = new javax.swing.JLabel();
        jPanelNombreJugador = new PanelTransparente((float) 0.35);
        jblNombreJugador = new javax.swing.JLabel();
        jPanelEsperando = new PanelTransparente((float) 0.35);
        jblEsperando = new javax.swing.JLabel();
        jPanelDerechos = new javax.swing.JPanel();
        jblDerechos = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
        btnGirar = new javax.swing.JButton();
        panelListaBarcos = new javax.swing.JPanel();
        txtPortaAviones = new javax.swing.JLabel();
        txtContadorPortaAviones = new javax.swing.JLabel();
        txtSubmarino = new javax.swing.JLabel();
        txtContadorSubmarino = new javax.swing.JLabel();
        txtBarco = new javax.swing.JLabel();
        txtContadorBarco = new javax.swing.JLabel();
        txtCrucero = new javax.swing.JLabel();
        txtContadorCrucero = new javax.swing.JLabel();
        seleccionarPortaAviones = new javax.swing.JButton();
        seleccionarSubmarino = new javax.swing.JButton();
        seleccionarBarco = new javax.swing.JButton();
        seleccionarCrucero = new javax.swing.JButton();
        jblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Asignar Barcos");

        jPanelFondo.setLayout(null);

        jPanelHead.setBackground(new java.awt.Color(13, 26, 51));
        jPanelHead.setLayout(null);

        jblConfiguracion1.setFont(new java.awt.Font("Monospaced", 0, 22)); // NOI18N
        jblConfiguracion1.setForeground(new java.awt.Color(255, 255, 255));
        jblConfiguracion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblConfiguracion1.setText("ID partida: 172.72.82...");
        jPanelHead.add(jblConfiguracion1);
        jblConfiguracion1.setBounds(235, 6, 412, 47);

        btnVolver.setBackground(new java.awt.Color(0, 166, 255));
        btnVolver.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        btnVolver.setText("Volver");
        btnVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVolverMouseClicked(evt);
            }
        });
        jPanelHead.add(btnVolver);
        btnVolver.setBounds(21, 14, 120, 35);

        txtNombreOponente3.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        txtNombreOponente3.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreOponente3.setText("Jugador 2");
        jPanelHead.add(txtNombreOponente3);
        txtNombreOponente3.setBounds(750, 10, 190, 20);

        jPanelColorOponente.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanelColorOponenteLayout = new javax.swing.GroupLayout(jPanelColorOponente);
        jPanelColorOponente.setLayout(jPanelColorOponenteLayout);
        jPanelColorOponenteLayout.setHorizontalGroup(
            jPanelColorOponenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanelColorOponenteLayout.setVerticalGroup(
            jPanelColorOponenteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanelHead.add(jPanelColorOponente);
        jPanelColorOponente.setBounds(710, 30, 30, 30);

        txtNombreOponente.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        txtNombreOponente.setForeground(new java.awt.Color(255, 255, 255));
        txtNombreOponente.setText("Sin oponente");
        jPanelHead.add(txtNombreOponente);
        txtNombreOponente.setBounds(750, 35, 190, 20);

        jPanelFondo.add(jPanelHead);
        jPanelHead.setBounds(0, 0, 950, 70);

        jPanelTiposNaves.setBackground(new java.awt.Color(255, 255, 255));

        jblTiposNave.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jblTiposNave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jblTiposNave.setText("Tipos de nave");

        javax.swing.GroupLayout jPanelTiposNavesLayout = new javax.swing.GroupLayout(jPanelTiposNaves);
        jPanelTiposNaves.setLayout(jPanelTiposNavesLayout);
        jPanelTiposNavesLayout.setHorizontalGroup(
            jPanelTiposNavesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTiposNavesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblTiposNave, javax.swing.GroupLayout.PREFERRED_SIZE, 343, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanelTiposNavesLayout.setVerticalGroup(
            jPanelTiposNavesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTiposNavesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblTiposNave)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFondo.add(jPanelTiposNaves);
        jPanelTiposNaves.setBounds(500, 130, 360, 32);

        jPanelNombreJugador.setBackground(new java.awt.Color(255, 255, 255));

        jblNombreJugador.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        jblNombreJugador.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jblNombreJugador.setText("Tablero de: Jack Sparrow");

        javax.swing.GroupLayout jPanelNombreJugadorLayout = new javax.swing.GroupLayout(jPanelNombreJugador);
        jPanelNombreJugador.setLayout(jPanelNombreJugadorLayout);
        jPanelNombreJugadorLayout.setHorizontalGroup(
            jPanelNombreJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugadorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblNombreJugador, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanelNombreJugadorLayout.setVerticalGroup(
            jPanelNombreJugadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelNombreJugadorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblNombreJugador)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFondo.add(jPanelNombreJugador);
        jPanelNombreJugador.setBounds(20, 130, 360, 32);

        jPanelEsperando.setBackground(new java.awt.Color(255, 255, 255));

        jblEsperando.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        jblEsperando.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jblEsperando.setText("Esperando Jugadores");

        javax.swing.GroupLayout jPanelEsperandoLayout = new javax.swing.GroupLayout(jPanelEsperando);
        jPanelEsperando.setLayout(jPanelEsperandoLayout);
        jPanelEsperandoLayout.setHorizontalGroup(
            jPanelEsperandoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEsperandoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblEsperando, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );
        jPanelEsperandoLayout.setVerticalGroup(
            jPanelEsperandoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelEsperandoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jblEsperando, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelFondo.add(jPanelEsperando);
        jPanelEsperando.setBounds(510, 470, 360, 50);

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

        btnConfirmar.setBackground(new java.awt.Color(0, 166, 255));
        btnConfirmar.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnConfirmar.setText("Confirmar");
        btnConfirmar.setEnabled(false);
        btnConfirmar.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        btnConfirmar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnConfirmarMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnConfirmar);
        btnConfirmar.setBounds(750, 415, 120, 40);

        btnGirar.setBackground(new java.awt.Color(0, 166, 255));
        btnGirar.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnGirar.setText("Vertical");
        btnGirar.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnGirar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnGirarMouseClicked(evt);
            }
        });
        jPanelFondo.add(btnGirar);
        btnGirar.setBounds(510, 415, 120, 40);

        txtPortaAviones.setText("Porta Aviones");

        txtContadorPortaAviones.setText("0x");

        txtSubmarino.setText("Submarino");

        txtContadorSubmarino.setText("0x");

        txtBarco.setText("Barco");

        txtContadorBarco.setText("0/4");

        txtCrucero.setText("Crucero");

        txtContadorCrucero.setText("0x");

        seleccionarPortaAviones.setText("Seleccionar");
        seleccionarPortaAviones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarPortaAvionesActionPerformed(evt);
            }
        });

        seleccionarSubmarino.setText("Seleccionar");
        seleccionarSubmarino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarSubmarinoActionPerformed(evt);
            }
        });

        seleccionarBarco.setText("Seleccionar");
        seleccionarBarco.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarBarcoActionPerformed(evt);
            }
        });

        seleccionarCrucero.setText("Seleccionar");
        seleccionarCrucero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarCruceroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelListaBarcosLayout = new javax.swing.GroupLayout(panelListaBarcos);
        panelListaBarcos.setLayout(panelListaBarcosLayout);
        panelListaBarcosLayout.setHorizontalGroup(
            panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(17, Short.MAX_VALUE)
                    .addComponent(txtPortaAviones)
                    .addContainerGap(260, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addGap(82, 82, 82)
                    .addComponent(txtContadorPortaAviones, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(237, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(226, Short.MAX_VALUE)
                    .addComponent(txtSubmarino)
                    .addContainerGap(66, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(272, Short.MAX_VALUE)
                    .addComponent(txtContadorSubmarino, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(43, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(37, Short.MAX_VALUE)
                    .addComponent(txtBarco)
                    .addContainerGap(283, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(69, Short.MAX_VALUE)
                    .addComponent(txtContadorBarco, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(250, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(242, Short.MAX_VALUE)
                    .addComponent(txtCrucero)
                    .addContainerGap(66, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(272, Short.MAX_VALUE)
                    .addComponent(txtContadorCrucero, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(45, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(13, Short.MAX_VALUE)
                    .addComponent(seleccionarPortaAviones)
                    .addContainerGap(247, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addGap(209, 209, 209)
                    .addComponent(seleccionarSubmarino)
                    .addContainerGap(51, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(24, Short.MAX_VALUE)
                    .addComponent(seleccionarBarco)
                    .addGap(236, 236, 236)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addGap(203, 203, 203)
                    .addComponent(seleccionarCrucero, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(45, Short.MAX_VALUE)))
        );
        panelListaBarcosLayout.setVerticalGroup(
            panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 190, Short.MAX_VALUE)
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(19, Short.MAX_VALUE)
                    .addComponent(txtPortaAviones)
                    .addContainerGap(155, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtContadorPortaAviones)
                    .addContainerGap(168, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(17, Short.MAX_VALUE)
                    .addComponent(txtSubmarino)
                    .addContainerGap(157, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(txtContadorSubmarino)
                    .addContainerGap(168, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(129, Short.MAX_VALUE)
                    .addComponent(txtBarco)
                    .addContainerGap(45, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(115, Short.MAX_VALUE)
                    .addComponent(txtContadorBarco)
                    .addContainerGap(59, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(129, Short.MAX_VALUE)
                    .addComponent(txtCrucero)
                    .addContainerGap(45, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(112, Short.MAX_VALUE)
                    .addComponent(txtContadorCrucero)
                    .addContainerGap(62, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(48, Short.MAX_VALUE)
                    .addComponent(seleccionarPortaAviones)
                    .addContainerGap(119, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelListaBarcosLayout.createSequentialGroup()
                    .addGap(49, 49, 49)
                    .addComponent(seleccionarSubmarino)
                    .addContainerGap(118, Short.MAX_VALUE)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(153, Short.MAX_VALUE)
                    .addComponent(seleccionarBarco)
                    .addGap(14, 14, 14)))
            .addGroup(panelListaBarcosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelListaBarcosLayout.createSequentialGroup()
                    .addContainerGap(153, Short.MAX_VALUE)
                    .addComponent(seleccionarCrucero)
                    .addGap(14, 14, 14)))
        );

        jPanelFondo.add(panelListaBarcos);
        panelListaBarcos.setBounds(500, 170, 350, 190);

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

    private void btnConfirmarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnConfirmarMouseClicked
        if (socket == null || out == null) {
            JOptionPane.showMessageDialog(this, "Aún no estás conectado con el otro jugador.");
            return;
        }

        yoListo = true;
        out.println("LISTO");
        out.println(tableroJugador.serializarBarcos());
        out.flush();

        if (oponenteListo) {
            out.println("AMBOS_LISTOS");
            out.flush();
            SwingUtilities.invokeLater(() -> iniciarJuego(tableroJugador, tableroOponente));
        } else {
            JOptionPane.showMessageDialog(this, "Esperando al oponente...");
        }


    }//GEN-LAST:event_btnConfirmarMouseClicked
    private void limpiarTableroGUI() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                botonesTablero[i][j].setBackground(new Color(173, 216, 230)); // Azul claro
                botonesTablero[i][j].setEnabled(true);
            }
        }
    }

    private boolean validarPosicion(Barco barco, int x, int y) {
        for (int i = 0; i < barco.getTamaño(); i++) {
            if (orientacionActual.equals("horizontal") && botonesTablero[x + i][y].getBackground() == Color.GRAY) {
                return false;
            } else if (orientacionActual.equals("vertical") && botonesTablero[x][y + i].getBackground() == Color.GRAY) {
                return false;
            }
        }
        return true;
    }

    private void btnGirarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnGirarMouseClicked
        if (orientacionActual.equals("horizontal")) {
            orientacionActual = "vertical";
            btnGirar.setBackground(Color.RED);
            btnGirar.setText("Horizontal");
        } else {
            orientacionActual = "horizontal";
            btnGirar.setBackground(Color.GREEN);
            btnGirar.setText("Vertical");
        }
    }//GEN-LAST:event_btnGirarMouseClicked

    private void btnVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVolverMouseClicked
        // TODO add your handling code here:
        escoger.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVolverMouseClicked

    private void seleccionarPortaAvionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarPortaAvionesActionPerformed
        if (colocados.get(TipoBarco.PORTAAVIONES) < maximos.get(TipoBarco.PORTAAVIONES)) {
            tipoSeleccionado = TipoBarco.PORTAAVIONES;
            barcoActual = new Barco(tipoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, "Ya colocaste todos los Portaaviones.");
        }
    }//GEN-LAST:event_seleccionarPortaAvionesActionPerformed

    private void seleccionarSubmarinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarSubmarinoActionPerformed
        if (colocados.get(TipoBarco.SUBMARINO) < maximos.get(TipoBarco.SUBMARINO)) {
            tipoSeleccionado = TipoBarco.SUBMARINO;
            barcoActual = new Barco(tipoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, "Ya colocaste todos los Submarinos.");
        }
    }//GEN-LAST:event_seleccionarSubmarinoActionPerformed

    private void seleccionarBarcoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarBarcoActionPerformed
        if (colocados.get(TipoBarco.BARCO) < maximos.get(TipoBarco.BARCO)) {
            tipoSeleccionado = TipoBarco.BARCO;
            barcoActual = new Barco(tipoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, "Ya colocaste todas las Lanchas.");
        }
    }//GEN-LAST:event_seleccionarBarcoActionPerformed

    private void seleccionarCruceroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarCruceroActionPerformed
        if (colocados.get(TipoBarco.CRUCERO) < maximos.get(TipoBarco.CRUCERO)) {
            tipoSeleccionado = TipoBarco.CRUCERO;
            barcoActual = new Barco(tipoSeleccionado);
        } else {
            JOptionPane.showMessageDialog(this, "Ya colocaste todos los Cruceros.");
        }
    }

    private void verificarAmbosListos() {
        if (yoListo && oponenteListo) {
            iniciarJuego(tableroJugador, tableroOponente);
        }
    }//GEN-LAST:event_seleccionarCruceroActionPerformed
    private String colorToHex(Color color) {
        return "#" + Integer.toHexString(color.getRGB()).substring(2).toUpperCase();
    }

    private Color hexToColor(String hex) {
        return Color.decode(hex);
    }

    private void verificarTodoListo() {
        int totalEsperado = 10; // total de barcos: 1x4 + 2x3 + 3x2 + 4x1
        int totalColocados = colocados.values().stream().mapToInt(Integer::intValue).sum();
        if (totalColocados == totalEsperado) {
            btnConfirmar.setEnabled(true);
            btnConfirmar.setVisible(true);
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnGirar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JPanel jPanelColorOponente;
    private javax.swing.JPanel jPanelDerechos;
    private javax.swing.JPanel jPanelEsperando;
    private javax.swing.JPanel jPanelFondo;
    private javax.swing.JPanel jPanelHead;
    private javax.swing.JPanel jPanelNombreJugador;
    private javax.swing.JPanel jPanelTiposNaves;
    private javax.swing.JLabel jblConfiguracion1;
    private javax.swing.JLabel jblDerechos;
    private javax.swing.JLabel jblEsperando;
    private javax.swing.JLabel jblFondo;
    private javax.swing.JLabel jblNombreJugador;
    private javax.swing.JLabel jblTiposNave;
    private javax.swing.JPanel panelListaBarcos;
    private javax.swing.JButton seleccionarBarco;
    private javax.swing.JButton seleccionarCrucero;
    private javax.swing.JButton seleccionarPortaAviones;
    private javax.swing.JButton seleccionarSubmarino;
    private javax.swing.JLabel txtBarco;
    private javax.swing.JLabel txtContadorBarco;
    private javax.swing.JLabel txtContadorCrucero;
    private javax.swing.JLabel txtContadorPortaAviones;
    private javax.swing.JLabel txtContadorSubmarino;
    private javax.swing.JLabel txtCrucero;
    private javax.swing.JLabel txtNombreOponente;
    private javax.swing.JLabel txtNombreOponente3;
    private javax.swing.JLabel txtPortaAviones;
    private javax.swing.JLabel txtSubmarino;
    // End of variables declaration//GEN-END:variables
}
