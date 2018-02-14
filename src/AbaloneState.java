import java.util.ArrayList;
import java.util.List;

public class AbaloneState implements GameState {
    List<List<AbaloneCoord>> pieces = new ArrayList<List<AbaloneCoord>>();
    Priority priority;
    
    //int turn = 1;
    
    public AbaloneState(List<AbaloneCoord> redPieces, List<AbaloneCoord> bluePieces, Priority priority) {
        this.pieces.add(redPieces);
        this.pieces.add(bluePieces);
        this.priority = priority;
        
        //this.turn = turn;
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
    private void move(List<int[]> pieces, int x1, int y1, int x2, int y2, Abalone.Dir direction) {
        int dx = x1 - x2;
        int dy = y1 - y2;
        int ddx = getIncrement(dx);
        int ddy = getIncrement(dy);
        
        for (int i = 0; i <= Math.max(Math.abs(dx), Math.abs(dy)); i++) {
            for (int[] coord : pieces) {
                if (coord[0] == x1 + ddx * i && coord[1] == y1 + ddy * i) {
                    coord[0] += direction.dx;
                    coord[1] += direction.dy;
                }
            }
        }
    }
    
    private int getIncrement(int x) {
        if (x > 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }
    
    
}
