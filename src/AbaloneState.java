import java.util.ArrayList;
import java.util.List;

public class AbaloneState implements GameState {
    List<List<AbaloneCoord>> pieces = new ArrayList<List<AbaloneCoord>>();
    Priority priority;
    
    
    public AbaloneState(List<AbaloneCoord> redPieces, List<AbaloneCoord> bluePieces, Priority priority) {
        this.pieces.add(redPieces);
        this.pieces.add(bluePieces);
        this.priority = priority;
        
    }
    
    enum Priority {
        P1(0), P2(1);
        
        int i;
        
        Priority(int i) {
            this.i = i;
        }
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
    private AbaloneState getNextState(AbaloneAction action) {
        
        return null;
    } 

    
    
}
