package game;

import java.util.HashMap;

/**
 * Actions are the main way Players will interact with the Game.
 * Actions hold parameters until they are eventually ready to be
 * executed and when executed will change the state of the game 
 * in some way.
 * 
 * @author dylan
 *
 */
public abstract class GameAction {
    private HashMap<String, Object> params = new HashMap<String, Object>();
    
    /**
     * Determines how an Action loads and stores parameters passed to it. By default,
     * parameters are stored in a HashTable. Parameters should be validated here.
     * 
     * @param key
     * @param param
     */
    protected void load(String key, Object param) {
        storeParam(key, param);
    }

    /**
     * Determines when every parameter which must be set has been set. If 
     * Action is not ready, it will not attempt to execute.
     * 
     * @return
     */
    protected boolean ready(GamePlayer player) { return false; }
    
    /**
     * This is method is run when attempting to execute this Action and determines
     * whether it is appropriate to execute the Action given the parameters it has
     * been loaded with. If validation fails, the Action is unset.
     * 
     * @return
     */
    protected boolean validate(GamePlayer player) {
        return true;
    }

    /**
     * Events which happen after an Action has been executed successfully.
     */
    protected void onSuccess(GamePlayer player) { }

    /**
     * Events which happen when this Action is executed.
     */
    protected void execute(GamePlayer player) { }
    
    /**
     * Events which happen after an Action has been executed successfully.
     */
    protected void onFailure(GamePlayer player) { }

    /**
     * Determines whether after this Action is successfully executed, it is unset.
     * 
     * @return
     */
    protected boolean unsetOnExecute(GamePlayer player) {
        return true;
    }

    /**
     * Events which take place as this Action is unsetting. (Example: deselecting
     * GamePieces which were selected when loaded.)
     */
    protected void onUnset(GamePlayer player) { };
    
    /**
     * Stores a parameter into this Action's HashTable of parameters.
     * 
     * @param key
     * @param param
     */
    protected final void storeParam(String key, Object param) {
        params.put(key, param);
    }

    /**
     * Retrieves a parameter from this Action's HashTable of parameters.
     * 
     * @param key
     * @return
     */
    protected final Object getParam(String key) {
        return params.get(key);
    }
}