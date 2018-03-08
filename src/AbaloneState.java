import java.util.ArrayList;
import java.util.List;

public class AbaloneState implements GameState {
    List<AbaloneCoord> p1Pieces = new ArrayList<AbaloneCoord>();
    List<AbaloneCoord> p2Pieces = new ArrayList<AbaloneCoord>();
    int turn;
    List<AbaloneCoord> newP1Pces = new ArrayList<AbaloneCoord>();
    List<AbaloneCoord> newP2Pces = new ArrayList<AbaloneCoord>();
    AbaloneState newState;

    public AbaloneState(List<AbaloneCoord> p1Pieces, List<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;
    }

    public List<AbaloneCoord> getP1Pieces() {
        return newP1Pces;
    }

    public List<AbaloneCoord> getP2Pieces() {
        return newP2Pces;
    }

    @Override
    public int getStateValue() {
        return 0;
    }

    @Override
    /**
     * Takes the list of moves, iterates through it, and calls getNextState() on
     * every move, then adds all the new states into a List of <AbaloneState>
     */
    public List<AbaloneState> getAllNextStates() {
        
        List<AbaloneMove> moves = MoveHelper.generateAllMoves(null, null, null); // this would pass in real information
        List<AbaloneState> newStates = new ArrayList<AbaloneState>();

        AbaloneState newState = new AbaloneState(p1Pieces, p2Pieces, turn);
        
        for (int j = 0; j < moves.size(); j++) {

            newState.getNextState(moves.get(j));
            newStates.add(newState);

        }

        return newStates;
    }

    /**
     * Generates the new Abalone game state based off the move passed in.
     * 
     * @param move:
     *            (List<AbaloneCoord> movingPieces, List<AbaloneCoord>
     *            pushingPieces, Abalone.Dir direction, boolean isInlineMove,
     *            int numPushedPieces)
     */
    AbaloneState getNextState(AbaloneMove move) {

        List<AbaloneCoord> movingPieces;
        List<AbaloneCoord> pushedPieces;
        Abalone.Dir direction;

        movingPieces = move.getMovingPieces();
        pushedPieces = move.getPushedPieces();
        direction = move.getDirection();

        for (AbaloneCoord coord : p1Pieces) {
            newP1Pces.add(coord);
        }
        
        for (AbaloneCoord coord : p2Pieces) {
            newP2Pces.add(coord);
        }
        
        if (turn % 2 == 0) {

            for (AbaloneCoord coord : movingPieces) {

                for (int i = 0; i < p1Pieces.size(); i++) {

                    if (coord.equals(p1Pieces.get(i))) {
                        newP1Pces.remove(i);
                        newP1Pces.add(new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }

            for (AbaloneCoord coord : pushedPieces) {

                for (int i = 0; i < p2Pieces.size(); i++) {

                    if (coord.equals(p2Pieces.get(i))) {
                        newP2Pces.remove(i);
                        newP2Pces.add(new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }

        } else {

            for (AbaloneCoord coord : pushedPieces) {

                for (int i = 0; i < p1Pieces.size(); i++) {

                    if (coord.equals(p1Pieces.get(i))) {
                        newP1Pces.remove(i);
                        newP1Pces.add(new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }

            for (AbaloneCoord coord : movingPieces) {

                for (int i = 0; i < p2Pieces.size(); i++) {

                    if (coord.equals(p2Pieces.get(i))) {
                        newP2Pces.remove(i);
                        newP2Pces.add(new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }
        }

        newState = new AbaloneState(newP1Pces, newP2Pces, turn + 1); 
         
        return newState;
    }

}
