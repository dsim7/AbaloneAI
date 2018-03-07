import java.util.ArrayList;
import java.util.List;

public class AbaloneState implements GameState {
    List<AbaloneCoord> p1Pieces = new ArrayList<AbaloneCoord>();
    List<AbaloneCoord> p2Pieces = new ArrayList<AbaloneCoord>();
    int turn;
    
    
    public AbaloneState(List<AbaloneCoord> p1Pieces, List<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;
        
    }
    
    @Override
    public int getStateValue() {
        return 0;
    }
    
    
    @Override
    public GameState[] getAllNextStates() {
        return null;
    }

    /**
     * Generates the new state based off the move passed in.
     * 
     * @param move: (List<AbaloneCoord> pieces, Abalone.Dir direction, boolean isInlineMove)
     */
    private AbaloneState getNextState(AbaloneMove move) {
        
        List<AbaloneCoord> movingPieces;
        List<AbaloneCoord> pushedPieces;
        Abalone.Dir direction;
        List<AbaloneCoord> p1Pces = new ArrayList<AbaloneCoord>();
        List<AbaloneCoord> p2Pces = new ArrayList<AbaloneCoord>();
        
        movingPieces = move.getMovingPieces();
        pushedPieces = move.getPushedPieces();
        direction = move.getDirection();
        
        for(AbaloneCoord coord : p1Pieces) {
            p1Pces.add(coord);
        }
        
        for(AbaloneCoord coord : p2Pieces) {
            p2Pces.add(coord);
        }
        
        for(AbaloneCoord coord : movingPieces) {
            
            for(int i = 0; i < p1Pces.size(); i++) {
                
                if(coord.equals(p1Pces.get(i))) {
                    
                }
                
            }
        }
        
        for(AbaloneCoord coord : pushedPieces) {
            
            for(int i = 0; i < p2Pces.size(); i++) {
                
                if(coord.equals(p2Pces.get(i))) {
                    
                }
                
            } 
            
        }
        
        AbaloneState newState = new AbaloneState(p1Pces, p2Pces, turn + 1);
        
        return newState;
    } 

    
    
}
