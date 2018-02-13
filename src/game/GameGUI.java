package game;


/**
 * Users use the GUI to set off Events.
 * 
 * @author dylan
 *
 */
public interface GameGUI {  
    Game getGame();
    
    GamePlayer getPlayer();
    
    void updateGUI();
}

