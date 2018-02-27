import java.util.ArrayList;
import java.util.List;

public class AbaloneAction {
    List<AbaloneCoord> piecesMoving = new ArrayList<AbaloneCoord>();
    Abalone.Dir direction;
    
    AbaloneAction(List<AbaloneCoord> pieces, Abalone.Dir direction) {
        this.piecesMoving = pieces;
        this.direction = direction;
    }
}
