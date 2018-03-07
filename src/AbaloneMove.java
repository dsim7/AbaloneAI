import java.util.List;

public class AbaloneMove {
    private List<AbaloneCoord> movingPieces;
    private List<AbaloneCoord> pushingPieces;
    private Abalone.Dir direction;
    private boolean isInlineMove;
    private int numPushedPieces;
    
    AbaloneMove(List<AbaloneCoord> movingPieces,
                List<AbaloneCoord> pushingPieces,
                Abalone.Dir direction,
                boolean isInlineMove,
                int numPushedPieces) {
        this.movingPieces = movingPieces;
        this.pushingPieces = pushingPieces;
        this.direction = direction;
        this.isInlineMove = isInlineMove;
        this.numPushedPieces = numPushedPieces;
    }
    
    public List<AbaloneCoord> getMovingPieces() {
        return movingPieces;
    }
    
    public List<AbaloneCoord> getPushedPieces() {
        return pushingPieces;
    }
    
    public int getNumPushedPieces() {
        return numPushedPieces;
    }
    
    public Abalone.Dir getDirection() {
        return direction;
    }
}
