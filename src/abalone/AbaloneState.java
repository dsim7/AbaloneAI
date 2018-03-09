package abalone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbaloneState {
    Set<AbaloneCoord> p1Pieces = new HashSet<AbaloneCoord>();
    Set<AbaloneCoord> p2Pieces = new HashSet<AbaloneCoord>();
    int turn;
    
    
    public AbaloneState(Set<AbaloneCoord> p1Pieces, Set<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;
        
    }
    
    public int getStateValue() {
        return 0;
    }
    
    public List<AbaloneState> getAllNextStates() {
        List<AbaloneState> listOfStates = new ArrayList<AbaloneState>();
        
        return listOfStates;
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
        
        return null;
    } 
    
    public String toString() {
        String toWrite = "";
        for (AbaloneCoord blackPiece : this.p1Pieces) {
            char row = (char) (blackPiece.x + 65);
            int col = blackPiece.y + 1;
            toWrite += "" + row + col + "b,";
        }
        for (AbaloneCoord whitePiece : this.p2Pieces) {
            char row = (char) (whitePiece.x + 65);
            int col = whitePiece.y + 1;
            toWrite += "" + row + col + "w,";
        }
        toWrite = toWrite.replaceAll(",$", "");
        return toWrite;
    }
    
    
}
