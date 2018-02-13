package game;

/**
 * Parts of GUIs which store information (such as Game Components)
 * that can be loaded into Actions.
 *  
 * @author dylan
 *
 */
public interface GameGUIComponent {
    GameGUI getGUI();
    
    GameComponent getComponent();
    
}
