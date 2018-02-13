package game;

public class GameCard extends GameComponent {
    GamePosition position;
    
    public GameCard(Game game) {
        super(game);
    }
    
    public GameCard(Game game, GamePlayer owner) {
        super(game, owner);
    }
    
}
