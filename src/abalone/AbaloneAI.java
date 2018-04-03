package abalone;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AbaloneAI implements Runnable {

    
    private static final int MAX_DEPTH = 3;
    private Abalone ab;
    private AbaloneMove bestMove;
    private AbaloneMove bestMoveMidSearch;
    //private final static AbaloneMove PLACE_HOLDER_BEST_MOVE = 
    //       new AbaloneMove(new ArrayList<AbaloneCoord>(), null, Abalone.Dir.UL, false);
    
    //Concurrency stuff
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    

    int count = 0;
    double maxScoreSoFar;
    double minScoreSoFar;
    
    AbaloneAI(Abalone ab) {
        this.ab = ab;
    }
    
    public AbaloneMove getBestMove() {
        /*List<AbaloneMove> nextMoves = ab.getState().getAllNextMoves();
        
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
        */
        return bestMove;
    }
    
    private void iterativeDeepening(AbaloneState state) {
        System.out.println("Iterative deepening");
        Map<AbaloneState, Integer> transpositionTable = new HashMap<AbaloneState, Integer>();

        int depth = 0;
        while (depth <= MAX_DEPTH) {
             //Concurrency stuff... call checkPaused and check running at regular intervals.
             //if not running, should back out completely. abort everything
            
            minimaxSearch(state, depth++,  transpositionTable);
        }
    }
    
    
    /**
     * Sets the AI's current best move from the given state. The best
     * move is computed using minimax iterative deepening search
     * to a certain depth. Transposition table is used to avoid recomputation. 
     * 
     * @param root
     * @param depth
     * @return true if the search finds a finished solution
     */
    /*
    private void minimaxSearch(AbaloneState root,
                               int depth,
                               Map<AbaloneState, Integer> transpositionTable) {
        // before computing each state's value, search transposition table to see if
        // we already know it
        //System.out.println("Minimax Search depth: " + depth);

        long time = System.nanoTime();
        if(root.turn % 2 == 0) {
            maxMove(root, MIN, MAX, 0, depth);
        } else {
            minMove(root, MIN, MAX, 0, depth);
        }
        System.out.println("Minimax depth " + depth + " " + (System.nanoTime() - time));
       
    }
    
    private double maxMove(AbaloneState state, double a, double b, int curDepth, int depth) {
        //System.out.println("//// MAX " + curDepth + " \\\\");
        if (curDepth >= depth || state.getStateValueRedPerspective() == MAX) {
            //System.out.println("TERMINAL STATE VALUE: " + state.getStateValueRedPerspective());
            //System.out.println("\\\\ MAX " + curDepth + " ////");
            return state.getStateValueRedPerspective();
        }

        List<AbaloneMove> moves = state.getAllNextMoves();
        Collections.sort(moves);
        //System.out.println(curDepth + " " + moves.size());
        double resultantStateValue = -Double.MAX_VALUE;
        for (AbaloneMove move : moves) {
            //System.out.println("Check Move: " + move);
            
            AbaloneState resultantState = state.getNextState(move);
            double resultantStateValue = minMove(resultantState, a, b, curDepth + 1, depth);
            
            //checkPaused();
            if (!running) {
                break;
            }
            
            //System.out.println("Compare " + result + " " + highestChildValue);
            if (resultantStateValue > highestChildValue) {
                highestChildValue = resultantStateValue;
                //System.out.println("Highest child is now: " + highestChildValue);
                if (curDepth == 0) {
                    bestMoveMidSearch = move;
                }
            }

            //System.out.println("Beta Compare: " + highestChildValue + " " + b.beta);
            if (highestChildValue > b) {
                //System.out.println("Pruning max");
                return highestChildValue;
            }
            a = Math.max(highestChildValue, a);
            //System.out.println("Alpha set to: " + a.alpha);
        }
        //System.out.println("VALUE OF THIS NODE IS: " + highestChildValue);
        //System.out.println("\\\\ MAX " + curDepth + " ////");
        
        return highestChildValue;
        
    }
    
    private double minMove(AbaloneState state, double a, double b, int curDepth, int depth) {
        //System.out.println("//// MIN " + curDepth + " \\\\");
        if (curDepth >= depth || state.getStateValueRedPerspective() == MIN) {
            //System.out.println("TERMINAL STATE VALUE: " + state.getStateValueRedPerspective());
            //System.out.println("\\\\ MIN " + curDepth + " ////");
            return state.getStateValueRedPerspective();
        }
        
        List<AbaloneMove> moves = state.getAllNextMoves();
        
        Collections.sort(moves);
        
        double lowestChildValue = MAX;
        for (AbaloneMove move : moves) {
            //System.out.println("Check Move: " + move);
            //System.out.println(state.turn);
            AbaloneState resultantState = state.getNextState(move);
            
            double resultantStateValue = maxMove(resultantState, a, b , curDepth + 1, depth);
            //System.out.println("Compare " + result + " " + lowestChildValue);

            //checkPaused();
            if (!running) {
                break;
            }
            
            if (resultantStateValue < lowestChildValue) {
                //System.out.println("Lowest child is now: " + lowestChildValue);
                lowestChildValue = resultantStateValue;
                if (curDepth == 0) {
                    bestMoveMidSearch = move;
                }
            }

            //System.out.println("Alpha Compare: " + lowestChildValue + " " + a.alpha);
            // pruning
            if (lowestChildValue < a) {
                //System.out.println("Pruning min");
                return lowestChildValue;
            }
            b = Math.min(lowestChildValue, b);
            //System.out.println("Beta set to: " + b.beta);
        }
        //System.out.println("Max return " + maxStateValue);
        //System.out.println("VALUE OF THIS NODE IS: " + lowestChildValue);
        //System.out.println("\\\\ MIN " + curDepth + " ////");
        return lowestChildValue;
        
    }
    */
    
    
    //alpha beta pruning code start - swp
    /**
     * Sets the AI's current best move from the given state. The best
     * move is computed using minimax iterative deepening search
     * to a certain depth. Transposition table is used to avoid recomputation. 
     * 
     * @param root
     * @param depth
     * @return true if the search finds a finished solution
     */
    private void minimaxSearch(AbaloneState root,
                               int depth,
                               Map<AbaloneState, Integer> transpositionTable) {
        // before computing each state's value, search transposition table to see if
        // we already know it
        //System.out.println("Minimax Search depth: " + depth);

        maxScoreSoFar = -Double.MAX_VALUE;
        minScoreSoFar = Double.MAX_VALUE;
        
        long time = System.nanoTime();
        if(root.turn % 2 == 0) {
            maxMove(root, 0, depth, maxScoreSoFar, minScoreSoFar);
        } else {
            minMove(root, 0, depth, maxScoreSoFar, minScoreSoFar);
        }
        bestMove = bestMoveMidSearch;
        System.out.println("Minimax depth " + depth + " " + (System.nanoTime() - time));
       
    }
    
    private double maxMove(AbaloneState state, int curDepth, int depth, double alpha, double beta) {
        //System.out.println("Minimax : max " + count++);
        if (curDepth >= depth || state.getStateValueRedPerspective() == Double.MAX_VALUE) {
            //maxScoreSoFar = Math.max(maxScoreSoFar, state.getStateValueRedPerspective());
            return state.getStateValueRedPerspective();
        }

        List<AbaloneMove> moves = state.getAllNextMoves();
        
        Collections.sort(moves);
        //System.out.println(curDepth + " " + moves.size());
        double resultantStateValue = -Double.MAX_VALUE;
        for (AbaloneMove move : moves) {
            //System.out.println(state.turn);
            AbaloneState resultantState = state.getNextState(move);
            
            double result = minMove(resultantState, curDepth + 1, depth, alpha, beta);
            
            if (result > resultantStateValue) {
                resultantStateValue = result;
                if (curDepth == 0) {
                    bestMoveMidSearch = move;
                }
            }
            
            //pruning
            if (resultantStateValue > beta) {
                return resultantStateValue;
            }
            
            alpha = Math.max(alpha, resultantStateValue);
            
            if (!running) {
                break;
            }
        }
        //System.out.println("Max return " + maxStateValue);
        return resultantStateValue;
        
    }
    
    private double minMove(AbaloneState state, int curDepth, int depth, double alpha, double beta) {
        if (curDepth >= depth || state.getStateValueRedPerspective() == -Double.MAX_VALUE) {
            return state.getStateValueRedPerspective();
        }
        //System.out.println("Minimax : min");
        List<AbaloneMove> moves = state.getAllNextMoves();
        
        Collections.sort(moves);
        //System.out.println(curDepth + " " + moves.size());
        double resultantStateValue = Double.MAX_VALUE;
        
        for (AbaloneMove move : moves) {
            //System.out.println(state.turn);
            AbaloneState resultantState = state.getNextState(move);
            //AbaloneState resultantState = state.getNextState(moves.get(i));
            
            double result = maxMove(resultantState, curDepth + 1, depth, alpha, beta);
            
            if (result < resultantStateValue) {
                resultantStateValue = result;
                if (curDepth == 0) {
                  bestMoveMidSearch = move;
                  //bestMove = moves.get(i);
                }
            }
            
            //pruning
            if (resultantStateValue < alpha) {
                return resultantStateValue;
            }
            
            beta = Math.min(beta, resultantStateValue);
            
            if (!running) {
                break;
            }
        }
        //System.out.println("Max return " + maxStateValue);
        return resultantStateValue;
        
    }
    //swp code end
    
    
    // ===========Concurrency stuff============
    @Override
    public void run() {
        iterativeDeepening(ab.getState());
        ab.move(getBestMove());
        
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