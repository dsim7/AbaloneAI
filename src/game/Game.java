package game;

import java.util.ArrayList;
import java.util.List;

/**
 * The core component. Information on the current state of the game is stored here.
 * Constants and static information will also be stored here.
 * 
 * master
 * 
 * @author dylan
 *
 */
public abstract class Game {
    protected List<GamePlayer> players = new ArrayList<GamePlayer>();
    protected GamePlayer curPlayer;
    private boolean gameOver = false;
    
    
    public List<GamePlayer> getPlayers() {
        return players;
    }
    
    public GamePlayer getCurPlayer() {
        return curPlayer;
    }
    
    public void addPlayer(GamePlayer player) {
        players.add(player);
        if (players.size() == 1) {
            setCurPlayer(player);
        }
    }
    
    public void nextPlayerTurn() {
        do {
            if (players.indexOf(curPlayer) != players.size() - 1) {
                setCurPlayer(players.get(players.indexOf(curPlayer) + 1));
            } else {
                setCurPlayer(players.get(0));
            }
        } while (curPlayer.isOut());
    }
    
    public void setCurPlayer(GamePlayer player) {
        if (this.curPlayer != null) {
            this.curPlayer.setActive(false);
        }
        this.curPlayer = player;
        this.curPlayer.setActive(true);
    }

    public void updateGUIs() {
        for (GamePlayer player : players) {
            if (player.getGUI() != null) {
                player.getGUI().updateGUI();
            }
        }
    }

    public boolean gameOver() {
        return gameOver;
    }
    
    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    
}

