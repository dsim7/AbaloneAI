import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.Timer;

import game.Game;
import game.GamePiece;
import game.GamePosition;

/*
 * TODO:
 * -after reset movement
 * -time per move counter display
 * -bigger outs
 * -save to times to external
 */

public class Abalone extends Game {
    static final int PVP = 0;
    static final int PVC = 1;
    static final int CVC = 2;
    static final int FRAME_WIDTH = 1000;
    static final int FRAME_HEIGHT = 1000;
    static final Color P1_COLOR = Color.RED;
    static final Color P2_COLOR = Color.BLUE;
    static final Font INFO_FONT = new Font("Arial",Font.BOLD, 20 );
    static final DecimalFormat FORMAT = new DecimalFormat("0.0");
    
    static final AbaloneCoord[] P1_STANDARD = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    static final AbaloneCoord[] P2_STANDARD = {
        new AbaloneCoord(4, 8), new AbaloneCoord(5, 8),
        new AbaloneCoord(6, 8), new AbaloneCoord(7, 8),
        new AbaloneCoord(8, 8), new AbaloneCoord(3, 7),
        new AbaloneCoord(4, 7), new AbaloneCoord(5, 7),
        new AbaloneCoord(6, 7), new AbaloneCoord(7, 7),
        new AbaloneCoord(8, 7), new AbaloneCoord(4, 6),
        new AbaloneCoord(5, 6), new AbaloneCoord(6, 6),
    };
    
    static final AbaloneCoord[] P1_BELGIAN = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    static final AbaloneCoord[] P2_BELGIAN = {
        new AbaloneCoord(4, 8), new AbaloneCoord(5, 8),
        new AbaloneCoord(6, 8), new AbaloneCoord(7, 8),
        new AbaloneCoord(8, 8), new AbaloneCoord(3, 7),
        new AbaloneCoord(4, 7), new AbaloneCoord(5, 7),
        new AbaloneCoord(6, 7), new AbaloneCoord(7, 7),
        new AbaloneCoord(8, 7), new AbaloneCoord(4, 6),
        new AbaloneCoord(5, 6), new AbaloneCoord(6, 6),
    }; 
    
    static final AbaloneCoord[] P1_GERMAN = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    static final AbaloneCoord[] P2_GERMAN = {
        new AbaloneCoord(4, 8), new AbaloneCoord(5, 8),
        new AbaloneCoord(6, 8), new AbaloneCoord(7, 8),
        new AbaloneCoord(8, 8), new AbaloneCoord(3, 7),
        new AbaloneCoord(4, 7), new AbaloneCoord(5, 7),
        new AbaloneCoord(6, 7), new AbaloneCoord(7, 7),
        new AbaloneCoord(8, 7), new AbaloneCoord(4, 6),
        new AbaloneCoord(5, 6), new AbaloneCoord(6, 6),
    };
    
    
    private int maxTurns = 0;
    private double maxTimePerTurn = 0;
    private Scanner console = new Scanner(System.in);
    //AbaloneSquare[][] squares = new AbaloneSquare[9][9];
    
