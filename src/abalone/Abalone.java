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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.Timer;




/**
 * Center of the Abalone game. Contains configurations for the game as it is
 * played, timers for each player, and an association with a GUI. An Abalone
 * game processes command line commands to configure the game.
 * 
 * @author dylan
 *
 */
public class Abalone {
    public static final int FRAME_WIDTH = 1000;
    public static final int FRAME_HEIGHT = 1000;
    public static final Color P1_COLOR = Color.BLACK;
    public static final Color P2_COLOR = Color.WHITE;
    public static final DecimalFormat TIME_FORMAT = new DecimalFormat("0.0");
    public static final SimpleDateFormat TIME_STAMP_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
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
    
    private boolean guiFlipped = true;
    private int maxTurns = 0;
    private double maxTimePerTurn = 0;
    private Scanner console = new Scanner(System.in);
    private File logFile = new File("move_log.txt");
    private FileWriter logWriter;
    
    private Timer p1timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            getPlayers()[0].timeTaken += 0.1;
            getPlayers()[0].roundTimeTaken += 0.1;
            updateGUIInfo();
        }
    });
    private Timer p2timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            getPlayers()[1].timeTaken += 0.1;
            getPlayers()[1].roundTimeTaken += 0.1;
            updateGUIInfo();
        }
    });
    private Timer maxTurnTimer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Turn is up");
            pauseTimers();
            if (aiThread != null) {
                aiThread.stop();
            }
            updateGUIInfo();
        }
    });
    private KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println("ESC Pressed");
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_ESCAPE) {
                clearSelection();
            }
        }
    };
    private Timer lastRunningTimer = p1timer;

    private boolean gameRunning = false;
    private boolean gameStarted = false;
    private boolean gameOver = false;
    private boolean settingTurns = false;
    private boolean settingTimePerTurn = false;
    private AbaloneCoord selection1, selection2;
    private Dir directionSelection;
    
    private AbaloneGUI gui;

    private AbaloneState state;
    private AbalonePlayer[] players = {new AbalonePlayer(0, this), new AbalonePlayer(1, this)};
    private AbalonePlayer curPlayer = getPlayers()[0];
    private AbaloneCoord[][] board = new AbaloneCoord[9][9];
    private AbaloneAI aiThread;
    
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
        this.initState(Abalone.P1_STANDARD, Abalone.P2_STANDARD, -1);
        p1timer.setRepeats(true);
        p2timer.setRepeats(true);
        
        log("New Game Started");
        
        while (console.hasNextLine()) {
            processInput(console.nextLine());
            updateGUI();
        }

        
    }
    
    public Abalone(File inputFile, File boardoutFile, File moveoutfile) {
        System.out.println("Testing file");
        if (this.readTestFile(inputFile)) {
            this.printNextStatesResults(boardoutFile, moveoutfile);
        }
    }

    /**
     * Determines whether the game is ready for users to click pieces and play the game.
     * 
     * @return true if the game is not over, the game has started, and the game is running
     */
    public boolean getCanClick() {
        return !gameOver && gameRunning && gameStarted;
    }
    
    public AbalonePlayer getCurPlayer() {
        return getPlayers()[getState().turn % 2];
    }

    public void clicked(AbaloneCoord coord) {
        //System.out.println(getCurPlayer());
        if (!curPlayer.isAI && getCanClick()) {
            if (selection1 == null) {
                if (getState().getCurPlayerPieces().contains(coord)) {
                    selection1 = coord;
                }
            } else if (selection2 == null) {
                if (getState().getCurPlayerPieces().contains(coord)) {
                    selection2 = coord;
                }
            } else if (directionSelection == null) {
                directionSelection = getDirection(coord);
                if (!move(composeMove(selection1,
                        selection2,
                        directionSelection))) {
                    System.out.println("Invalid move, try again");
                }
            }
        } else {
            System.out.println("Can't click right now");
        }
    }
    
    private Abalone.Dir getDirection(AbaloneCoord coord) {
        int dx = coord.x - selection2.x;
        int dy = coord.y - selection2.y;
        for (Abalone.Dir dir : Abalone.Dir.values()) {
            if (dir.dx == dx && dir.dy == dy) {
                return dir;
            }
        }
        return null;
    }
    
    
    private void checkMaxTurns() {
        if (getState().turn > maxTurns && maxTurns != 0) {
            stopTimers();
        }
    }
    
    private boolean processInput(String input) {
        String lowerInput = input.toLowerCase();
        /*if (lowerInput.equals("stop")) { //stops the game and restarts all timers. game is over
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
            
        } else*/ if (lowerInput.equals("standard")) {
        
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_STANDARD, P2_STANDARD, -1);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        
        } else if (lowerInput.equals("belgian")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_BELGIAN, P2_BELGIAN, -1);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        
        } else if (lowerInput.equals("german")) {
            if (!gameOver && !gameStarted) {
                clearBoard();
                initState(P1_GERMAN, P2_GERMAN, -1);
            } else {
                System.out.println("Stop and reset the game first");
            }
            return true;
        /*
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
        */
            // modes
        } else if (lowerInput.equals("bluecomp")) {
            if (!gameStarted) {
                System.out.println("Setting Blue to Comp");
                setBlueComp(true);
            } else {
                System.out.println("Stop and reset the game first");
            }
        
        } else if (lowerInput.equals("blueplayer")) {
            if (!gameStarted) {
                System.out.println("Setting Blue to Player");
                setBlueComp(false);
            } else {
                System.out.println("Stop and reset the game first");
            }
        
        } else if (lowerInput.equals("redcomp")) {
            if (!gameStarted) {
                System.out.println("Setting Red to Comp");
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
        getPlayers()[0].isAI = comp;
    }
    
    private void setBlueComp(boolean comp) {
        getPlayers()[1].isAI = comp;
    }


    public void nextPlayerTurn() {
        switchTimers();
        pauseTimers();
        curPlayer = getPlayers()[state.turn % 2];
        updateGUI();
        
        checkMaxTurns();

    }
    
    // pause timers
    public void pauseTimers() {
        System.out.println("Pausing");
        lastRunningTimer = p1timer.isRunning() ? p1timer : p2timer;
        lastRunningTimer.stop();
        maxTurnTimer.stop();
        gameRunning = false;
        // stop ai's TODO
        
        if (aiThread != null) {
            aiThread.pause();
        }
    }
    
    // timers revert to 0 and stop. should be followed up with a reset of the board.
    // player 1 timer will be the one to resume upon resuming
    public void stopTimers() {
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
        updateGUIInfo();
        
        // stop ai's TODO
        if (aiThread != null) {
            aiThread.stop();
        }
    }

    public void resetTimers() {
        stopTimers();
        System.out.println("Reseting");
        for (AbalonePlayer player : getPlayers()) {
            player.timeTaken = 0;
            player.roundTimeTaken = 0;
            player.outs = 0;
        }
        maxTimePerTurn = 0;
        maxTurns = 0;
        gameOver = false;
        gameStarted = false;
        clearBoard();
    }

    // resumes timers
    public void resumeTimers() {
        System.out.println("Starting/Resuming");
        if (!gameStarted) {
            if (maxTimePerTurn != 0) {
                maxTurnTimer.start();
            }
            gameStarted = true;
            state.turn++;
            nextPlayerTurn();
        }
        lastRunningTimer.start();
        if (maxTimePerTurn != 0) {
            maxTurnTimer.start();
        }
        gameRunning = true;
        
        if (aiThread != null) {
            aiThread.resume();
        }
    }
    
    // switches which timer is running between player1 and player2
    public void switchTimers() {        
        if (p1timer.isRunning()) {
            p1timer.stop();
            p2timer.start();
            getPlayers()[1].roundTimeTaken = 0;
        } else {
            p2timer.stop();
            p1timer.start();
            getPlayers()[0].roundTimeTaken = 0;
        }
        
        if (maxTimePerTurn != 0) {
            maxTurnTimer.restart();
        }
    }
    
    public void getAIMove() {
        if (getCanClick() && curPlayer.isAI) {
            System.out.println("Getting AI to move");
            aiThread = new AbaloneAI(this);
            (new Thread(aiThread)).start();
        } else {
            System.out.println("Player is not an AI or can't Click right now.");
        }
    }
    
    public void flipGUI() {
        gui.flip(!guiFlipped);
        guiFlipped = !guiFlipped;
    }

    // clears all currently selected squares and directions
    public void clearSelection() {
        System.out.println("Selection Cleared");
        selection1 = null;
        selection2 = null;
        directionSelection = null;
    }

    // removes all pieces from the board
    private void clearBoard() {
        AbaloneCoord[] coordArr1 = {};
        AbaloneCoord[] coordArr2 = {};
        initState(coordArr1, coordArr2, -1);
        updateGUI();
    }

    // initializes the squares in an 9x9 array. only coordinates within the
    // hexagonal board of Abalone will contain squares, the rest will be null
    private void initSquares() {
        
        // 1
        for (int i = 0; i < 5; i++) {
            getBoard()[0][i] = new AbaloneCoord(i, 0);
        }
        
        // 2
        for (int i = 0; i < 6; i++) {
            getBoard()[1][i] = new AbaloneCoord(i, 1);
        }
        
        // 3
        for (int i = 0; i < 7; i++) {
            getBoard()[2][i] = new AbaloneCoord(i, 2);
        }
        
        // 4
        for (int i = 0; i < 8; i++) {
            getBoard()[3][i] = new AbaloneCoord(i, 3);
        }
        
        // 5
        for (int i = 0; i < 9; i++) {
            getBoard()[4][i] = new AbaloneCoord(i, 4);
        }
        
        // 6
        for (int i = 1; i < 9; i++) {
            getBoard()[5][i] = new AbaloneCoord(i, 5);
        }
        
        // 7
        for (int i = 2; i < 9; i++) {
            getBoard()[6][i] = new AbaloneCoord(i, 6);
        }
        
        // 8
        for (int i = 3; i < 9; i++) {
            getBoard()[7][i] = new AbaloneCoord(i, 7);
        }
        
        // 9
        for (int i = 4; i < 9; i++) {
            getBoard()[8][i] = new AbaloneCoord(i, 8);
        }
    }
    
    private void initState(AbaloneCoord[] p1array, AbaloneCoord[] p2array, int turn) {
        System.out.println("Setting board");
        Set<AbaloneCoord> p1PiecesStandard = new HashSet<AbaloneCoord>(Arrays.asList(p1array));
        Set<AbaloneCoord> p2PiecesStandard = new HashSet<AbaloneCoord>(Arrays.asList(p2array));
        state = new AbaloneState(p1PiecesStandard, p2PiecesStandard, turn);
        updateGUI();
    }

    // initializes the gui's for player 1 and player 2
    private void initGUIs() {
        gui = new AbaloneGUI(this);
        
        new AbaloneGUIThread().start();
        
        
    }
    
    private class AbaloneGUIThread extends Thread {
        @Override
        public void run() {
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
    public boolean move(AbaloneMove move) {
        if (move != null) {
            logMove(move);
            gui.updateLastMove(move);
            setToNextState(move);
            nextPlayerTurn();
            clearSelection();
            return true;
        } else {
            System.out.println("Invalid Move");
            clearSelection();
            return false;
        }
    }
    
    private AbaloneMove composeMove(AbaloneCoord coord1, AbaloneCoord coord2, Dir dir) {
        System.out.println(getState().turn % 2);
        Set<AbaloneCoord> playerPieces = getState().getCurPlayerPieces();
        Set<AbaloneCoord> enemyPieces = getState().getEnemyPieces();
        List<AbaloneCoord> group = GroupingHelper.generateCoordinates(coord1, coord2, playerPieces);
        if (group != null) {
            AbaloneMove move = MoveHelper.generateMove(group, dir, playerPieces, enemyPieces);
            return move;
        } else {
            System.out.println("Invalid Grouping");
        }
        return null;
    }
    
    private void setToNextState(AbaloneMove move) {
        state = state.getNextState(move);
    }

    private boolean readTestFile(File infile) {
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
            return true;
        } catch (FileNotFoundException ex) {
            System.out.println("Input File Not Found");
            return false;
        }
    }
    
    private void printNextStatesResults(File boardoutfile, File moveoutfile) {
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
            Set<List<AbaloneCoord>> allGroupings = GroupingHelper.generateGroups(playerPieces);
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
    
    private void logMove(AbaloneMove move) {
        if (move != null) {
            log(getCurPlayer().toString() + " " + move.toString() + " (" + TIME_FORMAT.format(curPlayer.roundTimeTaken) + "sec)");
        }
    }
    
    private void log(String string) {
        try {
            logWriter = new FileWriter(logFile, true);
            Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
            logWriter.write(TIME_STAMP_FORMAT.format(timeStamp) + ": " + string + "\n");
            System.out.print(TIME_STAMP_FORMAT.format(timeStamp) + ": " + string + "\n");
            logWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public AbalonePlayer[] getPlayers() {
        return players;
    }

    public AbaloneState getState() {
        return state;
    }

    public AbaloneCoord[][] getBoard() {
        return board;
    }

    // enumeration of Directions
    // each direction has a delta x and delta y which represent the difference in x
    // and y which result from moving in that direction
    public enum Dir {
        L(-1,0,"L", "9"), UL(0,1,"UL", "11"), DL(-1,-1,"DL", "7"), UR(1,1,"UR", "1"), DR(0,-1,"DR", "5"), R(1,0,"R", "3");

        int dx, dy;
        String name;
        String notation;
        
        Dir(int dx, int dy, String name, String notation) {
            this.dx = dx;
            this.dy = dy;
            this.name = name;
            this.notation = notation;
        }
    }
}


