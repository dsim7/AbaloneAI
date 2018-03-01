import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import game.Game; 
import game.GameGUI;
import game.GamePiece;
import game.GamePlayer;

public class AbaloneGUI extends JPanel {
    private Abalone ab;
    
    AbaloneState state;
    AbaloneGUISquare[][] coordSpaces = new AbaloneGUISquare[9][9];
    AbaloneGUIInfoPanel info = new AbaloneGUIInfoPanel();

    AbaloneGUI(Abalone ab) {
        this.ab = ab;
        this.setLayout(new BorderLayout());
        this.add(new AbaloneGUIGrid(), BorderLayout.CENTER);
        this.add(info, BorderLayout.SOUTH);
    }
    
    public void updateState(AbaloneState state) {
        this.state = state;
        this.updateColors();
        this.updateInfo();
        this.repaint();
    }
    
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
        if (state == null) {
            return;
        }
        // player1 
        List<AbaloneCoord> p1Pieces = state.p1Pieces;
        for (AbaloneCoord p1piece : p1Pieces) {
            coordSpaces[p1piece.y][p1piece.x].color = Color.RED;
        }
        // player2
        List<AbaloneCoord> p2Pieces = state.p2Pieces;
        for (AbaloneCoord p2piece : p2Pieces) {
            coordSpaces[p2piece.y][p2piece.x].color = Color.BLUE;
        }
    }
    
    
    // panel containing statistics of the game
    class AbaloneGUIInfoPanel extends JPanel {
        JLabel player1label = new JLabel("Player 1");
        JLabel player2label = new JLabel("Player 2");
        JLabel time1label = new JLabel("0");
        JLabel time2label = new JLabel("0");
        JLabel outs1label = new JLabel("0");
        JLabel outs2label = new JLabel("0");
        JLabel turnslabel = new JLabel("0");
        
        AbaloneGUIInfoPanel() {
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
            p1.add(outs1label);
            p2.add(player2label);
            p2.add(time2label);
            p2.add(outs2label);
            center.add(turnslabel);
        }
        
        // update statistics with the most recent information
        private void updateInfo() {
            outs1label.setText("" + ab.player1.outs);
            outs2label.setText("" + ab.player2.outs);
            time1label.setText("" + Abalone.FORMAT.format(ab.player1.timeTaken));
            time2label.setText("" + Abalone.FORMAT.format(ab.player2.timeTaken));
            turnslabel.setText("" + (state == null ? 0 : state.turn));
            repaint();
        }
        

    }

    
    // panel containing the hexagonal board
    class AbaloneGUIGrid extends JPanel {
    AbaloneGUIGrid() {
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(9,1));
        for (int i = 8; i >= 0; i--) {
            this.add(new AbaloneGUIGridStrip(ab.board[i]));
        }
    }
}


    // a strip represents one row of squares on the board
    class AbaloneGUIGridStrip extends JPanel {
        AbaloneGUIGridStrip(AbaloneCoord[] strip) {
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
    class AbaloneGUISquare extends JPanel {
        Color color;
        AbaloneCoord coord;
        //AbaloneSquare square;
        //BasicStroke selectedStroke = new BasicStroke(10);
        
        AbaloneGUISquare(AbaloneCoord coord) {
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
                if (!((AbalonePlayer) ab.getCurPlayer()).isAI && ab.getCanClick()) {
                    System.out.println(coord);
                    if (ab.selection1 == null) {
                        ab.selection1 = coord;
                    } else if (ab.selection2 == null) {
                        ab.selection2 = coord;
                    } else if (ab.directionSelection == null) {
                        ab.directionSelection = getDirection();
                        if (!ab.move(ab.selection1,
                                ab.selection2,
                                ab.directionSelection)) {
                            System.out.println("Invalid move, try again");
                        }
                        //System.out.println("Direction: " + ab.directionSelection);
                        ab.clearSelection();
                    }
                }
            }
    
            @Override
            public void mouseReleased(MouseEvent arg0) {}
    
            private Abalone.Dir getDirection() {
                
                int dx = coord.x - ab.selection2.x;
                int dy = coord.y - ab.selection2.y;
                //System.out.println("" + dx);
                //System.out.println("" + dy);
                for (Abalone.Dir dir : Abalone.Dir.values()) {
                    //System.out.println(dir + " " + dir.dx + " " + dir.dy);
                    if (dir.dx == dx && dir.dy == dy) {
                        return dir;
                    }
                }
                return null;
            }
            
        }
    }




}
