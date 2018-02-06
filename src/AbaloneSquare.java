import java.util.List;

import game.Game;
import game.GameAction;
import game.GamePiece;
import game.GamePlayer;
import game.GamePosition;
import game.GameTrigger;

public class AbaloneSquare extends GamePosition {
    
    int x, y;
    /*
    GameTrigger selectTrigger = new AbaloneTriggerSelect();
    GameTrigger moveTrigger = new AbaloneTriggerMove();
    */
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
    /*
    class AbaloneTriggerSelect extends GameTrigger {
        GamePiece piece;

        @Override
        protected GameAction getAction(GamePlayer player) {
            return new GameAction() {
                
                @Override
                protected boolean ready(GamePlayer player) {
                    return (GamePiece) getParam("piece") != null && (GamePosition) getParam("target") != null;
                }
                
                @Override
                protected void execute(GamePlayer player) {
                    ((Abalone)player.getGame()).movePiece(((GamePiece) getParam("piece")), ((GamePosition) getParam("target")));
                }
                
                @Override
                protected boolean validate(GamePlayer player) {
                    return true;
                }
                
                @Override
                protected void onUnset(GamePlayer player) {
                    if (piece != null) {
                        piece.selected = false;
                    }
                }

                @Override
                protected void load(String key, Object param) {
                    super.load(key, param);
                    if (key.equals("piece")) {
                        if (piece != null) {
                            piece.selected = false;
                        }
                        piece = (AbalonePiece) getParam("piece");
                        if (piece != null) {
                            piece.selected = true;
                        }
                    }
                }
            };
        }

        @Override
        protected void loadAction(GamePlayer player) {
            if (getPiece() != null && !containsEnemy(player)) {   
                player.loadAction("piece", AbaloneSquare.this.getPiece());
            }
            
        }
        
        @Override
        protected boolean overrideCurrentAction(GamePlayer player) {
            return false;
        }
    }
    
    class AbaloneTriggerMove extends GameTrigger {

        @Override
        protected GameAction getAction(GamePlayer player) {
            return null;
        }

        @Override
        protected void loadAction(GamePlayer player) {
            player.loadAction("target", AbaloneSquare.this);
        }
        
        protected boolean validate(GamePlayer player) {
            return AbaloneSquare.this.isEmpty(player) || AbaloneSquare.this.containsEnemy(player);
        }
        
    }*/

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
