import java.util.ArrayList;
import java.util.List;

public class AbaloneState implements GameState {
    AbalonePlayer player1, player2;
    int turn;
    
    
    public AbaloneState(AbalonePlayer player1, AbalonePlayer player2, int turn) {
        this.player1 = player1;
        this.player2 = player2;
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
    private AbaloneState getNextState(AbaloneMove move) {
        if (AbaloneAI.isValidMove(this, move.x1, move.y1, move.x2, move.y2, move.direction)) {
            
        }
        return null;
    } 

    
    
}
