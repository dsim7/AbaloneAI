package game;

/**
 * Game Components are pieces of the Game such as game pieces,
 * cards, positions, and other things that the Game's state
 * depends on.
 * 
 * @author dylan
 *
 */
public abstract class GameComponent {
    private GamePlayer owner;
    private Game game;

    public boolean selected;
    
    public GameComponent(Game game) {
        this.game = game;
    }
    
    public GameComponent(Game game, GamePlayer player) {
        owner = player;
        this.game = game;
    }
    
    public GamePlayer getOwner() {
        return owner;
    }
    
    public Game getGame() {
        return game;
    }

    public void setOwner(GamePlayer player) {
        owner = player;
    }
    
}
