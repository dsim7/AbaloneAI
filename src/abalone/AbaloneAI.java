package abalone;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbaloneAI implements Runnable {
    private Abalone ab;
    private AbaloneMove bestMove;
    //private final static AbaloneMove PLACE_HOLDER_BEST_MOVE = 
    //       new AbaloneMove(new ArrayList<AbaloneCoord>(), null, Abalone.Dir.UL, false);
    
    //Concurrency stuff
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
      
    AbaloneAI(Abalone ab) {
        this.ab = ab;
    }
    
    public AbaloneMove getBestMove() {
        List<AbaloneMove> nextMoves = ab.getState().getAllNextMoves();
        
        AbaloneMove bestMove = nextMoves.get(0);
        double bestMoveStateValue = ab.getState().getNextState(bestMove).getStateValueRedPerspective();
        
        if (ab.getState().turn % 2 == 0) {
            for (AbaloneMove move : nextMoves) {
                double moveStateValue = ab.getState().getNextState(move).getStateValueRedPerspective();
                if (moveStateValue > bestMoveStateValue) {  // TESTING. blue will always be comp. always find least redperspective value
                    bestMove = move;
                    bestMoveStateValue = moveStateValue;
                }
            }
        }else {
            for (AbaloneMove move : nextMoves) {
                double moveStateValue = ab.getState().getNextState(move).getStateValueRedPerspective();
                if (moveStateValue < bestMoveStateValue) {  // TESTING. blue will always be comp. always find least redperspective value
                    bestMove = move;
                    bestMoveStateValue = moveStateValue;
                }
            }
        }
        System.out.println("BEST MOVE VALUE: " + bestMoveStateValue);
        return bestMove;
    }
    
    private void iterativeDeepening(AbaloneState state) {
        Map<AbaloneState, Integer> transpositionTable = new HashMap<AbaloneState, Integer>();
        int depth = 1;
        while (depth < 10000) {
            // Concurrency stuff... call checkPaused and check running at regular intervals.
            // if not running, should back out completely. abort everything
            checkPaused();
            if (!running) {
                return;
            }
            
            
            minimaxSearch(state, depth++,  transpositionTable);
        }
    }
    
    /**
     * Sets the AI's current best move from the given state. The best
     * move is computed using minimax iterative deepening search
     * to a certain depth. Tranposition table is used to avoid recomputation. 
     * 
     * @param root
     * @param depth
     * @return true if the search finds a finished solution
     */
    private boolean minimaxSearch(AbaloneState root,
                                      int depth,
                                      Map<AbaloneState, Integer> transpositionTable) {
        // before computing each state's value, search transposition table to see if
        // we already know it
        System.out.println("Minimax Search depth: " + depth);
        
        // add each state's value to the transposition table when it is computed
        
        return false;
    }
    

    private double evaluateState(AbaloneState state) {
        return state.getStateValueRedPerspective();
    }
    
    
    
    
    
    
    // ===========Concurrency stuff============
    @Override
    public void run() {
        iterativeDeepening(ab.getState());
        if (running) {
            ab.move(getBestMove());
        }
        
    }
    
    public void stop() {
        running = false;
        // you might also want to interrupt() the Thread that is 
        // running this Runnable, too, or perhaps call:
        resume();
        // to unblock
    }

    public void pause() {
        // you may want to throw an IllegalStateException if !running
        paused = true;
        
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll(); // Unblocks thread
        }
    }
    
    private void checkPaused() {
        if (paused) {
            synchronized (pauseLock) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }   
    }
    
    
}
