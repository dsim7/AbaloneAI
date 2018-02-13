package game;

import java.util.ArrayList;
import java.util.List;

/**
 * The agent of the Game. They interact with the Game through 
 * their Action.
 * 
 * @author dylan
 *
 */
public class GamePlayer extends GameComponent {
    private GameAction action;
    private Game game;
    private GameGUI gui;
    private boolean isOut;
    private boolean isActive;
    private List<GameCard> hand = new ArrayList<GameCard>();
    
    private GameTrigger lastTrigger;
    
    public GamePlayer(Game game) {
        super(game);
        this.game = game;
    }
    
    public Game getGame() {
        return game;
    }
    
    public GameGUI getGUI() {
        return gui;
    }
    
    public void setGUI(GameGUI gui) {
        this.gui = gui;
    }

    public void setAction(GameAction action) {
        if (this.action != null) {
            unsetAction();
        }

        this.action = action;
    }

    public void loadAction(String key, Object param) {
        if (this.action != null) {
            this.action.load(key, param);
        }
    }
    
    public Object getActionParam(String key) {
        return action == null ? null : action.getParam(key);
    }
    
    public void unsetAction() {
        GameAction tmp = action;
        this.lastTrigger = null;
        this.action = null;
        if (tmp != null) {
            tmp.onUnset(this);
        }
    }

    public final int trigger(GameTrigger trigger) {
        int result = -1;
        if ((!trigger.playerMustBeActive(this) || isActive())) {
            if (trigger.canUntrigger(this) && lastTrigger != null && lastTrigger.equals(trigger)) {
                trigger.onUntrigger(this);
                if (trigger.validateUntrigger(this)) {
                    unsetAction();
                }
            } else {
                if (trigger.validate(this)) {
                    trigger.onTrigger(this);
                    GameAction getAction = trigger.getAction(this);
                    if (getAction != null) {
                        if (trigger.overrideCurrentAction(this) || this.action == null) {
                            setAction(getAction);
                            lastTrigger = trigger;
                        }
                    }
                    if (this.action != null) {
                        trigger.loadAction(this);
                        if (action.ready(this)) {
                            lastTrigger = null;
                            GameAction tmp = action;
                            if (action != null && action.unsetOnExecute(this)) {
                                unsetAction();
                            }
                            if (tmp.validate(this)) {   
                                tmp.execute(this);
                                tmp.onSuccess(this);
                                result = 1;
                            } else {
                                if (action != null && action.unsetOnExecute(this)) {
                                    unsetAction();
                                }
                                tmp.onFailure(this);
                                result = 0;
                            }
                        } else {
                        }
                    } else {
                    }
                } else {
                    trigger.onFailure(this);
                }
            }
        }
        game.updateGUIs();
        return result;
    }
  
    public final boolean executeAction() {
        if (action != null) {
            
        }
        return false;
    }
    
    public void draw(List<GameCard> deck) {
        hand.add(deck.remove(0));
    }
    
    public void discard(GameCard card) {
        hand.remove(card);
    }

    public void setActive(boolean active) {
        boolean wasActive = isActive;
        isActive = active;
        if (!active && wasActive) {
            unsetAction();
        }
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setOut(boolean out) {
        boolean wasOut = isOut;
        isOut = out;
        if (!out && wasOut) {
            unsetAction();
        }
    }
    
    public boolean isOut() {
        return isOut;
    }
    
}
