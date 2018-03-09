package abalone;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;


/*
 * TODO:
 * -after reset movement
 * -time per move counter display
 * -bigger outs
 * -save to times to external
 */


/**
 * Center of the Abalone game. Contains configurations for the game as it is
 * played, timers for each player, and an association with a GUI. An Abalone
 * game processes command line commands to configure the game.
 * 
 * @author dylan
 *
 */
public class Abalone {
    public static final int PVP = 0;
    public static final int PVC = 1;
    public static final int CVC = 2;
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 1000;
    public static final Color P1_COLOR = Color.RED;
    public static final Color P2_COLOR = Color.BLUE;
    public static final Font INFO_FONT = new Font("Arial",Font.BOLD, 20 );
    public static final DecimalFormat FORMAT = new DecimalFormat("0.0");
    
    public static final AbaloneCoord[] P1_STANDARD = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    public static final AbaloneCoord[] P2_STANDARD = {
        new AbaloneCoord(4, 8), new AbaloneCoord(5, 8),
        new AbaloneCoord(6, 8), new AbaloneCoord(7, 8),
        new AbaloneCoord(8, 8), new AbaloneCoord(3, 7),
        new AbaloneCoord(4, 7), new AbaloneCoord(5, 7),
        new AbaloneCoord(6, 7), new AbaloneCoord(7, 7),
        new AbaloneCoord(8, 7), new AbaloneCoord(4, 6),
        new AbaloneCoord(5, 6), new AbaloneCoord(6, 6),
    };
    
    public static final AbaloneCoord[] P1_BELGIAN = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    public static final AbaloneCoord[] P2_BELGIAN = {
        new AbaloneCoord(4, 8), new AbaloneCoord(5, 8),
        new AbaloneCoord(6, 8), new AbaloneCoord(7, 8),
        new AbaloneCoord(8, 8), new AbaloneCoord(3, 7),
        new AbaloneCoord(4, 7), new AbaloneCoord(5, 7),
        new AbaloneCoord(6, 7), new AbaloneCoord(7, 7),
        new AbaloneCoord(8, 7), new AbaloneCoord(4, 6),
        new AbaloneCoord(5, 6), new AbaloneCoord(6, 6),
    }; 
    
    public static final AbaloneCoord[] P1_GERMAN = {
        new AbaloneCoord(0, 0), new AbaloneCoord(1, 0),
        new AbaloneCoord(2, 0), new AbaloneCoord(3, 0),
        new AbaloneCoord(4, 0), new AbaloneCoord(0, 1),
        new AbaloneCoord(1, 1), new AbaloneCoord(2, 1),
        new AbaloneCoord(3, 1), new AbaloneCoord(4, 1),
        new AbaloneCoord(5, 1), new AbaloneCoord(2, 2),
        new AbaloneCoord(3, 2), new AbaloneCoord(4, 2)
    };

