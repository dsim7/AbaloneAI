package abalone;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AbalonePlayer {
    Abalone abalone;
    Color color;
    int outs, priority;
    double timeTaken;
    boolean isAI;
    
    public AbalonePlayer(int priority, Color color, Abalone game) {
        this.abalone = game;
        this.color = color;
        this.priority = priority;
    }

}
