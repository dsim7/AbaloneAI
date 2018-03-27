package abalone;
import java.util.List;

public class AbaloneMove {
    private List<AbaloneCoord> movingPieces;
    private List<AbaloneCoord> pushingPieces;
    private Abalone.Dir direction;
    private boolean isInlineMove;
    
    private static final double MOVE_WEIGHT = 1;
    private static final double PUSH_MOVE_VALUE = 30;
    private static final double THREATEN_MOVE_VALUE = 80;
    private static final double SINGLE_GROUP_VALUE = 1;
    private static final double DOUBLE_GROUP_VALUE = 5;
    private static final double TRIPLE_GROUP_VALUE = 10;
    
    
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
    
    public double getValue() {
        double result = 0;
        switch (movingPieces.size()) {
        case 1 : result = SINGLE_GROUP_VALUE;
        break;
        case 2 : result = DOUBLE_GROUP_VALUE;
        break;
        case 3 : result = TRIPLE_GROUP_VALUE;
        break;
        }
        if (pushingPieces != null) {
            result += PUSH_MOVE_VALUE;                // + x if push move
            for (AbaloneCoord pushedPiece : pushingPieces) {
                if (!pushedPiece.isValid()) {
                    result += THREATEN_MOVE_VALUE;
                }
            }
        }
        return result * MOVE_WEIGHT;
    }
}
