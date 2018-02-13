package game;

public class GamePiece extends GameComponent {
    GamePosition position;
    
    public GamePiece(Game game) {
        super(game);
    }
    
    public GamePiece(Game game, GamePlayer owner) {
        super(game, owner);
    }
    
    public GamePiece(Game game, GamePosition position) {
        super(game);
        this.position = position;
    }
    
    public GamePiece(Game game, GamePlayer player, GamePosition position) {
        super(game, player);
        this.position = position;
        if (position != null) {
            position.addPiece(this);
        }
    }
    
    public void move(GamePosition position) {
        if (position != null) {
            if (this.position != null)
                this.position.removePiece(this);
            this.position = position;
            position.addPiece(this);
        } else {
            remove();
        }
        
    }
    
    public void remove() {
        if (this.position != null)
            this.position.removePiece(this);
        this.position = null;
    }
    
    public GamePosition getPosition() {
        return position;
    }
}
