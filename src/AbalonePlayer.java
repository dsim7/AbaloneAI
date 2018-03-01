import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.GamePlayer;

public class AbalonePlayer extends GamePlayer {
    Color color;
    int outs, priority;
    double timeTaken;
    boolean isAI;
    AbaloneAI ai = new AbaloneAI();
    List<AbaloneCoord> pieces = new ArrayList<AbaloneCoord>();
    
    public AbalonePlayer(int priority, Color color, Game game) {
        super(game);
        this.color = color;
        this.priority = priority;
    }

}
