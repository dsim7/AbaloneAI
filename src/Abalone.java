import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.Timer;

import game.Game;
import game.GamePiece;
import game.GamePosition;

public class Abalone extends Game {
    
    static final int FRAME_WIDTH = 1000;
    static final int FRAME_HEIGHT = 1000; 

    static final Color P1_COLOR = Color.RED;
    static final Color P2_COLOR = Color.BLUE;
    static final Font INFO_FONT = new Font("Arial",Font.BOLD, 20 );
    static final DecimalFormat FORMAT = new DecimalFormat(".#");

    int turns = 0;
    Scanner console = new Scanner(System.in);
    AbaloneSquare[][] squares = new AbaloneSquare[9][9];

    ActionListener p1timerTick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.timeTaken += 0.1;
            player1.getGUI().updateGUI();
            player2.getGUI().updateGUI();
        }
        
    };
    Timer p1timer = new Timer(100, p1timerTick);
    ActionListener p2timerTick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player2.timeTaken += 0.1;
            player1.getGUI().updateGUI();
            player2.getGUI().updateGUI();
        }
        
    };
    Timer p2timer = new Timer(100, p2timerTick);
    
    AbalonePlayer player1 = new AbalonePlayer(P1_COLOR, this);
    AbalonePlayer player2 = new AbalonePlayer(P2_COLOR, this);
    
    
    public static void main(String[] args) {
        new Abalone();
    }
    
    @Override
    public void nextPlayerTurn() {
        super.nextPlayerTurn();
        startStopTimers();
        turns++;
        player1.getGUI().updateGUI();
        player2.getGUI().updateGUI();
        
    }
    
    public Abalone() {
        this.addPlayer(player1);
        this.addPlayer(player2);
        this.initSquares();
        this.initPiecesStandard();
        
        JFrame frame1 = new JFrame();
        frame1.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
        
        frame1.add(new AbaloneGUI(this, player1));
        frame1.revalidate();
        frame1.repaint();
        
        JFrame frame2 = new JFrame();
        frame2.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setVisible(true);
        
        frame2.add(new AbaloneGUI(this, player2));

        //this.move(2, 2, 2, 0, Dir.UL);

        p1timer.setRepeats(true);
        p2timer.setRepeats(true);
        p1timer.start();
        //p2timer.start();
        
        while (console.hasNextLine()) {
            if (this.processInput(console.nextLine())) {
                nextPlayerTurn();
                System.out.println("Player turn: " + getCurPlayer());
            }
            frame1.repaint();
            frame1.revalidate();
            frame2.repaint();
            frame2.revalidate();
        }
        
        
    }

    public boolean move(int x1, int y1, int x2, int y2, Dir dir) {
        boolean moveSuccess = false;
        
        // check move is in bounds and direction is not null
        if (!checkValidMoveInput(x1, y1, x2, y2, dir)) {
            return false;
        }
        
        // get pieces between coordinates. they must all be friendly and there
        // must be no more than 3 pieces, otherwise null is returned and function
        // exits with false
        ArrayList<AbalonePiece> pieces = getPieces(x1, y1, x2, y2);
        if (pieces == null) {
            return false;
        }
        
        // get squares to move to 
        ArrayList<AbaloneSquare> toSquares = new ArrayList<AbaloneSquare>();
        for (AbalonePiece piece : pieces) {
            if (piece != null) {
                int x = ((AbaloneSquare)piece.getPosition()).x + dir.dx;
                int y = ((AbaloneSquare)piece.getPosition()).y + dir.dy;
                toSquares.add(getSquare(x,y));
            }
        }
        
        // squares you're moving to cannot be occupied by friendlies
        if (this.checkSquaresOccupiedByFriendly(pieces, toSquares)) {
            return false;
        }
        
        // pushing move
        if (!inlineMove(x1, y1, x2, y2, dir)) {
            int numberOfPieces = pieces.size();
        } else {
            
        }
    
        
        for (int i = 0; i < Math.min(pieces.size(), toSquares.size()); i++) {
            movePiece(pieces.get(i), toSquares.get(i));
            moveSuccess = true;
        }
        return moveSuccess;
    }
    
    private void startStopTimers() {
        if (p1timer.isRunning()) {
            p1timer.stop();
            p2timer.start();
        } else {
            p2timer.stop();
            p1timer.start();
        }
    }

    private boolean inlineMove(int x1, int y1, int x2, int y2, Dir dir) {
        if ((x1 == x2) && (y1 != y2)) {
             if (dir == Dir.UL || dir == Dir.DR) {
                 return true;
             }
        }
        if ((x1 != x2) && (y1 == y2)) {
            if (dir == Dir.L || dir == Dir.R) {
                return true;
            }
        }
        if ((x1 != x2) && (y1 != y2)) {
            if (dir == Dir.UR || dir == Dir.DL) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkValidMoveInput(int x1, int y1, int x2, int y2, Dir dir) {
        if (dir == null) {
            return false;
        }
        if (!inBounds(x1,y1) || !inBounds(x2,y2)) {
            return false;
        }
        return true;
    }
    
    private boolean processInput(String input) {
        String[] parsed = input.split(" ");
        int x1 = Integer.parseInt(parsed[0]);
        int y1 = Integer.parseInt(parsed[1]);
        int x2 = Integer.parseInt(parsed[2]);
        int y2 = Integer.parseInt(parsed[3]);
        Dir dir = null;
        for (Dir direction : Dir.values()) {
            if (direction.name.equals(parsed[4])) {
                dir = direction;
            }
        }
        return move(x1, y1, x2, y2, dir);
    }
    
    private AbaloneSquare getSquare(int x, int y) {
        if (!inBounds(x, y)) {
            return null;
        }
        return squares[y][x];
    }

    private void movePiece(GamePiece piece, GamePosition pos) {
        if (piece != null) {
            if (pos == null) {
                piece.remove();
                System.out.println("piece out");
                ((AbalonePlayer)piece.getOwner()).outs++;
            }
            piece.move(pos);
        }
    }

    private boolean inBounds(int x, int y) {
        return (x > 0 || x < 8) || (y > 0 || y < 8);
    }
    
    private AbalonePiece getPiece(int x, int y) {
        if (squares[x][y] == null) {
            return null;
        }
        return (AbalonePiece)((AbaloneSquare)squares[y][x]).getPiece();
    }
    /*
    public void processMove(AbaloneMove move) {
        move(move.piece_x, move.piece_y, move.numberOfPieces, move.direction, move.broadside);
    }
    */
    private ArrayList<AbalonePiece> getPieces(int x1, int y1, int x2, int y2) {
        
        ArrayList<AbalonePiece> result = new ArrayList<AbalonePiece>();
        int dx = (x1 - x2);
        int dy = (y1 - y2);
        int ddx = getIncrement(dx);
        int ddy = getIncrement(dy);
        if (dx > 2 || dy > 2 || dx < -2 || dy < -2) {
            return null;
        }
        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            if (getPiece(x1 + ddx * i, y1 + ddy * i) == null ||
                    getPiece(x1 + ddx * i, y1 + ddy * i).getOwner() != getCurPlayer()) {
                return null;
            }
            result.add(getPiece(x1 + ddx * i, y1 + ddy * i));
        }
        
        return result;
    }
    
    private int getIncrement(int x) {
        if (x > 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }
    
    private boolean checkSquaresOccupiedByFriendly(ArrayList<AbalonePiece> pieces, ArrayList<AbaloneSquare> squares) {
        for (AbaloneSquare sq : squares) {
            if (sq != null) {
                if (sq.containsFriendly(getCurPlayer()) && !pieces.contains(sq.getPiece())) {
                    return true;
                }
            }
        }
        return false;
    }
    /*
    private boolean move(int x, int y, int numberOfPieces, Dir direction, boolean broadside) {
        if (!broadside) {
            switch (direction) {
            case L : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x, y+i), getSquare(x, y-1+i));
            }
            break;
            case UL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i+1, y));
            }
            break;
            case DL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i-1, y+i-1));
            }
            break;
            case UR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i+1, y+i+1));
            }
            break;
            case DR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i-1, y));
            }
            break;
            case R : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x, y+i), getSquare(x, y+i+1));
            }
            break;
            }
        }
        else {
            switch (direction) {
            case L : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x, y-1+i));
            }
            break;
            case UL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i+1, y));
            }
            break;
            case DL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i-1, y+i-1));
            }
            break;
            case UR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i+1, y+i+1));
            }
            break;
            case DR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i-1, y));
            }
            break;
            case R : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x, y+i+1));
            }
            break;
            }
        }
    
        return true;
    }
    */

    private void initSquares() {
        
        // 1
        for (int i = 0; i < 5; i++) {
            squares[0][i] = new AbaloneSquare(this, i, 0);
        }
        
        // 2
        for (int i = 0; i < 6; i++) {
            squares[1][i] = new AbaloneSquare(this, i, 1);
        }
        
        // 3
        for (int i = 0; i < 7; i++) {
            squares[2][i] = new AbaloneSquare(this, i, 2);
        }
        
        // 4
        for (int i = 0; i < 8; i++) {
            squares[3][i] = new AbaloneSquare(this, i, 3);
        }
        
        // 5
        for (int i = 0; i < 9; i++) {
            squares[4][i] = new AbaloneSquare(this, i, 4);
        }
        
        // 6
        for (int i = 1; i < 9; i++) {
            squares[5][i] = new AbaloneSquare(this, i, 5);
        }
        
        // 7
        for (int i = 2; i < 9; i++) {
            squares[6][i] = new AbaloneSquare(this, i, 6);
        }
        
        // 8
        for (int i = 3; i < 9; i++) {
            squares[7][i] = new AbaloneSquare(this, i, 7);
        }
        
        // 9
        for (int i = 4; i < 9; i++) {
            squares[8][i] = new AbaloneSquare(this, i, 8);
        }
    }
    
    /*
    private boolean move(int x, int y, int numberOfPieces, Dir direction, boolean broadside) {
        if (!broadside) {
            switch (direction) {
            case L : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x, y+i), getSquare(x, y-1+i));
            }
            break;
            case UL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i+1, y));
            }
            break;
            case DL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i-1, y+i-1));
            }
            break;
            case UR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i+1, y+i+1));
            }
            break;
            case DR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i-1, y));
            }
            break;
            case R : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x, y+i), getSquare(x, y+i+1));
            }
            break;
            }
        }
        else {
            switch (direction) {
            case L : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x, y-1+i));
            }
            break;
            case UL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i+1, y));
            }
            break;
            case DL : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i-1, y+i-1));
            }
            break;
            case UR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y+i), getSquare(x+i+1, y+i+1));
            }
            break;
            case DR : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x+i-1, y));
            }
            break;
            case R : for (int i = 0; i < numberOfPieces; i++) {
                movePiece(getPiece(x+i, y), getSquare(x, y+i+1));
            }
            break;
            }
        }
    
        return true;
    }
    */
    
    private void initPiecesStandard() {
        for (int i = 0; i < 5; i++) {
            new AbalonePiece(this, players.get(0), squares[0][i]);
        }
        for (int i = 0; i < 6; i++) {
            new AbalonePiece(this, players.get(0), squares[1][i]);
        }
        new AbalonePiece(this, players.get(0), squares[2][2]);
        new AbalonePiece(this, players.get(0), squares[2][3]);
        new AbalonePiece(this, players.get(0), squares[2][4]);
        new AbalonePiece(this, players.get(1), squares[6][6]);
        new AbalonePiece(this, players.get(1), squares[6][5]);
        new AbalonePiece(this, players.get(1), squares[6][4]);
        for (int i = 3; i < 9; i++) {
            new AbalonePiece(this, players.get(1), squares[7][i]);
        }
        for (int i = 4; i < 9; i++) {
            new AbalonePiece(this, players.get(1), squares[8][i]);
        }
    }

    private void initPiecesBelgianDaisy() {
        new AbalonePiece(this, players.get(0), squares[0][0]);
        new AbalonePiece(this, players.get(0), squares[1][0]);
        new AbalonePiece(this, players.get(0), squares[0][1]);
        new AbalonePiece(this, players.get(0), squares[1][1]);
        new AbalonePiece(this, players.get(0), squares[2][1]);
        new AbalonePiece(this, players.get(0), squares[1][2]);
        new AbalonePiece(this, players.get(0), squares[3][2]);

        new AbalonePiece(this, players.get(0), squares[8][8]);
        new AbalonePiece(this, players.get(0), squares[7][8]);
        new AbalonePiece(this, players.get(0), squares[8][7]);
        new AbalonePiece(this, players.get(0), squares[7][7]);
        new AbalonePiece(this, players.get(0), squares[6][7]);
        new AbalonePiece(this, players.get(0), squares[7][6]);
        new AbalonePiece(this, players.get(0), squares[6][6]);
        
        new AbalonePiece(this, players.get(1), squares[0][3]);
        new AbalonePiece(this, players.get(1), squares[1][3]);
        new AbalonePiece(this, players.get(1), squares[0][4]);
        new AbalonePiece(this, players.get(1), squares[1][4]);
        new AbalonePiece(this, players.get(1), squares[2][4]);
        new AbalonePiece(this, players.get(1), squares[1][5]);
        new AbalonePiece(this, players.get(1), squares[2][5]);
        
        new AbalonePiece(this, players.get(1), squares[7][3]);
        new AbalonePiece(this, players.get(1), squares[6][3]);
        new AbalonePiece(this, players.get(1), squares[8][4]);
        new AbalonePiece(this, players.get(1), squares[7][4]);
        new AbalonePiece(this, players.get(1), squares[6][4]);
        new AbalonePiece(this, players.get(1), squares[8][5]);
        new AbalonePiece(this, players.get(1), squares[7][5]);
        
    }
    
    private void initPiecesGermanDaisy() {
        new AbalonePiece(this, players.get(0), squares[1][0]);
        new AbalonePiece(this, players.get(0), squares[2][0]);
        new AbalonePiece(this, players.get(0), squares[1][1]);
        new AbalonePiece(this, players.get(0), squares[2][1]);
        new AbalonePiece(this, players.get(0), squares[3][1]);
        new AbalonePiece(this, players.get(0), squares[2][2]);
        new AbalonePiece(this, players.get(0), squares[3][2]);

        new AbalonePiece(this, players.get(0), squares[7][8]);
        new AbalonePiece(this, players.get(0), squares[6][8]);
        new AbalonePiece(this, players.get(0), squares[7][7]);
        new AbalonePiece(this, players.get(0), squares[6][7]);
        new AbalonePiece(this, players.get(0), squares[5][7]);
        new AbalonePiece(this, players.get(0), squares[6][6]);
        new AbalonePiece(this, players.get(0), squares[5][6]);
        
        new AbalonePiece(this, players.get(1), squares[1][4]);
        new AbalonePiece(this, players.get(1), squares[2][4]);
        new AbalonePiece(this, players.get(1), squares[1][5]);
        new AbalonePiece(this, players.get(1), squares[2][5]);
        new AbalonePiece(this, players.get(1), squares[3][5]);
        new AbalonePiece(this, players.get(1), squares[2][6]);
        new AbalonePiece(this, players.get(1), squares[3][6]);
        
        new AbalonePiece(this, players.get(1), squares[6][2]);
        new AbalonePiece(this, players.get(1), squares[5][2]);
        new AbalonePiece(this, players.get(1), squares[7][3]);
        new AbalonePiece(this, players.get(1), squares[6][3]);
        new AbalonePiece(this, players.get(1), squares[5][3]);
        new AbalonePiece(this, players.get(1), squares[7][4]);
        new AbalonePiece(this, players.get(1), squares[6][4]);
    }
    
    public enum Dir {
        L(-1,0,"L"), UL(0,1,"UL"), DL(-1,-1,"DL"), UR(1,1,"UR"), DR(0,-1,"DR"), R(1,0,"R");

        int dx, dy;
        String name;
        Dir(int dx, int dy, String name) {
            this.dx = dx;
            this.dy = dy;
            this.name = name;
        }
    }
}

