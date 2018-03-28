package abalone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The GUI to display the Abalone game
 * 
 * @author dylan
 *
 */
public class AbaloneGUI extends JPanel {
    private Abalone ab;

    public static final Font INFO_FONT = new Font("Arial",Font.BOLD, 20 );
    public static final Font COORD_FONT = new Font("Arial",Font.BOLD, 20 );
    public static final Color BACKGROUND_COLOR = new Color(0xA0522D);
    public static final Color BACKGROUND_COLOR2 = new Color(0xDEB887);
    private AbaloneGUISquare[][] coordSpaces = new AbaloneGUISquare[9][9];
    private AbaloneGUIInfoPanel info;
    private AbaloneGUITimer controls;
    private AbaloneGUIGrid grid;
    
    public AbaloneGUI(Abalone ab) {
        this.ab = ab;
        
        info = new AbaloneGUIInfoPanel();
        controls = new AbaloneGUITimer();
        grid = new AbaloneGUIGrid(true);
        
        this.setLayout(new BorderLayout());
        this.add(grid, BorderLayout.CENTER);
        this.add(info, BorderLayout.SOUTH);
        this.add(controls, BorderLayout.NORTH);
        this.setFocusable(false);
    }
    
    public void flip(boolean flip) {
        this.remove(grid);
        grid = new AbaloneGUIGrid(flip);
        this.add(grid);
        this.revalidate();
        this.updateState();
        
    }

    /**
     * Updates the GUI's display by reading the Abalone game's current state and
     * score/time information.
     * 
     */
    public void updateState() {
        this.updateColors();
        this.updateInfo();
        this.repaint();
    }
    
    /**
     * Updates only the GUI's score/time information display. 
     * 
     */
    public void updateInfo() {
        info.updateInfo();
    }
    
    private void updateColors() {
        // gray all out
        for (AbaloneGUISquare[] guisqarr : coordSpaces) {
            for (AbaloneGUISquare guisq : guisqarr) {
                if (guisq != null) {
                    guisq.color = BACKGROUND_COLOR;
                }
            }
        }
        if (ab.getState() == null) {
            return;
        }
        // player1 
        Set<AbaloneCoord> p1Pieces = ab.getState().p1Pieces;
        for (AbaloneCoord p1piece : p1Pieces) {
            coordSpaces[p1piece.y][p1piece.x].color = Abalone.P1_COLOR;
        }
        
        // player2
        Set<AbaloneCoord> p2Pieces = ab.getState().p2Pieces;
        for (AbaloneCoord p2piece : p2Pieces) {
            coordSpaces[p2piece.y][p2piece.x].color = Abalone.P2_COLOR;
        }
    }
    
    
    // panel containing statistics of the game
    private class AbaloneGUIInfoPanel extends JPanel {
        JLabel player1label = new JLabel("Player 1");
        JLabel player2label = new JLabel("Player 2");
        JLabel time1label = new JLabel("0");
        JLabel time2label = new JLabel("0");
        JLabel outs1label = new JLabel("0");
        JLabel outs2label = new JLabel("0");
        JLabel turnslabel = new JLabel("0");
        JLabel roundTime1label = new JLabel("0");
        JLabel roundTime2label = new JLabel("0");
        
        private AbaloneGUIInfoPanel() {
            this.setVisible(true);
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(100,100));
            this.setBackground(BACKGROUND_COLOR);
            this.setFocusable(false);
            
            player1label.setFont(INFO_FONT);
            player1label.setForeground(Abalone.P1_COLOR);
            player2label.setFont(INFO_FONT);
            player2label.setForeground(Abalone.P2_COLOR);
            time1label.setFont(INFO_FONT);
            time1label.setForeground(Abalone.P1_COLOR);
            time2label.setFont(INFO_FONT);
            time2label.setForeground(Abalone.P2_COLOR);
            outs1label.setFont(INFO_FONT);
            outs1label.setForeground(Abalone.P1_COLOR);
            outs2label.setFont(INFO_FONT);
            outs2label.setForeground(Abalone.P2_COLOR);
            turnslabel.setFont(INFO_FONT);
            turnslabel.setForeground(Color.WHITE);
            roundTime1label.setFont(INFO_FONT);
            roundTime1label.setForeground(Abalone.P1_COLOR);
            roundTime2label.setFont(INFO_FONT);
            roundTime2label.setForeground(Abalone.P2_COLOR);
            
            JPanel p1 = new JPanel();
            p1.setLayout(new GridLayout());
            p1.setBackground(BACKGROUND_COLOR);
            p1.setPreferredSize(new Dimension(400,100));
            
            JPanel p2 = new JPanel();
            p2.setLayout(new GridLayout());
            p2.setBackground(BACKGROUND_COLOR);
            p2.setPreferredSize(new Dimension(400,100));
            
            JPanel center = new JPanel();
            center.setBackground(BACKGROUND_COLOR);
            center.setLayout(new FlowLayout());
            
            this.add(p1, BorderLayout.WEST);
            this.add(p2, BorderLayout.EAST);
            this.add(center, BorderLayout.CENTER);
            
            p1.add(player1label);
            p1.add(time1label);
            p1.add(roundTime1label);
            p1.add(outs1label);
            p2.add(player2label);
            p2.add(time2label);
            p2.add(roundTime2label);
            p2.add(outs2label);
            center.add(turnslabel);
        }
        
