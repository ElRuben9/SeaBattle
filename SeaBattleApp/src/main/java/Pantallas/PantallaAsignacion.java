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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 *
 * @author ruben
 */
public class PantallaAsignacion extends javax.swing.JFrame {

    private Tablero tableroJugador;
    private JButton[][] botonesTablero = new JButton[10][10];
    private String orientacionActual = "horizontal"; // horizontal por defecto
    private TipoBarco tipoSeleccionado = TipoBarco.BARCO; // Tipo por defecto (puedes mejorarlo luego)
    private ViewModels.AsignacionViewModel vmAsignacion;
    private boolean esJugador1 = true; // Empezamos con jugador 1
    private Tablero tableroJugador1;
    private Tablero tableroJugador2;
    private Barco barcoActual;
    String fondo = "recursos/interfaz/fondoPantallaPartida.png";
    private Orientacion orientacion;
    PantallaEscogerPartida escoger;

    private final Map<TipoBarco, Integer> maximos = Map.of(
            TipoBarco.BARCO, 4, // Lancha
            TipoBarco.CRUCERO, 3, // Destructor
            TipoBarco.SUBMARINO, 2,
            TipoBarco.PORTAAVIONES, 1
    );

    private final Map<TipoBarco, Integer> colocados = new HashMap<>();

    /**
     * Creates new form PantallaAsignacion
     *
     * @param escoger
     */
    public PantallaAsignacion(PantallaEscogerPartida escoger) {
        initComponents();

        this.escoger = escoger;

        cargarInterfaz();
        tableroJugador = new Tablero();
        for (TipoBarco tipo : TipoBarco.values()) {
            colocados.put(tipo, 0); // Todos inician con 0 colocados
        }
        actualizarContadores();
        for (TipoBarco tipo : TipoBarco.values()) {
            JButton btnTipo = new JButton(tipo.name()); // Puedes personalizar el nombre con .toString() si prefieres
            btnTipo.addActionListener(e -> {
                if (colocados.get(tipo) < maximos.get(tipo)) {
                    tipoSeleccionado = tipo;
                    barcoActual = new Barco(tipo);
                } else {
                    JOptionPane.showMessageDialog(this, "Ya colocaste todos los " + tipo.name().toLowerCase());
                }
            });
        }

    }

    private void actualizarContadores() {
        txtContadorBarco.setText(colocados.get(TipoBarco.BARCO) + "/" + maximos.get(TipoBarco.BARCO));
        txtContadorCrucero.setText(colocados.get(TipoBarco.CRUCERO) + "/" + maximos.get(TipoBarco.CRUCERO));
        txtContadorSubmarino.setText(colocados.get(TipoBarco.SUBMARINO) + "/" + maximos.get(TipoBarco.SUBMARINO));
        txtContadorPortaAviones.setText(colocados.get(TipoBarco.PORTAAVIONES) + "/" + maximos.get(TipoBarco.PORTAAVIONES));
    }

    private void cargarInterfaz() {

        PersonalizacionGeneral.colocarImagenLabel(jblFondo, fondo);
        // PersonalizacionGeneral.imagenAJLabelMaxCalidad(jblIconApuntar, "recursos/iconos/apuntar.png");

        personazilarBotones();
        vmAsignacion = new ViewModels.AsignacionViewModel();
        crearTablero();
        btnConfirmar.setEnabled(false);
        btnConfirmar.setVisible(false);

    }

