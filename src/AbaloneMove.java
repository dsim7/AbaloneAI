import java.util.List;

public class AbaloneMove {
    private List<AbaloneCoord> pieces;
    private Abalone.Dir direction;
    private boolean isInlineMove;
    
    AbaloneMove(List<AbaloneCoord> pieces, Abalone.Dir direction, boolean isInlineMove) {
        this.pieces = pieces;
        this.direction = direction;
        this.isInlineMove = isInlineMove;
    }
}
