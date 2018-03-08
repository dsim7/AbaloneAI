import java.util.List;

public interface GameState {
    List<AbaloneState> getAllNextStates();
    
    int getStateValue();
    
}
