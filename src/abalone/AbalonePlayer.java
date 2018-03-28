package abalone;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AbalonePlayer {
    Abalone abalone;
    int outs, priority;
    double timeTaken;
    boolean isAI;
    double roundTimeTaken;
    
    public AbalonePlayer(int priority, Abalone game) {
        this.abalone = game;
        this.priority = priority;
    }
    
    public String toString() {
        return "Player " + (priority + 1);
    }

}