    private Timer p1timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player1.timeTaken += 0.1;
            gui.updateInfo();
        }
    });
    private Timer p2timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player2.timeTaken += 0.1;
            gui.updateInfo();
        }
    });
    private Timer maxTurnTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            nextPlayerTurn();
            System.out.println("Turn is up");
            gui.updateInfo();
        }
    });
    private KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ESCAPE) {
                System.out.println("Selection Cleared");
                clearSelection();
            }
        }
    };

    
    private Timer lastRunningTimer = p1timer;
    AbalonePlayer player1 = new AbalonePlayer(0, P1_COLOR, this);
    AbalonePlayer player2 = new AbalonePlayer(1, P2_COLOR, this);
    
    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private double timeAtTurnStart = 0;
    private AbaloneMove moveThisTurn;
    private boolean settingTurns = false;
    private boolean settingTimePerTurn = false;
    Dir directionSelection;
    

    
    AbaloneState state;
    AbaloneGUI gui;
    AbaloneCoord selection1, selection2;
    AbaloneCoord[][] board = new AbaloneCoord[9][9];
    
    
    public static void main(String[] args) {
        new Abalone();
    }
    
    // constructor which initializes the board and begins listening for user input
    public Abalone() {
        this.addPlayer(player1);
        this.addPlayer(player2);
    
        this.initSquares();
        this.initGUIs();
        //this.initStateStandard();
        
        p1timer.setRepeats(true);
        p2timer.setRepeats(true);
        
        while (console.hasNextLine()) {
            if (this.processInput(console.nextLine())) {
                //System.out.println("Player turn: " + getCurPlayer());
            }
            updateGUI();
        }
        
        
    }

    public boolean getCanClick() {
        return !gameOver && gameRunning && gameStarted;
    }
    
    // switches the current player of the game;
    public void nextTurn() {
        printTurnInfo();
        
        switchTimers();
        
        checkMaxTurns();
        
        updateGUI();
        
        // ai
        /*
        AbalonePlayer player = (AbalonePlayer) getCurPlayer();
        if (player.isAI) {
            AbaloneMove move = player.ai.getNextMove(squares);
            if (move != null) {
                executeAIMove(move);
            }
        }
        */
        
    }
    
    private void checkMaxTurns() {
        if (state.turn > maxTurns) {
            stopTimers();
        }
    }
    /*
    public boolean setState(AbaloneState state) {
        for (AbaloneCoord coord : state.p1Pieces) {
            new AbalonePiece(this, players.get(0), squares[coord.y][coord.x]);
        }
        for (AbaloneCoord coord : state.p2Pieces) {
            new AbalonePiece(this, players.get(1), squares[coord.y][coord.x]);
        }
        setCurPlayer(getPlayers().get(state.turn % 2));
        return false;
    }
    */
    
    // front end method to make a move, returns true if the move was successful
    // returns false if the game is currently paused or stopped
    // or if the input coordinates are out of bounds or do not exist
    // or if the direction given is null
    // or if there is an invalid selection of pieces between coordinates given
    // or if the the destination squares are not available to be occupied
    // or if an attempted push fails.
    // switches turns on a successful move
    /*
    public boolean move(int x1, int y1, int x2, int y2, Dir dir) {
        boolean moveSuccess = false;
        
        // game running
        if (!gameRunning || gameOver) {
            System.out.println("Game has not started");
            return false;
        }
        
        // check move is in bounds and direction is not null
        if (!checkValidMoveInput(x1, y1, x2, y2, dir)) {
            //System.out.println("out of bounds or direction null");
            return false;
        }
        
        // get pieces between coordinates. they must all be friendly and there
        // must be no more than 3 pieces, otherwise null is returned and function
        // exits with false
        ArrayList<AbalonePiece> pieces = getPieces(x1, y1, x2, y2);
        if (pieces == null) {
            //System.out.println("invalid pieces");
            return false;
        }
        
        // get squares to move to 
        ArrayList<AbaloneSquare> toSquares = getSquaresToMoveTo(pieces, dir);
        
        // squares you're moving to cannot be occupied by friendlies
        if (Abalone.checkSquaresOccupiedByFriendly(pieces, toSquares, (AbalonePlayer)getCurPlayer())) {
            //System.out.println("friendlies in destination");
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
            this.moveThisTurn = new AbaloneMove(x1, y1, x2, y2, dir);
            nextPlayerTurn();
        }
        return moveSuccess;
    }

    private boolean checkWinner() {
        if (player1.outs >= 6) {
            System.out.println("Winner: Player 2");
            return true;
        } else if (player2.outs >= 6) {
            System.out.println("Winner: Player 1");
            return true;
        }
        return false;
    }

    */
    
    // processes input from System.in
    private boolean processInput(String input) {
        String lowerInput = input.toLowerCase();
        if (lowerInput.equals("stop")) { //stops the game and restarts all timers. game is over
            if (gameStarted) {
                stopTimers();
            }
            return true;
        } else if (lowerInput.equals("reset")) {
            if (gameOver) {
                resetTimers();
                return true;
            } else {
                System.out.println("Stop the game first");
            }
        } else if (lowerInput.equals("standard")) {
        
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_STANDARD, P2_STANDARD);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        } else if (lowerInput.equals("belgian")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_BELGIAN, P2_BELGIAN);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        } else if (lowerInput.equals("german")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_GERMAN, P2_GERMAN);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        } else if (lowerInput.equals("pause")) {
            if (!gameOver && gameStarted && gameRunning) {
                pauseTimers();
            }
            return true;
        } else if (lowerInput.equals("resume") || lowerInput.equals("start")) {
            if (!gameOver) {
                resumeTimers();
            }
            return true;
        } else if (lowerInput.equals("pvp")) {
            if (!gameStarted) {
                System.out.println("Setting Player Vs Player");
                setAI(PVP);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("pvc")) {
            System.out.println("Setting Player Vs Computer");
            if (!gameStarted) {
                setAI(PVC);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("cvc")) {
            System.out.println("Setting Computer vs Computer");
            if (!gameStarted) {
                setAI(CVC);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("redfirst")) {
            if (!gameStarted) {
                setRedGoesFirst(true);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("bluefirst")) {
            if (!gameStarted) {
                setRedGoesFirst(false);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("turns")) {
            if (!gameStarted) {
                System.out.println("Enter number of Turns");
                settingTurns = true;
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("time")) {
            if (!gameStarted) {
                System.out.println("Enter Time per turn");
                settingTimePerTurn = true;
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else {
            try {
                int intinput = Integer.parseInt(lowerInput);
                if (settingTurns) {
                    maxTurns = intinput;
                    System.out.println("Max Turns: " + maxTurns);
                } else if (settingTimePerTurn) {
                    maxTimePerTurn = intinput;
                    maxTurnTimer.setInitialDelay(intinput * 1000 + 150);
                    maxTurnTimer.setDelay(intinput * 1000 + 150);
                    System.out.println("Max Time Per Turn: " + maxTimePerTurn);
                } else {
                    System.out.println("Invalid input");
                }
            } catch (Exception e) {
                System.out.println("Invalid input");
            }
            settingTurns = false;
            settingTimePerTurn = false;
        }
        return false;
    }

    private boolean executeAIMove(AbaloneMove move) {
        return true;
    }
    
    private void printTurnInfo() {
        if (moveThisTurn != null) {
            System.out.println("Player " + (getPlayers().indexOf(getCurPlayer()) + 1) + 
                    ": " + moveThisTurn.x1 + 
                    "," + moveThisTurn.y1 +
                    " to " + moveThisTurn.x2 + 
                    "," + moveThisTurn.y2 + 
                    " moving " + moveThisTurn.direction + 
                    " in " + FORMAT.format(getTimeTakenThisTurn()) + "s");
        }
    }
    
    // get the time that has elapsed this turn
    private double getTimeTakenThisTurn() {
        return ((AbalonePlayer)getCurPlayer()).timeTaken - timeAtTurnStart;
        
    }
    
    
    
    

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

    // move all pieces in the array of pieces to the squares in array of squares
    // PRE: size of both arrays are equal
    /*
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
    */
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
    
    // if the game has not started, determines whether red will move first or not
    private void setRedGoesFirst(boolean b) {
        if (!b) {
            System.out.println("Setting Blue moves first");
            this.setCurPlayer(getPlayers().get(1));
            this.lastRunningTimer = p2timer;
        } else {
            System.out.println("Setting Red moves first");
            this.setCurPlayer(getPlayers().get(0));
            this.lastRunningTimer = p1timer;
        }
    }

    // sets whether it is PVP, PVC, or CVC
    private void setAI(int x) {
        AbalonePlayer p1 = (AbalonePlayer) getPlayers().get(0);
        AbalonePlayer p2 = (AbalonePlayer) getPlayers().get(1);
        switch(x) {
        case 0 :
            p1.isAI = false;
            p2.isAI = false;
            break;
        case 1 : 
            p1.isAI = false;
            p2.isAI = true;
            break;
        case 2 :
            p1.isAI = true;
            p2.isAI = true;
            break;
        }
        
    }

    // pause timers
    private void pauseTimers() {
        System.out.println("Pausing");
        this.lastRunningTimer = p1timer.isRunning() ? p1timer : p2timer;
        lastRunningTimer.stop();
        maxTurnTimer.stop();
        gameRunning = false;
        // stop ai's TODO
    }
    
    // timers revert to 0 and stop. should be followed up with a reset of the board.
    // player 1 timer will be the one to resume upon resuming
    private void stopTimers() {
        System.out.println("Game Over");
        p1timer.restart();
        p2timer.restart();
        maxTurnTimer.restart();
        p1timer.stop();
        p2timer.stop();
        maxTurnTimer.stop();
        lastRunningTimer = p1timer;
        gameRunning = false;
        gameStarted = false;
        gameOver = true;

        gui.updateInfo();
        // stop ai's TODO
    }

    private void resetTimers() {
        System.out.println("Reseting");
        player1.timeTaken = 0;
        player2.timeTaken = 0;
        player1.outs = 0;
        player2.outs = 0;
        maxTimePerTurn = 0;
        maxTurns = 0;
        gameOver = false;
        gameStarted = false;
        clearBoard();
    }

    // resumes timers
    private void resumeTimers() {
        System.out.println("Starting/Resuming");
        if (!gameStarted) {
            if (maxTimePerTurn != 0) {
                maxTurnTimer.start();
            }
            gameStarted = true;
        }
        lastRunningTimer.start();
        gameRunning = true;
        
    }
    
    // switches which timer is running between player1 and player2
    private void switchTimers() {
        timeAtTurnStart = ((AbalonePlayer)getCurPlayer()).timeTaken;
        
        if (p1timer.isRunning()) {
            p1timer.stop();
            p2timer.start();
        } else {
            p2timer.stop();
            p1timer.start();
        }
        
        if (maxTimePerTurn != 0) {
            maxTurnTimer.restart();
        }
    }

    // removes all pieces from the board
    private void clearBoard() {
        /*
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (squares[i][j] != null && squares[i][j].getPiece() != null) {
                    squares[i][j].getPiece().remove();
                }
            }
        }
        */
        state = null;
        updateGUI();
    }

    /*
    // returns the square at coordinates x,y returns null if such a square does not
    // exist, such as coordinate 0,8 or if the coordinates are out of bounds
    private AbaloneSquare getSquare(int x, int y) {
        if (!inBounds(x, y)) {
            //System.out.println("coordinates not in bounds");
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
                System.out.println("Piece out");
                ((AbalonePlayer)piece.getOwner()).outs++;
            }
            piece.move(pos);
        }
    }

    // returns the piece at coordinates x,y. returns null if no piece
    // exists or the square at x,y does not exist or x,y is out of bounds
    private AbalonePiece getPiece(int x, int y) {
        if (getSquare(x, y) == null) {
            return null;
        }
        return (AbalonePiece)getSquare(x,y).getPiece();
    }
    */
    /*
    public void processMove(AbaloneMove move) {
        move(move.piece_x, move.piece_y, move.numberOfPieces, move.direction, move.broadside);
    }
    */
    /*
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
            //System.out.println("too many");
            return null;
        }
        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            if (getPiece(x1 + ddx * i, y1 + ddy * i) == null ||
                    getPiece(x1 + ddx * i, y1 + ddy * i).getOwner() != getCurPlayer()) {
                //System.out.println("empty or unfriendly piece" + x1 + ddx + i + " " + 
                //    y1 + ddy + i);
                return null;
            }
            result.add(getPiece(x1 + ddx * i, y1 + ddy * i));
        }
        
        return result;
    }
    
    //====== THESE FUNCTIONS ARE TO BE MOVED TO ABALONEAI======== //
    
    // determines if a move of pieces between x1,y1 and x2,y2 moving in dir direction
    // is an inline move or not
    boolean isInlineMove(int x1, int y1, int x2, int y2, Dir dir) {
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
    boolean checkValidMoveInput(int x1, int y1, int x2, int y2, Dir dir) {
        //System.out.println("" + x1 + y1 + x2 + y2 + dir);
        //System.out.println(getSquare(x1, y1) + " " + getSquare(x2, y2));
        return dir != null && inBounds(x1, y1) && inBounds(x2, y2);
    }

    // checks if coordinates x and y are in bounds 
    boolean inBounds(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 8;
    }

    List<AbaloneCoord> getOriginCoords(int x1, int y1, int x2, int y2, Dir dir) {
        List<AbaloneCoord> result = new ArrayList<AbaloneCoord>();
        int dx = (x1 - x2);
        int dy = (y1 - y2);
        int ddx = getIncrement(dx);
        int ddy = getIncrement(dy);
        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            AbaloneCoord newCoord = new AbaloneCoord(x1 + ddx * i, y1 + ddy * i);
            result.add(newCoord);
        }
        return result;
    }
    
    List<AbaloneCoord> getDestCoords(List<AbaloneCoord> origin, Dir dir) {
        List<AbaloneCoord> result = new ArrayList<AbaloneCoord>();
        for (AbaloneCoord coord : origin) {
            coord.x += dir.dx;
            coord.y += dir.dy;
        }
        return result;
    }
    
    boolean checkAllContained(List<AbaloneCoord> set, List<AbaloneCoord> playerPieces) {
        for (AbaloneCoord coord : set) {
            if (!playerPieces.contains(coord)) {
                return false;
            }
        }
        return true;
    }
    
    boolean checkDestUnoccupied(List<AbaloneCoord> origin, List<AbaloneCoord> dest,
            List<AbaloneCoord> playerPieces) {
        for (AbaloneCoord coord : dest) {
            if (!origin.contains(coord) && playerPieces.contains(coord)) {
                return false;
            }
        }
        return true;
    }
    
    boolean checkSetSize(List<AbaloneCoord> set) {
        return set.size() < 4;
    }
    
    //private 
    
    // returns the sign of x. used to determine which direction to incrementally
    // search when searching between coordinates for pieces
    int getIncrement(int x) {
        if (x > 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }
    

    //============== //
    

    private AbalonePiece getPiece(AbaloneCoord coord) {
        return (AbalonePiece)getSquare(coord).getPiece();
    }
    
    private AbaloneSquare getSquare(AbaloneCoord coord) {
        return squares[coord.y][coord.x];
    }


    // used to determine if a move is legal because pieces can never move to a
    // space currently occupied by a friendly piece unless that friendly piece
    // will also be moving
    static private boolean checkSquaresOccupiedByFriendly(ArrayList<AbalonePiece> pieces, ArrayList<AbaloneSquare> squares,
            AbalonePlayer player) {
        for (AbaloneSquare sq : squares) {
            if (sq != null) {
                if (sq.containsFriendly(player) && !pieces.contains(sq.getPiece())) {
                    return true;
                }
            }
        }
        return false;
    }
    
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
        /*
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
        */
        // ==================== //
        
        // 1
        for (int i = 0; i < 5; i++) {
            board[0][i] = new AbaloneCoord(i, 0);
        }
        
        // 2
        for (int i = 0; i < 6; i++) {
            board[1][i] = new AbaloneCoord(i, 1);
        }
        
        // 3
        for (int i = 0; i < 7; i++) {
            board[2][i] = new AbaloneCoord(i, 2);
        }
        
        // 4
        for (int i = 0; i < 8; i++) {
            board[3][i] = new AbaloneCoord(i, 3);
        }
        
        // 5
        for (int i = 0; i < 9; i++) {
            board[4][i] = new AbaloneCoord(i, 4);
        }
        
        // 6
        for (int i = 1; i < 9; i++) {
            board[5][i] = new AbaloneCoord(i, 5);
        }
        
        // 7
        for (int i = 2; i < 9; i++) {
            board[6][i] = new AbaloneCoord(i, 6);
        }
        
        // 8
        for (int i = 3; i < 9; i++) {
            board[7][i] = new AbaloneCoord(i, 7);
        }
        
        // 9
        for (int i = 4; i < 9; i++) {
            board[8][i] = new AbaloneCoord(i, 8);
        }
    }
    
    private void initState(AbaloneCoord[] p1array, AbaloneCoord[] p2array) {
        List<AbaloneCoord> p1PiecesStandard = Arrays.asList(p1array);
        List<AbaloneCoord> p2PiecesStandard = Arrays.asList(p2array);
        state = new AbaloneState(p1PiecesStandard, p2PiecesStandard, 1);
        updateGUI();
    }

    /*
    // places pieces on the board as per standard Abalone layout
    private void initPiecesStandard() {
        System.out.println("Standard layout");
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
        System.out.println("Belgian Daisy layout");
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
        System.out.println("German Daisy layout");
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
    
    */
    // initializes the gui's for player 1 and player 2
    private void initGUIs() {
        
        gui = new AbaloneGUI(this);
        
        JFrame frame1 = new JFrame();
        frame1.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame1.setVisible(true);
        
        frame1.add(gui);
        frame1.setFocusable(true);
        frame1.addKeyListener(keyListener);
        frame1.revalidate();
        frame1.repaint();
        /*
        JFrame frame2 = new JFrame();
        frame2.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setVisible(true);
        
        frame2.add(new AbaloneGUI(this, player2));
        frame1.revalidate();
        frame1.repaint();
        */
    }
    
    private void updateGUI() {
        gui.updateState(state);
    }
    
    private void updateGUIInfo() {
        gui.updateInfo();
    }
    
    public boolean move(AbaloneCoord coord1, AbaloneCoord coord2, Dir dir) {
        System.out.println(coord1);
        System.out.println(coord2);
        System.out.println(dir);
        
        // TODO update state with new state as a result of the move
        
        return false;
    }

    // clears all currently selected squares and directions
    public void clearSelection() {
        selection1 = null;
        selection2 = null;
        directionSelection = null;
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

