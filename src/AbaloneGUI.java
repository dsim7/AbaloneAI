import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
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

public class AbaloneGUI extends JPanel implements GameGUI {
    private AbaloneGUIInfoPanel info = new AbaloneGUIInfoPanel();
    private Abalone ab;
    private GamePlayer player;

    AbaloneGUI(Abalone ab, GamePlayer player) {
        this.ab = ab;
        this.player = player;
        player.setGUI(this);
        this.setLayout(new BorderLayout());
        this.add(new AbaloneGUIGrid(), BorderLayout.CENTER);
        this.add(info, BorderLayout.SOUTH);
    }
    
    @Override
    public Game getGame() {
        return ab;
    }

    @Override
    public GamePlayer getPlayer() {
        return player;
    }

    @Override
    public void updateGUI() {
        info.updateInfo();
        repaint();
        revalidate();
    }
    
// panel containing statistics of the game
class AbaloneGUIInfoPanel extends JPanel {
        
        JLabel player1 = new JLabel("Player 1");
        JLabel player2 = new JLabel("Player 2");
        JLabel time1 = new JLabel("0");
        JLabel time2 = new JLabel("0");
        JLabel outs1 = new JLabel("0");
        JLabel outs2 = new JLabel("0");
        JLabel turns = new JLabel("0");
        
        AbaloneGUIInfoPanel() {
            this.setVisible(true);
            this.setLayout(new BorderLayout());
            this.setPreferredSize(new Dimension(100,100));
            this.setBackground(Color.BLACK);
            
            player1.setFont(Abalone.INFO_FONT);
            player1.setForeground(Abalone.P1_COLOR);
            player2.setFont(Abalone.INFO_FONT);
            player2.setForeground(Abalone.P2_COLOR);
            time1.setFont(Abalone.INFO_FONT);
            time1.setForeground(Abalone.P1_COLOR);
            time2.setFont(Abalone.INFO_FONT);
            time2.setForeground(Abalone.P2_COLOR);
            outs1.setFont(Abalone.INFO_FONT);
            outs1.setForeground(Abalone.P1_COLOR);
            outs2.setFont(Abalone.INFO_FONT);
            outs2.setForeground(Abalone.P2_COLOR);
            turns.setFont(Abalone.INFO_FONT);
            turns.setForeground(Color.WHITE);
            
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
            
            p1.add(player1);
            p1.add(time1);
            p1.add(outs1);
            p2.add(player2);
            p2.add(time2);
            p2.add(outs2);
            center.add(turns);
        }
        
        // update statistics with the most recent information
        private void updateInfo() {
            outs1.setText("" + ((AbalonePlayer) (getGame().getPlayers().get(0))).outs);
            outs2.setText("" + ((AbalonePlayer) (getGame().getPlayers().get(1))).outs);
            time1.setText("" + Abalone.FORMAT.format(((AbalonePlayer) (getGame().getPlayers().get(0))).timeTaken));
            time2.setText("" + Abalone.FORMAT.format(((AbalonePlayer) (getGame().getPlayers().get(1))).timeTaken));
            turns.setText("" + ((Abalone) getGame()).getTurns());
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            updateInfo();
        }
        

    }

    
    // panel containing the hexagonal board
    class AbaloneGUIGrid extends JPanel {
    AbaloneGUIGrid() {
        this.setBackground(Color.BLACK);
        this.setLayout(new GridLayout(9,1));
        for (int i = 8; i >= 0; i--) {
            this.add(new AbaloneGUIGridStrip(ab.squares[i]));
        }
    }
}


    // a strip represents one row of squares on the board
    class AbaloneGUIGridStrip extends JPanel {
        AbaloneGUIGridStrip(AbaloneSquare[] strip) {
            this.setBackground(Color.BLACK);
            this.setVisible(true);
            for (AbaloneSquare sq : strip) {
                if (sq != null) {
                    this.add(new AbaloneGUISquare(sq));
                }
            }
            
        }
        
        // represents a single square
        class AbaloneGUISquare extends JPanel {
            AbaloneSquare square;
            Color color;
            BasicStroke selectedStroke = new BasicStroke(10);
            
            AbaloneGUISquare(AbaloneSquare square) {
                this.setPreferredSize(new Dimension(85,85));
                this.setVisible(true);
                this.setOpaque(false);
                this.square = square;
                this.addMouseListener(new Listener());
            }
            
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                updateColor();
                g.setColor(color);
                g.fillOval(0,0,this.getWidth(), this.getHeight());
                List<GamePiece> pieces = square.getPieces();
                if (pieces.size() > 0) {
                    if (pieces.get(0).selected) {
                        Graphics2D g2 = (Graphics2D) g;
                        g.setColor(Color.yellow);
                        g2.setStroke(selectedStroke);
                        g2.drawOval(0, 0, this.getWidth(), this.getHeight());
                    }
                }
            }
            
            private void updateColor() {
                
                if (square.getPieces().size() == 0) {
                    color = Color.white;
                } else {
                    GamePiece occupant = square.getPieces().get(0);
                    AbalonePlayer owner = (AbalonePlayer) occupant.getOwner();
                    color = owner.color;
                }
                
                
            }

            private class Listener implements MouseListener {
                @Override
                public void mouseClicked(MouseEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void mouseEntered(MouseEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void mouseExited(MouseEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }

                @Override
                public void mousePressed(MouseEvent arg0) {
                    /*
                    System.out.println(square.x + " " + square.y);
                    
                    GamePlayer player = getGame().getCurPlayer();
                    if (player.getActionParam("piece") != null) {
                        player.trigger(square.moveTrigger);
                    } else {
                        player.trigger(square.selectTrigger);
                    }
                    
                    AbaloneGUI.this.repaint();
                    */
                    if (ab.selection1[0] == -1) {
                        ab.selection1[0] = square.x;
                        ab.selection1[1] = square.y;
                        System.out.println("Selection 1: " + square.x + " " + square.y);
                    } else if (ab.selection2[0] == -1) {
                        ab.selection2[0] = square.x;
                        ab.selection2[1] = square.y;
                        System.out.println("Selection 2: " + square.x + " " + square.y);
                    } else if (ab.directionSelection == null) {
                        System.out.println("Destination 2: " + square.x + " " + square.y);
                        ab.directionSelection = getDirection();
                        ab.move(ab.selection1[0],
                                ab.selection1[1],
                                ab.selection2[0],
                                ab.selection2[1],
                                ab.directionSelection);
                        System.out.println("Direction: " + ab.directionSelection);
                        ab.clearSelection();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent arg0) {
                    // TODO Auto-generated method stub
                    
                }
                
                private Abalone.Dir getDirection() {
                    
                    int dx = square.x - ab.selection2[0];
                    int dy = square.y - ab.selection2[1];
                    System.out.println("" + dx);
                    System.out.println("" + dy);
                    for (Abalone.Dir dir : Abalone.Dir.values()) {
                        System.out.println(dir + " " + dir.dx + " " + dir.dy);
                        if (dir.dx == dx && dir.dy == dy) {
                            return dir;
                        }
                    }
                    return null;
                }
                
            }
        }
    }

}
