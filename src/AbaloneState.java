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

    /*
    /**
     * Changes the coordinates of pieces
     * 
     * PRECONDITION: all coordinates between x1,y1 and x2,y2 exist within the list pieces
     * 
     * @param pieces The list of pieces in which the pieces to move reside in
     * @param x1 The x-coordinate of one end of the pieces to move
     * @param y1 The y-coordinate of one end of the pieces to move
     * @param x2 The x-coordinate of the other end of the pieces to move
     * @param y2 The y-coordinate of the other end of the pieces to move
     * @param direction The direction to move
     * 
     */
    /*
    private AbaloneState getNextState(AbaloneMove move) {
        if (AbaloneAI.isValidMove(this, move.x1, move.y1, move.x2, move.y2, move.direction)) {
            
        }
        return null;
    }
    */

    
    
}
