package abalone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AbaloneState {
    Set<AbaloneCoord> p1Pieces = new HashSet<AbaloneCoord>();
    Set<AbaloneCoord> p2Pieces = new HashSet<AbaloneCoord>();
    int turn;
    AbaloneState nextState;

    public AbaloneState(Set<AbaloneCoord> p1Pieces, Set<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;

    }

    public Set<AbaloneCoord> getP1Pieces() {
        return p1Pieces;
    }

    public Set<AbaloneCoord> getP2Pieces() {
        return p2Pieces;
    }

    public int getStateValue() {
        return 0;
    }

    /**
     * Takes the list of moves, iterates through it, and calls getNextState() on
     * every move, then adds all the new states into a List of <AbaloneState>
     */
    public List<AbaloneState> getAllNextStates() {

        List<List<AbaloneCoord>> groups = new ArrayList<List<AbaloneCoord>>();

        if (turn % 2 == 0) {
            groups = GroupingHelper.generateGroups(p1Pieces);
        } else {
            groups = GroupingHelper.generateGroups(p2Pieces);
        }
        
        List<AbaloneMove> moves = MoveHelper.generateAllMoves(groups, p1Pieces, p2Pieces);                                                                    
        List<AbaloneState> allNextStates = new ArrayList<AbaloneState>();
        AbaloneState nextState = new AbaloneState(p1Pieces, p2Pieces, turn);

        for (int j = 0; j < moves.size(); j++) {
            AbaloneState ref;
            ref = nextState.getNextState(moves.get(j));
            if (ref != null) {   // move was a suicide move
                allNextStates.add(ref);
            }
        } 
        
        return allNextStates;
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
        Set<AbaloneCoord> newP1Pces = new HashSet<AbaloneCoord>();
        Set<AbaloneCoord> newP2Pces = new HashSet<AbaloneCoord>();
        List<AbaloneCoord> movingPieces = move.getMovingPieces();
        List<AbaloneCoord> pushedPieces = move.getPushedPieces();
        Abalone.Dir direction = move.getDirection();

        // add movingPieces and pushedPieces into the same set of changed pieces
        Set<AbaloneCoord> changedPieces = new HashSet<AbaloneCoord>(movingPieces);
        if (pushedPieces != null) {
            for (AbaloneCoord coord : pushedPieces) {
                changedPieces.add(coord);
            }
        }
        
        // copy player pieces, move if it is a moving piece
        for (AbaloneCoord coord : p1Pieces) {
            if (!changedPieces.add(coord)) {   // if add fails, it is a moving piece
                AbaloneCoord newCoord = new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy);
                if (newCoord.isValid()) {  // if it is pushed to an out of bounds coord, don't add
                    newP1Pces.add(newCoord);
                } else {
                    if (turn % 2 == 0) {  // player1 piece moved player1 piece off bounds
                        return null;
                    }
                }
            } else {
                newP1Pces.add(new AbaloneCoord(coord.x, coord.y));
            }
        }
        // copy player pieces, move if it is a pushed piece
        for (AbaloneCoord coord : p2Pieces) {
            if (!changedPieces.add(coord)) {  // if add fails, it is a pushed piece
                AbaloneCoord newCoord = new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy);
                if (newCoord.isValid()) {  // if it is pushed to an out of bounds coord, don't add
                    newP2Pces.add(newCoord);
                } else {
                    if (turn % 2 == 1) { // player2 piece moved player2 piece off bounds
                        return null;
                    }
                }
            } else {
                newP2Pces.add(new AbaloneCoord(coord.x, coord.y));
            }
        
        }
        AbaloneState nextState = new AbaloneState(newP1Pces, newP2Pces, turn + 1);
        return nextState;
    }

    public String toString() {
        TreeSet<AbaloneCoord> p1ordered = new TreeSet<AbaloneCoord>();
        TreeSet<AbaloneCoord> p2ordered = new TreeSet<AbaloneCoord>();
        for (AbaloneCoord p1coord : p1Pieces) {
            p1ordered.add(p1coord);
        }
        for (AbaloneCoord p2coord : p2Pieces) {
            p2ordered.add(p2coord);
        }
        
        String toWrite = "";
        for (AbaloneCoord blackPiece : p1ordered) {
            char row = (char) (blackPiece.y + 65);
            int col = blackPiece.x + 1;
            toWrite += "" + row + col + "b,";
        }
        for (AbaloneCoord whitePiece : p2ordered) {
            char row = (char) (whitePiece.y + 65);
            int col = whitePiece.x + 1;
            toWrite += "" + row + col + "w,";
        }
        toWrite = toWrite.replaceAll(",$", "");
        return toWrite;
    }

}