    private void colocarBarco(int x, int y) {
        
        if(tipoSeleccionado.equals(TipoBarco.BARCO) && colocados.get(TipoBarco.BARCO) == 4){
            System.out.println(colocados.get(TipoBarco.BARCO));
            JOptionPane.showMessageDialog(this, "Maximo numero de barcos alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(tipoSeleccionado.equals(TipoBarco.CRUCERO) && colocados.get(TipoBarco.CRUCERO) == 3){
            JOptionPane.showMessageDialog(this, "Maximo numero de cruceros alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
                
        if(tipoSeleccionado.equals(TipoBarco.SUBMARINO) && colocados.get(TipoBarco.SUBMARINO) == 2){
            JOptionPane.showMessageDialog(this, "Maximo numero de submarinos alcanzado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(tipoSeleccionado.equals(TipoBarco.PORTAAVIONES) && colocados.get(TipoBarco.PORTAAVIONES) == 1){
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
        
        for(int i = 0; i < barcoActual.getTamaño(); i++){
            if(orientacionActual.equals("horizontal") && botonesTablero[x + i][y].getBackground().equals(Color.GRAY)){
                JOptionPane.showMessageDialog(this, "El espacio ya esta ocupado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if(orientacionActual.equals("vertical") && botonesTablero[x][y + i].getBackground().equals(Color.GRAY)){
                JOptionPane.showMessageDialog(this, "El espacio ya esta ocupado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Si el barco cabe, lo colocamos
        boolean colocado = tableroJugador.colocarBarco(barcoActual, x, y);

        if (colocado) {
            // Aumentar contador
            colocados.put(barcoActual.getTipo(), colocados.get(barcoActual.getTipo()) + 1);
            actualizarContadores();

            // Pintar las celdas
            for (int i = 0; i < barcoActual.getTamaño(); i++) {
                if (orientacionActual.equals("horizontal")) {
                    botonesTablero[x + i][y].setBackground(Color.GRAY);
                } else {
                    botonesTablero[x][y + i].setBackground(Color.GRAY);
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
                if (barcoActual != null){
                    
                    if (orientacionActual.equals("horizontal") && x + barcoActual.getTamaño() > 10) {
                        return;
                    } else if (orientacionActual.equals("vertical") && y + barcoActual.getTamaño() > 10) {
                        return;
                }
                    
                    for (int i = 0; i < barcoActual.getTamaño(); i++) {
                        try{    
                            if (orientacionActual.equals("horizontal")) {
                                if(botonesTablero[x + i][y].getBackground().equals(Color.GRAY)){
                                    return;
                                }
                                botonesTablero[x + i][y].setBackground(Color.LIGHT_GRAY);

                            } else {
                                if(botonesTablero[x][y + i].getBackground().equals(Color.GRAY)){
                                    return;
                                }
                                botonesTablero[x][y + i].setBackground(Color.LIGHT_GRAY);
                            }
                            }
                        catch(Exception ex){

                            botonesTablero[x + i][y].setBackground(new Color(173, 216, 230));
                        }
            }
                }
            }

            public void mouseExited(MouseEvent e) {
                
                 if (barcoActual != null){
                     
                    if (orientacionActual.equals("horizontal") && x + barcoActual.getTamaño() > 10) {
                        return;
                } else if (orientacionActual.equals("vertical") && y + barcoActual.getTamaño() > 10) {
                        return;
                }
                     
                     
                     
                    for (int i = 0; i < barcoActual.getTamaño(); i++) {
                        try{    
                            if (orientacionActual.equals("horizontal")) {
                                if(botonesTablero[x + i][y].getBackground().equals(Color.GRAY)){
                                    return;
                                }
                                botonesTablero[x + i][y].setBackground(new Color(173, 216, 230));

                            } else {
                                if(botonesTablero[x][y + i].getBackground().equals(Color.GRAY)){
                                    return;
                                }
                                botonesTablero[x][y + i].setBackground(new Color(173, 216, 230));
                            }
                            }
                        catch(Exception ex){
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

        jPanelFondo.add(jPanelHead);
        jPanelHead.setBounds(0, 0, 0, 0);

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
        if (esJugador1) {
            tableroJugador1 = vmAsignacion.getTablero(); // Guarda el tablero de jugador 1
            esJugador1 = false; // Cambia al jugador 2
            vmAsignacion = new ViewModels.AsignacionViewModel(); // Nuevo tablero para jugador 2
            limpiarTableroGUI();
            JOptionPane.showMessageDialog(this, "Turno del Jugador 2: Coloca tus barcos.");
        } else {
            tableroJugador2 = vmAsignacion.getTablero(); // Guarda el tablero de jugador 2
            abrirPantallaJuego(); // Iniciar la partida
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

    private void abrirPantallaJuego() {
        PantallaJuego juego = new PantallaJuego(tableroJugador1, tableroJugador2);
        juego.setVisible(true);
        this.dispose();
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
        
        
        
        
    }//GEN-LAST:event_seleccionarCruceroActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JButton btnGirar;
    private javax.swing.JButton btnVolver;
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
    private javax.swing.JLabel txtPortaAviones;
    private javax.swing.JLabel txtSubmarino;
    // End of variables declaration//GEN-END:variables
}
