package game;

public abstract class GameTrigger {
    /**
     * Determines whether a player must be Active to activate this
     * trigger. If true, and a non-Active player attempts to activate
     * this trigger, nothing happens.
     * 
     * @return
     */
    protected boolean playerMustBeActive(GamePlayer player) { return true; }
    
    /**
     * Validates that a player is allowed to activate this trigger and
     * possibly that they confirm they are activating it. If validation
     * fails, nothing happens.
     * 
     * @param player
     * @return
     */
    protected boolean validate(GamePlayer player) { return true; }
    
    /**
     * Any initiation that is to be done with the Action.
     * 
     * @param player
     * @return
     */
    //protected boolean onSetAction(GamePlayer player) { return true; }
    
    /**
     * If this trigger has an Action from getGameAction(), and a player activates
     * this trigger while they currently have an Action of the same type as 
     * this trigger's Action, this determines whether they will unset their Action.
     * 
     * @return
     */
    protected boolean canUntrigger(GamePlayer player) { return true; }

    /**
     * Any initiation that is to be done with the Action.
     * 
     * @param player
     * @return
     */
    //protected boolean onSetAction(GamePlayer player) { return true; }
    
    /**
     * Determines whether a player confirms that they are untriggering this trigger.
     * If they do, their action is unset. If not, nothing happens.
     * 
     * @param player
     * @return
     */
    protected boolean validateUntrigger(GamePlayer player) { return true; }

    /**
     * Notifies the player they have untriggered this trigger and their action has
     * been unset.
     * 
     * @param player
     */
    protected void onUntrigger(GamePlayer player) { }

    /**
     * Any initiation that is to be done with the Action.
     * 
     * @param player
     * @return
     */
    //protected boolean onSetAction(GamePlayer player) { return true; }
    
    protected void onTrigger(GamePlayer player) { }

    /**
     * If this trigger has an Action from getGameAction(), this determines
     * whether this trigger will override any current Action which the player
     * has with this trigger's Action.
     * 
     * @return
     */
    protected boolean overrideCurrentAction(GamePlayer player) { return true; }

    /**
     * Returns a new Action which a player may be set with upon activating
     * this trigger.
     * 
     * @return
     */
    protected abstract GameAction getAction(GamePlayer player);

    //protected void initAction(GamePlayer player) { }

    /**
     * Loads the player's Action with a suite of parameters. This may
     * be initializing an Action they were just set with from getGameAction()
     * or loading an Action they already had.
     * 
     * @param player
     */
    protected abstract void loadAction(GamePlayer player);

    protected void onFailure(GamePlayer player) { }

    /**
     * Events which happen after a trigger successfully executes its Action.
     * 
     * @param player
     */
    //protected void afterExecute(GamePlayer player) { }
    
}
