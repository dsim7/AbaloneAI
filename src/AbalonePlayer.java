import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.GamePlayer;

public class AbalonePlayer extends GamePlayer {
    Color color;
    int outs;
    double timeTaken;
    boolean isAI;
    AbaloneAI ai = new AbaloneAI();
    List<AbaloneCoord> pieces = new ArrayList<AbaloneCoord>();
    
    public AbalonePlayer(Color color, Game game) {
        super(game);
        this.color = color;
    }

}
