package abalone;
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
    
    public String toString() {
        String toString = "";
        for (AbaloneCoord piece : movingPieces) {
            toString += "(" + piece.x + "," + piece.y + "),";
        }
        toString = toString.replaceAll(",$", "");
        toString += " moving: " + direction.toString()
                + (isInlineMove ? " inline" : " broadside")
                + " pushing: ";
        if (pushingPieces != null) {
            for (AbaloneCoord piece : pushingPieces) {
                toString += "(" + piece.x + "," + piece.y + "),";
            }
        }
        toString = toString.replaceAll(",$", "");
        return toString;
    }
}