        private void updateInfo() {
            AbaloneState state = ab.getState();
            outs1label.setText("" + (14 - state.p1Pieces.size()));
            outs2label.setText("" + (14 - state.p2Pieces.size()));
            time1label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[0].timeTaken));
            time2label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[1].timeTaken));
            roundTime1label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[0].roundTimeTaken));
            roundTime2label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[1].roundTimeTaken));
            
            turnslabel.setText("" + (state == null ? 0 : (state.turn + 1)));
            turnslabel.setForeground(state.turn == -1 ? Color.WHITE : (state.turn % 2 == 0 ? Abalone.P1_COLOR : Abalone.P2_COLOR));
            repaint();
        }
        

    }

    
    // panel containing the hexagonal board
    private class AbaloneGUIGrid extends JPanel {
        private AbaloneGUIGrid(boolean flipped) {
            if (flipped) {
                this.setBackground(BACKGROUND_COLOR);
                this.setFocusable(false);
                this.setLayout(new GridLayout(9,1));
                for (int i = 8; i >= 0; i--) {
                    this.add(new AbaloneGUIGridStrip(ab.getBoard()[i], flipped));
                }
            } else {
                this.setBackground(BACKGROUND_COLOR);
                this.setFocusable(false);
                this.setLayout(new GridLayout(9,1));
                for (int i = 0; i < 9; i++) {
                    this.add(new AbaloneGUIGridStrip(ab.getBoard()[i], flipped));
                }
            }
        }
    }


    // a strip represents one row of squares on the board
    private class AbaloneGUIGridStrip extends JPanel {
        private AbaloneGUIGridStrip(AbaloneCoord[] strip, boolean flipped) {
            this.setBackground(BACKGROUND_COLOR2);
            this.setVisible(true);
            this.setFocusable(false);
            if (flipped) {
                for (int i = 0; i < strip.length; i++) {
                    if (strip[i] != null) {
                        AbaloneGUISquare guisq = new AbaloneGUISquare(strip[i]);
                        this.add(guisq);
                        AbaloneGUI.this.coordSpaces[strip[i].y][strip[i].x] = guisq;
                    }
                }   
            } else {
                for (int i = strip.length - 1; i >= 0; i--) {
                    if (strip[i] != null) {
                        AbaloneGUISquare guisq = new AbaloneGUISquare(strip[i]);
                        this.add(guisq);
                        AbaloneGUI.this.coordSpaces[strip[i].y][strip[i].x] = guisq;
                    }
                }

            }
        }
    }


    // represents a single square
    private class AbaloneGUISquare extends JPanel {
        private Color color;
        private AbaloneCoord coord;
        //AbaloneSquare square;
        //BasicStroke selectedStroke = new BasicStroke(10);
        
        private AbaloneGUISquare(AbaloneCoord coord) {
            this.setPreferredSize(new Dimension(70,70));
            this.setVisible(true);
            this.setOpaque(false);
            this.coord = coord;
            this.addMouseListener(new Listener());
            this.setFocusable(false);
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillOval(0,0,this.getWidth(), this.getHeight());
            g.setColor(Color.CYAN);
            g.setFont(COORD_FONT);
            g.drawString(coord.toString(), 10, 20);
            /*
            List<GamePiece> pieces = square.getPieces();
            if (pieces.size() > 0) {
                if (pieces.get(0).selected) {
                    Graphics2D g2 = (Graphics2D) g;
                    g.setColor(Color.yellow);
                    g2.setStroke(selectedStroke);
                    g2.drawOval(0, 0, this.getWidth(), this.getHeight());
                }
            }
            */
        }
    
        private class Listener implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent arg0) {}
    
            @Override
            public void mouseEntered(MouseEvent arg0) {}
    
            @Override
            public void mouseExited(MouseEvent arg0) {}
    
            @Override
            public void mousePressed(MouseEvent arg0) {
                ab.clicked(coord);
            }
    
            @Override
            public void mouseReleased(MouseEvent arg0) {}
    
            
        }
    }

    private class AbaloneGUITimer extends JPanel {
        private JButton buttonAI = new JButton("Get AI Move");
        private JButton buttonSwitch = new JButton("Switch");
        private JButton buttonStart = new JButton("Go");
        private JButton buttonPause = new JButton("Pause");
        private JButton buttonStop = new JButton("Stop");
        private JButton buttonReset = new JButton("Reset");
        private JButton buttonNext = new JButton("Next Turn");
        private JButton buttonFlipGUI = new JButton("Flip GUI");
        
        private AbaloneGUITimer() {
            this.setPreferredSize(new Dimension(1000, 50));
            this.setLayout(new GridLayout());
            
            buttonAI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ab.getAIMove();
                }
                
            });
            buttonNext.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.nextPlayerTurn();
                }
            });
            buttonSwitch.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.switchTimers();
                    
                }
            });
            buttonStart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.resumeTimers();
                }
            });
            buttonPause.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.pauseTimers();
                }
            });
            buttonStop.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.stopTimers();
                }
            });
            buttonReset.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.resetTimers();
                }
            });
            buttonFlipGUI.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ab.flipGUI();
                }
            });
            
            this.add(buttonAI);
            //this.add(buttonNext);
            this.add(buttonSwitch);
            this.add(buttonStart);
            this.add(buttonPause);
            this.add(buttonStop);
            this.add(buttonReset);
            this.add(buttonFlipGUI);
        }
    }



}
