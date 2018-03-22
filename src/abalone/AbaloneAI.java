package abalone;

import java.util.HashMap;
import java.util.Map;

public class AbaloneAI implements Runnable {
    private AbaloneMove bestMove;
    private AbaloneState state;
    
    AbaloneAI(AbaloneState state) {
        this.state = state;
    }
    
    public void setState(AbaloneState state) {
        this.state = state;
    }
    
    @Override
    public void run() {
        iterativeDeepening(state);
    }
    
    public AbaloneMove getBestMove() {
        return state.getAllNextMoves().get(0);
    }
    
    private void iterativeDeepening(AbaloneState state) {
        Map<AbaloneState, Integer> transpositionTable = new HashMap<AbaloneState, Integer>();
        int depth = 1;
        while (!minimaxSearch(state, depth++,  transpositionTable)) { }
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
        
        // add each state's value to the transposition table when it is computed
        
        return false;
    }
    
}
