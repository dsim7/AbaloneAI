package game;

import java.util.ArrayList;
import java.util.List;


public class GamePosition extends GameComponent {
    private List<GamePiece> pieces = new ArrayList<GamePiece>();

    public GamePosition(Game game) {
        super(game);
    }
    
    public GamePosition(Game game, GamePlayer player) {
        super(game, player);
    }
    
    public List<GamePiece> getPieces() {
        return pieces;
    }

    void addPiece(GamePiece piece) {
        if (!pieces.contains(piece)) {
            pieces.add(piece);
        }
    }
    
    void removePiece(GamePiece piece) {
        pieces.remove(piece);
    }
}
