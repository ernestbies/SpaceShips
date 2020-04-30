/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shipsclient.gui;

import com.shipsclient.cl.Status;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class ShipsClient extends javax.swing.JFrame {
    
    public int size; //size of one cell on board
    public String board; //variable that stores the state of the board
    public boolean gamestatus; //true - game is created or loaded
    private boolean loggedIn; //true - player is logged in
    private String username; // logged in player name
    
    //constructor
    public ShipsClient() {
        initComponents();
        board = "";
        for (int i =0;i<81;i++) {
            board+=" ";
        }        
        size = (int) (jPanel6.getWidth()/9);
        gamestatus = false;
        getContentPane().setBackground(new java.awt.Color(56, 60 ,74));
        jTextFieldUser.requestFocus();
        loggedIn = false;
        username = "";
        jButtonNewGame.setEnabled(false);
        jButtonGetGame.setEnabled(false);
    }

    
    class BackgroundPanel extends JPanel {
        //load of background
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            BufferedImage image;            
            try {
                image = ImageIO.read(new File("./images/background.jpg"));
                double ratio=(double)image.getWidth()/image.getHeight();
                if((double)jPanel1.getWidth()/jPanel1.getHeight()>=ratio){
                    g.drawImage(image, 0, 0, jPanel1.getWidth(), (int) (jPanel1.getWidth()/ratio), null);
                }
                else {
                    g.drawImage(image, 0, 0, (int) (jPanel1.getHeight()*ratio), jPanel1.getHeight(), null);
                }
            } catch (IOException ex) {
                Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void resizeFunction(){

        if(jPanel1.getWidth()>jPanel1.getHeight()){
                jPanel6.setSize((int) (Math.round(jPanel1.getHeight()*0.9/9)*9), (int) (Math.round(jPanel1.getHeight()*0.9/9)*9));
        } else {
                jPanel6.setSize((int) (Math.round(jPanel1.getWidth()*0.9/9)*9), (int) (Math.round(jPanel1.getWidth()*0.9/9)*9));
        }

        int separator=(int) (0.03*jPanel1.getWidth());

        jScrollPane3.setSize((int) (0.9*jPanel1.getWidth()-jPanel6.getWidth()),jPanel6.getHeight());
        jTextPane2.setSize((int) (0.9*jPanel1.getWidth()-jPanel6.getWidth()),jPanel6.getHeight());
        jPanel6.setLocation(jPanel1.getWidth()-jPanel6.getWidth()-separator,(jPanel1.getHeight()-jPanel6.getHeight())/2);
        jScrollPane3.setLocation(separator,jPanel6.getY());
    }

public class BoardPanel extends JPanel implements MouseListener {
        //support board
        public BoardPanel() {
            addMouseListener(this);
	}
        
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor( getBackground() );
            g.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            paintBoard(g2d);
        }
        
        //support to mouse
        @Override
        public void mouseClicked(MouseEvent e) {
            int x, y;
            Status shot;
            x = ((int) (e.getX() / size));
            y = ((int) (e.getY() / size));
            if (!gamestatus) {
                JOptionPane.showMessageDialog(null, "Utwórz lub wczytaj grę użytkownika!");
            } else {
                try {
                    //MD5(Arrays.toString(jPasswordField1.getPassword()))
                    shot = shotGame(username, Integer.toString(x) + Integer.toString(y));

                    jLabelKroki.setText("" + shot.getSteps());
                    board = shot.getBoard();

                    StyledDocument text = jTextPane1.getStyledDocument();
                    Style style = jTextPane1.addStyle("Style", null);

                    switch (shot.getCode()) {
                        case "CHECKED":
                            StyleConstants.setForeground(style, Color.yellow);
                            text.insertString(text.getLength(), username + ": ", style);
                            StyleConstants.setForeground(style, Color.orange);
                            text.insertString(text.getLength(), (char) (65 + x) + "" + String.valueOf(y + 1), style);
                            StyleConstants.setForeground(style, Color.blue);
                            text.insertString(text.getLength(), " to pole było sprawdzane! \n", style);
                            sound("checked.wav");
                            break;
                        case "MISS":
                            StyleConstants.setForeground(style, Color.yellow);
                            text.insertString(text.getLength(), username + ": ", style);
                            StyleConstants.setForeground(style, Color.ORANGE);
                            text.insertString(text.getLength(), "Pudło! \n", style);
                            sound("miss.wav");
                            break;
                        case "ENDGAME":
                            StyleConstants.setForeground(style, Color.yellow);
                            text.insertString(text.getLength(), username + ": ", style);
                            StyleConstants.setForeground(style, Color.GREEN);
                            text.insertString(text.getLength(), "KONIEC GRY!!! po " + shot.getSteps() + " krokach \n", style);
                            sound("endgame.wav");
                            JOptionPane.showMessageDialog(null,"KONIEC GRY!!!\nPo " + shot.getSteps() + " krokach!");
                            break;
                        case "SHOTDOWN":
                            StyleConstants.setForeground(style, Color.yellow);
                            text.insertString(text.getLength(), username + ": ", style);
                            StyleConstants.setForeground(style, Color.red);
                            text.insertString(text.getLength(), "Z E S T R Z E L O N Y !! " + shot.getShipName() + "(" + shot.getType() + ") \n", style);
                            sound("shotdown.wav");
                            break;
                        case "HIT":
                            StyleConstants.setForeground(style, Color.yellow);
                            text.insertString(text.getLength(), username + ": ", style);
                            StyleConstants.setForeground(style, Color.PINK);
                            text.insertString(text.getLength(), "TRAFIONY !! " + shot.getShipName() + "(" + shot.getType() + ") \n", style);
                            sound("hit.wav");
                            break;
                    }

                    if (!"ENDGAME".equals(shot.getCode())) {
                        jPanel6.repaint();
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
                } catch (HttpClientErrorException ex) {
                    JOptionPane.showMessageDialog(null, "UWAGA! Niepoprawne zapytanie do serwera!");
                } catch (RestClientException ex) {
                    JOptionPane.showMessageDialog(null, "UWAGA! Brak połączenia z serwerem!");
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {       
        }

        @Override
        public void mouseReleased(MouseEvent e) {           
        }

        @Override
        public void mouseEntered(MouseEvent e) {            
        }

        @Override
        public void mouseExited(MouseEvent e) {            
        }
        
    }
    
    //paint the board
    private void paintBoard(Graphics2D g) {        
        int p;
        
        size = (int) (jPanel6.getWidth()/9);
        
        g.setColor(Color.WHITE);        
        for (int i=0;i<9;i++){
            g.drawLine(i*size+size, 0, i*size+size, jPanel6.getHeight());
            g.drawLine(0, i*size+size, jPanel6.getWidth(), i*size+size);
        }        
        
        BufferedImage image;
        
        try {
                        
            for (int y = 0; y < 9; y++) {
                for (int x = 0; x < 9; x++) {
                    p = y * 9 + x;
                    if (board.charAt(p) >= '0' && board.charAt(p) <= '9') {
                        g.drawLine(x*size, y*size, x*size + size, y*size + size);
                        g.drawLine(x*size, y*size + size, x*size + size, y*size);                      
                        g.setColor(Color.WHITE); 
                        g.drawString(Character.toString(board.charAt(p)), x*size + (int) (size/2), y*size + (int) (size/4));                
                    } else if (board.charAt(p) >= 'A' && board.charAt(p) <= 'Z'){
                        image = ImageIO.read(new File("./images/"+Character.toString(board.charAt(p))+".png"));             
                        g.drawImage(image, x*size + 1, y*size + 1, size - 1, size - 1, null);

                    }
                }
            }            
        } catch (IOException ex) {
            Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel1 = new BackgroundPanel();
        jPanel6 = new BoardPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButtonRank = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelKroki = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel5 = new javax.swing.JPanel();
        jTextFieldUser = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButtonNewGame = new javax.swing.JButton();
        jButtonGetGame = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Statki kosmiczne 1.0");
        setMinimumSize(new java.awt.Dimension(800, 630));
        setPreferredSize(new java.awt.Dimension(1024, 768));

        jPanel1.setBackground(new java.awt.Color(56, 60, 74));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(1024, 768));
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel1ComponentResized(evt);
            }
        });
        jPanel1.setLayout(null);

        jPanel6.setBackground(new Color(67,88,107,92));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(450, 450));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel6);
        jPanel6.setBounds(530, 60, 450, 450);

        jScrollPane3.setBackground(new java.awt.Color(153, 153, 153));
        jScrollPane3.setBorder(null);
        jScrollPane3.setAutoscrolls(true);
        jScrollPane3.setDoubleBuffered(true);
        jScrollPane3.setOpaque(false);
        jScrollPane3.setPreferredSize(new java.awt.Dimension(736, 85));

        jTextPane2.setEditable(false);
        jTextPane2.setBackground(new java.awt.Color(153, 153, 153));
        jTextPane2.setBorder(null);
        jTextPane2.setContentType("text/html"); // NOI18N
        jTextPane2.setText("<html>\n<body>\n<font color=\"white\">\n\t<center>ZASADY GRY</center> \n<br> \n\tGra polega na odkrywaniu na planszy poszczególnych pól tak, aby trafić w statek kosmiczny. \n<br>\n\tNa każdym odkrytym polu widnieje liczba, która wskazuje ile bezpośrednio stykających się pól zajmowanych jest przez wrogie statki kosmiczne.\n<br>\n\tZadaniem gracza jest zestrzelenie wszystkich statków znajdujących się na planszy w jak najmniejszej liczbie kroków.\t\t\n<br>\n<br>\n\t<center>LEGENDA\n<br>\n<table>\n<tr>\n\t<td><img src=\"file:images/A.png\" width=10% height=auto/></td>\t<td><font color=\"white\">Szturmowiec</font></td>\t<td><font color=\"white\">1 pole</font></td>\n</tr>\n<tr>\n\t<td><img src=\"file:images/B.png\" width=20% height=auto/></td>\t<td><font color=\"white\">Wahadłowiec</font></td>\t<td><font color=\"white\">2 pola</font></td>\n</tr>\n<tr>\n\t<td><img src=\"file:images/C.png\" width=20% height=auto/></td>\t<td><font color=\"white\">Samolot kosmiczny</font></td>\t<td><font color=\"white\">3 pola</font></td>\n</tr>\n<tr>\n\t<td><img src=\"file:images/D.png\" width=20% height=auto/></td>\t<td><font color=\"white\">Transportowiec</font></td>\t<td><font color=\"white\">4 pola</font></td>\n</tr>\n</font>\n</table></center>\n</body> \n</html>");
        jTextPane2.setOpaque(false);
        jScrollPane3.getViewport().setOpaque(false);
        jScrollPane3.setViewportView(jTextPane2);
        jScrollPane3.setViewportBorder(null);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(130, 60, 330, 440);
        jScrollPane3.getAccessibleContext().setAccessibleDescription("");

        jPanel2.setBackground(new java.awt.Color(56, 60, 74));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setFont(new java.awt.Font("Calibri Light", 0, 25)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Statki kosmiczne");
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Autorzy: Ernest Bieś, Konrad Czechowski, Dawid Kwaśny");
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jButtonRank.setBackground(new java.awt.Color(68, 74, 88));
        jButtonRank.setForeground(new java.awt.Color(255, 255, 255));
        jButtonRank.setText("Ranking");
        jButtonRank.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonRank.setMaximumSize(new java.awt.Dimension(113, 19));
        jButtonRank.setMinimumSize(new java.awt.Dimension(113, 19));
        jButtonRank.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRankActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(68, 74, 88));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Pokaż/ukryj zasady gry");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButtonRank, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE))
                .addContainerGap(241, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButtonRank, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 19, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(56, 60, 74));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Liczba kroków");

        jLabelKroki.setFont(new java.awt.Font("Times New Roman", 1, 60)); // NOI18N
        jLabelKroki.setForeground(new java.awt.Color(255, 255, 255));
        jLabelKroki.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelKroki.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelKroki, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelKroki, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(56, 60, 74));

        jScrollPane1.setBorder(null);

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane1.setViewportView(jTextPane1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanel5.setBackground(new java.awt.Color(56, 60, 74));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Logowanie do serwera", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        jPanel5.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.setFocusTraversalPolicyProvider(true);

        jTextFieldUser.setBackground(new java.awt.Color(68, 74, 88));
        jTextFieldUser.setForeground(new java.awt.Color(255, 255, 255));
        jTextFieldUser.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(58, 62, 75), 2, true));
        jTextFieldUser.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldUserFocusLost(evt);
            }
        });

        jPasswordField1.setBackground(new java.awt.Color(68, 74, 88));
        jPasswordField1.setForeground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(58, 62, 75), 2, true));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Nazwa użytkownika");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hasło");

        jButtonNewGame.setBackground(new java.awt.Color(68, 74, 88));
        jButtonNewGame.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNewGame.setText("Nowa gra");
        jButtonNewGame.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNewGameActionPerformed(evt);
            }
        });

        jButtonGetGame.setBackground(new java.awt.Color(68, 74, 88));
        jButtonGetGame.setForeground(new java.awt.Color(255, 255, 255));
        jButtonGetGame.setText("Wczytaj grę");
        jButtonGetGame.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonGetGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGetGameActionPerformed(evt);
            }
        });

        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldUser, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonGetGame, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                    .addComponent(jButtonNewGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonNewGame, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButtonGetGame, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1124, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    //support to button "NewGame" - sending information to the server about creating a new user game
    private void jButtonNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNewGameActionPerformed
        Status status;
        if (!loggedIn) {
            JOptionPane.showMessageDialog(null, "Zaloguj się!");
        } else {
            try {

                StyledDocument text = jTextPane1.getStyledDocument();
                Style style = jTextPane1.addStyle("Style", null);
                status = newGame(username);
                try {
                    StyleConstants.setForeground(style, Color.yellow);
                    text.insertString(text.getLength(), username, style);
                    StyleConstants.setForeground(style, Color.CYAN);

                    
                    text.insertString(text.getLength(), ": utworzono nowa gre użytkownika  \n", style);
                    sound("newloadgame.wav");
                    gamestatus = true;
                } catch (BadLocationException ex) {
                    Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
                }

                jLabelKroki.setText("" + status.getSteps());
                board = status.getBoard();

                jPanel6.repaint();
            } catch (HttpClientErrorException ex) {
                JOptionPane.showMessageDialog(null, "UWAGA! Niepoprawne zapytanie do serwera!");
            } catch (RestClientException e) {
                JOptionPane.showMessageDialog(null, "UWAGA! Brak połączenia z serwerem!");
            }
        }
    }//GEN-LAST:event_jButtonNewGameActionPerformed

    //support to button "GetGame" - sending information to the server about loading user game
    private void jButtonGetGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGetGameActionPerformed
        Status status;

        if (!loggedIn) {
            JOptionPane.showMessageDialog(null, "Wprowadź poprawną nazwę użytkownika i hasło!");
        } else {
            try {
                status = getGame(username);
                StyledDocument text = jTextPane1.getStyledDocument();
                Style style = jTextPane1.addStyle("Style", null);

                try {
                    StyleConstants.setForeground(style, Color.yellow);
                    text.insertString(text.getLength(), username, style);
                    StyleConstants.setForeground(style, Color.CYAN);

                    if ("NOGAME".equals(status.getCode())) {
                        text.insertString(text.getLength(), ": brak zapisanej gry użytkownika  \n", style);
                    } else {
                        text.insertString(text.getLength(), ": wczytano grę użytkownika  \n", style);
                        sound("newloadgame.wav");
                        gamestatus = true;
                        jLabelKroki.setText("" + status.getSteps());
                        board = status.getBoard();
                        jPanel6.repaint();
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (HttpClientErrorException ex) {
                JOptionPane.showMessageDialog(null, "UWAGA! Niepoprawne zapytanie do serwera!");
            } catch (RestClientException e) {
                JOptionPane.showMessageDialog(null, "UWAGA! Brak połączenia z serwerem!");
            }
        }
    }//GEN-LAST:event_jButtonGetGameActionPerformed

    private void jButtonRankActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRankActionPerformed
        String rank = getRank();
        if(rank==null || rank.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Brak wyników na serwerze");
        } else {
            final int columnCount = 2;
            final int rowCount = 10;
            String[] ranks = rank.split("\n");
            String[] columnNames = {"Player","Score"};
        
            Object[][] data = new Object[rowCount][columnCount];
            for(int i=0; i<ranks.length; i++) {
                data[i][0] = ranks[i].split(" ")[0];
                data[i][1] = ranks[i].split(" ")[1];
            }
        
            DefaultTableModel model = new DefaultTableModel(data, columnNames)
            {
                public Class<?> getColumnClass(int column){
                    return String.class;
                }
            };
            
            Ranking ranking = new Ranking(model);
            ranking.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            ranking.setVisible(true);
        }
        
    }//GEN-LAST:event_jButtonRankActionPerformed

    private void jTextFieldUserFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldUserFocusLost
        jPasswordField1.requestFocus();
    }//GEN-LAST:event_jTextFieldUserFocusLost

    private void jPanel1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel1ComponentResized
        resizeFunction();
    }//GEN-LAST:event_jPanel1ComponentResized

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        
        if(loggedIn) {
            //logout
            username = "";
            loggedIn = false;
            jButton1.setText("Login");
            jTextFieldUser.setText("");
            jPasswordField1.setText("");
            jButtonNewGame.setEnabled(false);
            jButtonGetGame.setEnabled(false);
            jTextFieldUser.setEnabled(true);
            jPasswordField1.setEnabled(true);
            //clearing game components
            jLabelKroki.setText("");
            board = "";
            for (int i =0;i<81;i++) {
                board+=" ";
            }
            jPanel6.repaint();
        }
        else if(!loggedIn && logIn(jTextFieldUser.getText(), MD5(jPasswordField1.getText()))) {
            //login
            loggedIn = true;
            username = jTextFieldUser.getText();
            jButton1.setText("Logout");
            jButtonNewGame.setEnabled(true);
            jButtonGetGame.setEnabled(true);
            jTextFieldUser.setEnabled(false);
            jPasswordField1.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(null, "Podaj prawidłowe hasło");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private boolean logIn(String user, String pass) throws RestClientException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        boolean status = restTemplate.getForObject("http://localhost:8080/api/login/" + user + "/" + pass, Boolean.class);
        return status;
    } 
    
    //method to send information to server about creating new game
    private Status newGame(String user) throws RestClientException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        Status status = restTemplate.getForObject("http://localhost:8080/api/newgame/" + user, Status.class);
        return status;
    }    

    //method to send information to server about loading a game
    private Status getGame(String user) throws RestClientException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();        
        Status status = restTemplate.getForObject("http://localhost:8080/api/getgame/" + user, Status.class);
        return status;
    }
    
    //method to send information to server about checking position
    private Status shotGame(String user, String shot) throws RestClientException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        Status status = restTemplate.getForObject("http://localhost:8080/api/shotgame?user=" + user + "&shot=" + shot, Status.class);
        return status;        
    }
    
    private String getRank() throws RestClientException, HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        String rank = restTemplate.getForObject("http://localhost:8080/api/getrank", String.class);
        return rank;        
    }
    
    //method to MD5 coding passwords
    private String MD5(String password){   
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashInBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ShipsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    //method to check if username is correctly entered
    private boolean checkName(String s) {
        char[] chars = s.toCharArray();
        if(s.equals("")) return false;
        for (int x = 0; x < chars.length; x++) {
            char c = chars[x];
            if ((c >= 'a') && (c <= 'z')) {
                continue;
            }
            if ((c >= 'A') && (c <= 'Z')) {
                continue;
            }
            
            if((c >= '0') && (c <= '9')){
                continue;
            }
            
            return true;
        }
        return false;
    }
    
    //method to play sounds in game (hit, miss, checked, shotdown, new/load game, endgame)
    private void sound(String audioFile) {
        try {
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;

            stream = AudioSystem.getAudioInputStream(new File("./sounds/"+audioFile));
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            e.getMessage();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
//        try {
//            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            // If Nimbus is not available, you can set the GUI to another look and feel.
//        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ShipsClient().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonGetGame;
    private javax.swing.JButton jButtonNewGame;
    private javax.swing.JButton jButtonRank;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelKroki;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextFieldUser;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    // End of variables declaration//GEN-END:variables
}
