import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AbalonePlayer {
    Abalone abalone;
    Color color;
    int outs, priority;
    double timeTaken;
    boolean isAI;
    AbaloneAI ai = new AbaloneAI();
    List<AbaloneCoord> pieces = new ArrayList<AbaloneCoord>();
    
    public AbalonePlayer(int priority, Color color, Abalone game) {
        this.abalone = game;
        this.color = color;
        this.priority = priority;
    }

}
