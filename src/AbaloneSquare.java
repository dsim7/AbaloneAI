
import game.Game;
import game.GamePiece;
import game.GamePlayer;
import game.GamePosition;

public class AbaloneSquare extends GamePosition {
    
    int x, y;
    
    public AbaloneSquare(Game game, int x, int y) {
        super(game);
        this.x = x;
        this.y = y;
    }
    
    public GamePiece getPiece() {
        if (getPieces().size() > 0) {
            return getPieces().get(0);
        }
        return null;
    }

    public boolean containsEnemy(GamePlayer player) {
        return getPiece() != null && getPiece().getOwner() != player;
    }
    
    public boolean containsFriendly(GamePlayer player) {
        return getPiece() != null && getPiece().getOwner() == player;
    }
    
    public boolean isEmpty(GamePlayer player) {
        return getPiece() == null;
    }
}
