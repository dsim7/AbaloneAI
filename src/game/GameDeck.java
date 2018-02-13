package game;

import java.util.ArrayList;

public class GameDeck {
    private ArrayList<GameCard> cards = new ArrayList<GameCard>();
    
    public GameCard getTopCard() {
        return cards.get(0);
    }
    
    public GameCard getCard(int index) {
        return cards.get(index);
    }
    
}
