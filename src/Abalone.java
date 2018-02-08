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
    private int turns = 0;
    private Scanner console = new Scanner(System.in);
    AbaloneSquare[][] squares = new AbaloneSquare[9][9];
    private ActionListener p1timerTick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.timeTaken += 0.1;
            updateGUIs();
        }
        
    };
    private Timer p1timer = new Timer(100, p1timerTick);
    private ActionListener p2timerTick = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player2.timeTaken += 0.1;
            updateGUIs();
        }
        
    };
    private Timer p2timer = new Timer(100, p2timerTick);
    private Timer lastRunningTimer = p1timer;
    private AbalonePlayer player1 = new AbalonePlayer(P1_COLOR, this);
    private AbalonePlayer player2 = new AbalonePlayer(P2_COLOR, this);
    int[] selection1 = {-1, -1};
    int[] selection2 = {-1, -1};
    private boolean gameRunning = false;
    Dir directionSelection;
    
    public static void main(String[] args) {
        new Abalone();
    }
    
    // switches the current player of the game;
    @Override
    public void nextPlayerTurn() {
        super.nextPlayerTurn();
        switchTimers();
        turns++;
        updateGUIs();
        
    }
    
    // constructor which initializes the board and begins listening for user input
    public Abalone() {
        this.addPlayer(player1);
        this.addPlayer(player2);

        this.initSquares();
        this.initGUIs();
        this.initPiecesStandard();
        
        p1timer.setRepeats(true);
        p2timer.setRepeats(true);
        
        while (console.hasNextLine()) {
            if (this.processInput(console.nextLine())) {
                //System.out.println("Player turn: " + getCurPlayer());
            }
            updateGUIs();
        }
        
        
    }
    
    // front end method to make a move, returns true if the move was successful
    // returns false if the game is currently paused or stopped
    // or if the input coordinates are out of bounds or do not exist
    // or if the direction given is null
    // or if there is an invalid selection of pieces between coordinates given
    // or if the the destination squares are out of bounds or do not exist
    public boolean move(int x1, int y1, int x2, int y2, Dir dir) {
        boolean moveSuccess = false;
        
        // game running
        if (!gameRunning) {
            System.out.println("Game not running");
            return false;
        }
        
        // check move is in bounds and direction is not null
        if (!checkValidMoveInput(x1, y1, x2, y2, dir)) {
            System.out.println("out of bounds");
            return false;
        }
        
        // get pieces between coordinates. they must all be friendly and there
        // must be no more than 3 pieces, otherwise null is returned and function
        // exits with false
        ArrayList<AbalonePiece> pieces = getPieces(x1, y1, x2, y2);
        if (pieces == null) {
            System.out.println("invalid pieces");
            return false;
        }
        
        // get squares to move to 
        ArrayList<AbaloneSquare> toSquares = getSquaresToMoveTo(pieces, dir);
        
        // squares you're moving to cannot be occupied by friendlies
        if (this.checkSquaresOccupiedByFriendly(pieces, toSquares)) {
            System.out.println("friendlies in destination");
            return false;
        }
        
        // pushing move
        if (!isInlineMove(x1, y1, x2, y2, dir)) {
            int numberOfPieces = pieces.size();
        } else {
            
        }
    
        // move pieces
        moveSuccess = movePieces(pieces, toSquares);

        // Next player turn
        if (moveSuccess) {
            nextPlayerTurn();
        }
        return moveSuccess;
    }

    // processes input from System.in
    private boolean processInput(String input) {
        String lowerInput = input.toLowerCase();
        
        if (lowerInput.equals("reset")) {
            clearBoard();
            stopTimers();
            resetTurns();
            return true;
        } else if (lowerInput.equals("standard")) {
            clearBoard();
            stopTimers();
            resetTurns();
            initPiecesStandard();
            return true;
        } else if (lowerInput.equals("belgian")) {
            clearBoard();
            stopTimers();
            resetTurns();
            initPiecesBelgianDaisy();
            return true;
        } else if (lowerInput.equals("german")) {
            clearBoard();
            stopTimers();
            resetTurns();
            initPiecesGermanDaisy();
            return true;
        } else if (lowerInput.equals("pause")) {
            pauseTimers();
            return true;
        } else if (lowerInput.equals("resume") || lowerInput.equals("start")) {
            resumeTimers();
            return true;
        } else {
            /*
            int x1, y1, x2, y2;
            String[] parsed = input.split(" ");
            try {
                x1 = Integer.parseInt(parsed[0]);
                y1 = Integer.parseInt(parsed[1]);
                x2 = Integer.parseInt(parsed[2]);
                y2 = Integer.parseInt(parsed[3]);
            } catch (NumberFormatException ex) {
                return false;
            }
            
            Dir dir = null;
            for (Dir direction : Dir.values()) {
                if (direction.name.equals(input.trim())) {
                    dir = direction;
                }
            }
            System.out.println("!! " + selection1[0] + " " + selection1[0] + " " + selection2[0] + " " + selection2[1] + " "  + dir);
            
            boolean result = move(selection1[0], selection1[1], selection2[0], selection2[1], dir);
            clearSelection();
            return result;
            */
        }
        return false;
    }

    // move all pieces in the array of pieces to the squares in array of squares
    // PRE: size of both arrays are equal
    private boolean movePieces(ArrayList<AbalonePiece> pieces, ArrayList<AbaloneSquare> toSquares) {
        boolean result = false;
        for (int i = 0; i < pieces.size(); i++) {
            movePiece(pieces.get(i), toSquares.get(i));
            result = true;
        }
        return result;
    }
    
    // returns an array of squares which an array of pieces would be moving to given a direction
    private ArrayList<AbaloneSquare> getSquaresToMoveTo(ArrayList<AbalonePiece> pieces, Dir dir) {
        ArrayList<AbaloneSquare> toSquares = new ArrayList<AbaloneSquare>();
        for (AbalonePiece piece : pieces) {
            if (piece != null) {
                int x = ((AbaloneSquare)piece.getPosition()).x + dir.dx;
                int y = ((AbaloneSquare)piece.getPosition()).y + dir.dy;
                toSquares.add(getSquare(x,y));
            }
        }
        return toSquares;
    }
    
    // initializes the gui's for player 1 and player 2
    private void initGUIs() {
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
        frame1.revalidate();
        frame1.repaint();
    }
    
    // pause timers
    private void pauseTimers() {
        this.lastRunningTimer = p1timer.isRunning() ? p1timer : p2timer;
        lastRunningTimer.stop();
        gameRunning = false;
        // stop ai's TODO
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
    
    // timers revert to 0 and stop. should be followed up with a reset of the board.
    // player 1 timer will be the one to resume upon resuming
    private void stopTimers() {
        p1timer.restart();
        p2timer.restart();
        p1timer.stop();
        p2timer.stop();
        ((AbalonePlayer)getPlayers().get(0)).timeTaken = 0;
        ((AbalonePlayer)getPlayers().get(1)).timeTaken = 0;
        lastRunningTimer = p1timer;
        gameRunning = false;
        resetTurns();

        updateGUIs();
        // stop ai's TODO
    }

    private void resetTurns() {
        turns = 0;
    }

    // resumes timers
    private void resumeTimers() {
        lastRunningTimer.start();
        gameRunning = true;
    }
    
    // switches which timer is running between player1 and player2
    private void switchTimers() {
        if (p1timer.isRunning()) {
            p1timer.stop();
            p2timer.start();
        } else {
            p2timer.stop();
            p1timer.start();
        }
    }

    // determines if a move of pieces between x1,y1 and x2,y2 moving in dir direction
    // is an inline move or not
    private boolean isInlineMove(int x1, int y1, int x2, int y2, Dir dir) {
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
    
    // checks if the squares x1,y1 and x2,y2 exist and that dir is not null
    private boolean checkValidMoveInput(int x1, int y1, int x2, int y2, Dir dir) {
        System.out.println("" + x1 + y1 + x2 + y2 + dir);
        System.out.println(getSquare(x1, y1) + " " + getSquare(x2, y2));
        return dir != null && getSquare(x1, y1) != null && getSquare(x2, y2) != null;
    }
    
    // returns the square at coordinates x,y returns null if such a square does not
    // exist, such as coordinate 0,8 or if the coordinates are out of bounds
    private AbaloneSquare getSquare(int x, int y) {
        if (!inBounds(x, y)) {
            System.out.println("coordinates not in bounds");
            return null;
        }
        return squares[y][x];
    }

    // moves a piece from one square to another. if the square does not exist, the
    // piece is removed and the owner's outs increment
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

    // checks if coordinates x and y are in bounds 
    private boolean inBounds(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 8;
    }
    
    // returns the piece at coordinates x,y. returns null if no piece
    // exists or the square at x,y does not exist or x,y is out of bounds
    private AbalonePiece getPiece(int x, int y) {
        if (getSquare(x, y) == null) {
            return null;
        }
        return (AbalonePiece)getSquare(x,y).getPiece();
    }
    
    /*
    public void processMove(AbaloneMove move) {
        move(move.piece_x, move.piece_y, move.numberOfPieces, move.direction, move.broadside);
    }
    */
    
    // returns an array of pieces which are located between x1,y1 and x2,y2
    // returns null if there are more than 3 pieces selected
    // or if any of the squares between are not occupied by a piece belonging to
    // the player whose turn it currently is
    private ArrayList<AbalonePiece> getPieces(int x1, int y1, int x2, int y2) {
        
        ArrayList<AbalonePiece> result = new ArrayList<AbalonePiece>();
        int dx = (x1 - x2);
        int dy = (y1 - y2);
        int ddx = getIncrement(dx);
        int ddy = getIncrement(dy);
        if (dx > 2 || dy > 2 || dx < -2 || dy < -2) {
            System.out.println("too many");
            return null;
        }
        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            if (getPiece(x1 + ddx * i, y1 + ddy * i) == null ||
                    getPiece(x1 + ddx * i, y1 + ddy * i).getOwner() != getCurPlayer()) {
                System.out.println("empty or unfriendly piece" + x1 + ddx + i + " " + 
                    y1 + ddy + i);
                return null;
            }
            result.add(getPiece(x1 + ddx * i, y1 + ddy * i));
        }
        
        return result;
    }
    
    // returns the sign of x. used to determine which direction to incrementally
    // search when searching between coordinates for pieces
    private int getIncrement(int x) {
        if (x > 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }
    
    // used to determine if a move is legal because pieces can never move to a
    // space currently occupied by a friendly piece unless that friendly piece
    // will also be moving
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

    // initializes the squares in an 9x9 array. only coordinates within the
    // hexagonal board of Abalone will contain squares, the rest will be null
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
    
    // removes all pieces from the board
    private void clearBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (squares[i][j] != null && squares[i][j].getPiece() != null) {
                    squares[i][j].getPiece().remove();
                }
            }
        }
        updateGUIs();
    }
    
    // places pieces on the board as per standard Abalone layout
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
        updateGUIs();
    }

    // places pieces on the board as per belgian daisy layout
    private void initPiecesBelgianDaisy() {
        new AbalonePiece(this, players.get(0), squares[0][0]);
        new AbalonePiece(this, players.get(0), squares[1][0]);
        new AbalonePiece(this, players.get(0), squares[0][1]);
        new AbalonePiece(this, players.get(0), squares[1][1]);
        new AbalonePiece(this, players.get(0), squares[2][1]);
        new AbalonePiece(this, players.get(0), squares[1][2]);
        new AbalonePiece(this, players.get(0), squares[2][2]);

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
        updateGUIs();
        
    }
    
    // places pieces on the board as per german daisy layout
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
        updateGUIs();
    }
    
    // clears all currently selected squares and directions
    public void clearSelection() {
        selection1[0] = -1;
        selection1[1] = -1;
        selection2[0] = -1;
        selection2[1] = -1;
        directionSelection = null;
    }
    
    // returns the number of turns that have been taken
    public int getTurns() {
        return turns;
    }

    // enumeration of Directions
    // each direction has a delta x and delta y which represent the difference in x
    // and y which result from moving in that direction
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

