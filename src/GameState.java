import java.util.List;

public interface GameState {
    List<GameState> getAllNextStates();
    
    int getStateValue();
    
}
