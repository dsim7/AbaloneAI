package abalone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    private AbaloneGUISquare[][] coordSpaces = new AbaloneGUISquare[9][9];
    private AbaloneGUIInfoPanel info = new AbaloneGUIInfoPanel();

    public AbaloneGUI(Abalone ab) {
        this.ab = ab;
        this.setLayout(new BorderLayout());
        this.add(new AbaloneGUIGrid(), BorderLayout.CENTER);
        this.add(info, BorderLayout.SOUTH);
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
                    guisq.color = Color.DARK_GRAY;
                }
            }
        }
        if (ab.getState() == null) {
            return;
        }
        // player1 
        Set<AbaloneCoord> p1Pieces = ab.getState().p1Pieces;
        for (AbaloneCoord p1piece : p1Pieces) {
            coordSpaces[p1piece.y][p1piece.x].color = Color.RED;
        }
        
        // player2
        Set<AbaloneCoord> p2Pieces = ab.getState().p2Pieces;
        for (AbaloneCoord p2piece : p2Pieces) {
            coordSpaces[p2piece.y][p2piece.x].color = Color.BLUE;
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
            this.setBackground(Color.BLACK);
            
            player1label.setFont(Abalone.INFO_FONT);
            player1label.setForeground(Abalone.P1_COLOR);
            player2label.setFont(Abalone.INFO_FONT);
            player2label.setForeground(Abalone.P2_COLOR);
            time1label.setFont(Abalone.INFO_FONT);
            time1label.setForeground(Abalone.P1_COLOR);
            time2label.setFont(Abalone.INFO_FONT);
            time2label.setForeground(Abalone.P2_COLOR);
            outs1label.setFont(Abalone.INFO_FONT);
            outs1label.setForeground(Abalone.P1_COLOR);
            outs2label.setFont(Abalone.INFO_FONT);
            outs2label.setForeground(Abalone.P2_COLOR);
            turnslabel.setFont(Abalone.INFO_FONT);
            turnslabel.setForeground(Color.WHITE);
            roundTime1label.setFont(Abalone.INFO_FONT);
            roundTime1label.setForeground(Abalone.P1_COLOR);
            roundTime2label.setFont(Abalone.INFO_FONT);
            roundTime2label.setForeground(Abalone.P2_COLOR);
            
            JPanel p1 = new JPanel();
            p1.setLayout(new GridLayout());
            p1.setBackground(Color.BLACK);
            p1.setPreferredSize(new Dimension(400,100));
            
            JPanel p2 = new JPanel();
            p2.setLayout(new GridLayout());
            p2.setBackground(Color.BLACK);
            p2.setPreferredSize(new Dimension(400,100));
            
            JPanel center = new JPanel();
            center.setBackground(Color.BLACK);
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
            outs1label.setText("" + ab.getPlayers()[0].outs);
            outs2label.setText("" + ab.getPlayers()[1].outs);
            time1label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[0].timeTaken));
            time2label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[1].timeTaken));
            roundTime1label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[0].roundTimeTaken));
            roundTime2label.setText("" + Abalone.TIME_FORMAT.format(ab.getPlayers()[1].roundTimeTaken));
            turnslabel.setText("" + (ab.getState() == null ? 0 : ab.getState().turn));
            repaint();
        }
        

    }

    
    // panel containing the hexagonal board
    private class AbaloneGUIGrid extends JPanel {
        private AbaloneGUIGrid() {
            this.setBackground(Color.BLACK);
            this.setLayout(new GridLayout(9,1));
            for (int i = 8; i >= 0; i--) {
                this.add(new AbaloneGUIGridStrip(ab.getBoard()[i]));
            }
        }
    }


    // a strip represents one row of squares on the board
    private class AbaloneGUIGridStrip extends JPanel {
        private AbaloneGUIGridStrip(AbaloneCoord[] strip) {
            this.setBackground(Color.BLACK);
            this.setVisible(true);
            for (AbaloneCoord sq : strip) {
                if (sq != null) {
                    AbaloneGUISquare guisq = new AbaloneGUISquare(sq);
                    this.add(guisq);
                    AbaloneGUI.this.coordSpaces[sq.y][sq.x] = guisq;
                }
            }
        }
    }


    // represents a single square
    private class AbaloneGUISquare extends JPanel {
        Color color;
        AbaloneCoord coord;
        //AbaloneSquare square;
        //BasicStroke selectedStroke = new BasicStroke(10);
        
        private AbaloneGUISquare(AbaloneCoord coord) {
            this.setPreferredSize(new Dimension(85,85));
            this.setVisible(true);
            this.setOpaque(false);
            this.coord = coord;
            this.addMouseListener(new Listener());
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(color);
            g.fillOval(0,0,this.getWidth(), this.getHeight());
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
                if (!(ab.getCurPlayer()).isAI && ab.getCanClick()) {
                    ab.clicked(coord);
                }
            }
    
            @Override
            public void mouseReleased(MouseEvent arg0) {}
    
            
        }
    }




}
