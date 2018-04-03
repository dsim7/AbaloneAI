package abalone;
import java.util.List;

public class AbaloneMove implements Comparable {
    private List<AbaloneCoord> movingPieces;
    private List<AbaloneCoord> pushingPieces;
    private Abalone.Dir direction;
    private boolean isInlineMove;
    
    private static final double MOVE_WEIGHT = 2;
    private static final double PUSH_MOVE_VALUE = 30;
    private static final double THREATEN_MOVE_VALUE = 50;
    private static final double SINGLE_GROUP_VALUE = 1;
    private static final double DOUBLE_GROUP_VALUE = 10;
    private static final double TRIPLE_GROUP_VALUE = 20;
    
    
    AbaloneMove(List<AbaloneCoord> movingPieces,
                List<AbaloneCoord> pushingPieces,
                Abalone.Dir direction,
                boolean isInlineMove) {
        this.movingPieces = movingPieces;
        this.pushingPieces = pushingPieces;
        this.direction = direction;
        this.isInlineMove = isInlineMove;
    }
    
    @Override
    public String toString() {
        String moveTypeNotation = isInlineMove ? "i" : "s";
        String tail = MoveHelper.findTailPiece(movingPieces, direction).toString();
        String front = MoveHelper.findFrontPiece(movingPieces, direction).toString();
        String directionNotation = direction.notation;
        
        if (isInlineMove) {
            return moveTypeNotation + " - " + tail + " - " + directionNotation;
        } else {
            return moveTypeNotation + " - " + tail + " - " + front + " - " + directionNotation;
        }
        
        /*     
        for (AbaloneCoord piece : movingPieces) {
            toString += piece.toString() + ", ";
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
        }/
        toString = toString.replaceAll(",$", "");
        return toString;*/
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

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof AbaloneMove)) {
            return -1;
        }
        return ((Double) this.getValue()).compareTo(((AbaloneMove) obj).getValue());
    }
}
