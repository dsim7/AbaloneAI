import java.awt.Color;

import game.Game;
import game.GamePlayer;

public class AbalonePlayer extends GamePlayer {
    Color color;
    int outs;
    double timeTaken;
    
    
    public AbalonePlayer(Color color, Game game) {
        super(game);
        this.color = color;
    }

}
