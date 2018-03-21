package abalone;
import java.util.List;

public class AbaloneMove {
    private List<AbaloneCoord> movingPieces;
    private List<AbaloneCoord> pushingPieces;
    private Abalone.Dir direction;
    private boolean isInlineMove;
    
    AbaloneMove(List<AbaloneCoord> movingPieces,
                List<AbaloneCoord> pushingPieces,
                Abalone.Dir direction,
                boolean isInlineMove) {
        this.movingPieces = movingPieces;
        this.pushingPieces = pushingPieces;
        this.direction = direction;
        this.isInlineMove = isInlineMove;
    }
    
    public String toString() {
        String toString = "";
        for (AbaloneCoord piece : movingPieces) {
            toString += "(" + piece.x + "," + piece.y + "),";
        }
        toString = toString.replaceAll(",$", "");
        toString += " " + direction.toString()
                + (isInlineMove ? " inline" : " broadside");
        /*
        if (pushingPieces != null) {
            toString += " pushing: ";
            for (AbaloneCoord piece : pushingPieces) {
                toString += "(" + piece.x + "," + piece.y + "),";
            }
        }*/
        toString = toString.replaceAll(",$", "");
        return toString;
    }
    
    public List<AbaloneCoord> getMovingPieces() {
        return movingPieces;
    }
    
    public List<AbaloneCoord> getPushedPieces() {
        return pushingPieces;
    }
    
    public boolean getIsInlineMove() {
        return isInlineMove;
    }
    
    public Abalone.Dir getDirection() {
        return direction;
    }
    
}