    public static final AbaloneCoord[] P2_GERMAN = {
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
            updateGUIInfo();
        }
    });
    private Timer p2timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            player2.timeTaken += 0.1;
            updateGUIInfo();
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

    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private double timeAtTurnStart = 0;
    private AbaloneMove moveThisTurn;
    private boolean settingTurns = false;
    private boolean settingTimePerTurn = false;
    Dir directionSelection;
    
    private AbaloneGUI gui;

    AbaloneState state;
    AbalonePlayer player1 = new AbalonePlayer(0, P1_COLOR, this);
    AbalonePlayer player2 = new AbalonePlayer(1, P2_COLOR, this);
    AbalonePlayer curPlayer = player1;
    AbaloneCoord selection1, selection2;
    AbaloneCoord[][] board = new AbaloneCoord[9][9];
    
    private AbaloneState testState;
    
    public static void main(String[] args) {
        if (args.length > 0) {
            String inputFilename = args[0];
            String boardOutFilename = "Test" + args[0].charAt(4) + ".board";
            String moveOutFilename = "Test" + args[0].charAt(4) + ".moves";
            new Abalone(new File(inputFilename), new File(boardOutFilename), new File(moveOutFilename));
        } else {
            new Abalone();
        }
    }
    
    // constructor which initializes the board and begins listening for user input
    public Abalone() {    
        this.initSquares();
        this.initGUIs();
        
        p1timer.setRepeats(true);
        p2timer.setRepeats(true);
        
        while (console.hasNextLine()) {
            if (this.processInput(console.nextLine())) {
                //System.out.println("Player turn: " + getCurPlayer());
            }
            updateGUI();
        }   
    }
    
    public Abalone(File inputFile, File boardoutFile, File moveoutfile) {
        System.out.println("Testing file");
        this.readTestFile(inputFile);
        this.printResults(boardoutFile, moveoutfile);
    }

    /**
     * Determines whether the game is ready for users to click pieces and play the game.
     * 
     * @return true if the game is not over, the game has started, and the game is running
     */
    public boolean getCanClick() {
        return !gameOver && gameRunning && gameStarted;
    }
    
    /**
     * Rotate player turns. 
     */
    public void nextTurn() {
        //printTurnInfo();
        switchTimers();
        checkMaxTurns();
        updateGUI();
        
    }
    
    public void nextPlayerTurn() {
        if (curPlayer == player1) {
            setCurPlayer(player2);
        } else {
            setCurPlayer(player1);
        }
    }
    
    public AbalonePlayer getCurPlayer() {
        return curPlayer;
    }
    
    private void checkMaxTurns() {
        if (state.turn > maxTurns) {
            stopTimers();
        }
    }
    
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
                initState(P1_STANDARD, P2_STANDARD, 1);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        } else if (lowerInput.equals("belgian")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_BELGIAN, P2_BELGIAN, 1);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        } else if (lowerInput.equals("german")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_GERMAN, P2_GERMAN, 1);
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
            // modes
        } else if (lowerInput.equals("bluecomp")) {
            if (!gameStarted) {
                System.out.println("Setting Blue to Comp");
                setBlueComp(true);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("blueplayer")) {
            System.out.println("Setting Blue to Player");
            if (!gameStarted) {
                setBlueComp(false);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("redcomp")) {
            System.out.println("Setting Red to Comp");
            if (!gameStarted) {
                setRedComp(true);
            } else {
                System.out.println("Stop and reset the game first");
            }
        } else if (lowerInput.equals("redplayer")) {
            System.out.println("Setting Red to Player");
            if (!gameStarted) {
                setRedComp(false);
            } else {
                System.out.println("Stop and reset the game first");
            }
            // turn and time limit
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
    
    // if the game has not started, determines whether red will move first or not
    private void setRedComp(boolean comp) {
        if (comp) {
            player2.isAI = true;
        } else {
            player2.isAI = false;
        }
    }
    
    private void setBlueComp(boolean comp) {
        if (comp) {
            player1.isAI = true;
        } else {
            player1.isAI = false;
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
        timeAtTurnStart = curPlayer.timeTaken;
        
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
        state = null;
        updateGUI();
    }

    // initializes the squares in an 9x9 array. only coordinates within the
    // hexagonal board of Abalone will contain squares, the rest will be null
    private void initSquares() {
        
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
    
    private void initState(AbaloneCoord[] p1array, AbaloneCoord[] p2array, int turn) {
        Set<AbaloneCoord> p1PiecesStandard = new HashSet<AbaloneCoord>(Arrays.asList(p1array));
        Set<AbaloneCoord> p2PiecesStandard = new HashSet<AbaloneCoord>(Arrays.asList(p2array));
        state = new AbaloneState(p1PiecesStandard, p2PiecesStandard, turn);
        updateGUI();
    }

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
        
    }
    
    private void updateGUI() {
        gui.updateState();
    }
    
    private void updateGUIInfo() {
        gui.updateInfo();
    }
    
    /**
     * Attempts to perform a move indicated by a player.
     * 
     * POST: The state of the game will be updated to the resulting state
     * of the move if the move is successful.
     * 
     * @param coord1 The coordinate of one end of the group to be moved
     * @param coord2 The coordinate of the other end of the group to be moved
     * @param dir The direction the group is to be moved
     * @return true if the move is successful
     */
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
    
    public void setCurPlayer(AbalonePlayer player) {
        this.curPlayer = player;
    }
    
    private void readTestFile(File infile) {
        System.out.println("Reading " + infile.getName());
        try {
            Scanner scan = new Scanner(infile);
            String turnString = scan.next();
            String[] coords = scan.next().split(",");
            
            System.out.println("Scan in complete");
            int turn = (turnString.equals("w") ? 1 : 0);
            Set<AbaloneCoord> blackPieces = new HashSet<AbaloneCoord>();
            Set<AbaloneCoord> whitePieces = new HashSet<AbaloneCoord>();
            
            // populate lists
            int i = 0;
            while (i < coords.length) {
                char ownerChar = coords[i].charAt(2);
                
                if (ownerChar == 'b') {
                    int read_x = coords[i].charAt(1) - 49;
                    int read_y = coords[i].charAt(0) - 65;
                    blackPieces.add(new AbaloneCoord(read_x, read_y));
                    i++;
                } else {
                    break;
                }
            }
            while (i < coords.length) {
                char ownerChar = coords[i].charAt(2);
                
                if (ownerChar == 'w') {
                    int read_x = coords[i].charAt(1) - 49;
                    int read_y = coords[i].charAt(0) - 65;
                    whitePieces.add(new AbaloneCoord(read_x, read_y));
                    i++;
                } else {
                    break;
                }
            
            }
            
            // set test state
            System.out.println("Setting test state");
            testState = new AbaloneState(blackPieces, whitePieces, turn);
            scan.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    private void printResults(File boardoutfile, File moveoutfile) {
        System.out.println("printing to " + boardoutfile.getName());
        try {
            BufferedWriter boardWriter = new BufferedWriter(new FileWriter(boardoutfile));
            BufferedWriter moveWriter = new BufferedWriter(new FileWriter(moveoutfile));
            
            List<AbaloneState> allNextStates = testState.getAllNextStates();
            
            System.out.println("Writing to .board");
            for (AbaloneState nextState : allNextStates) {
                boardWriter.write(nextState.toString() + "\n");
            }
            
            boardWriter.close();
            
            Set<AbaloneCoord> playerPieces = testState.turn % 2 == 0 ? testState.p1Pieces : testState.p2Pieces;
            List<List<AbaloneCoord>> allGroupings = GroupingHelper.generateGroups(playerPieces);
            List<AbaloneMove> allMoves = MoveHelper.generateAllMoves(allGroupings, testState.p1Pieces, testState.p2Pieces);
            System.out.println("Writing to .move");
            for (AbaloneMove move : allMoves) {
                moveWriter.write(move.toString() + "\n");
            }
            
            moveWriter.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

