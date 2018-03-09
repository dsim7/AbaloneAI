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

    public AbaloneState(Set<AbaloneCoord> p1Pieces, Set<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;

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

        for (int j = 0; j < moves.size(); j++) {
            AbaloneState nextState = this.getNextState(moves.get(j));
            allNextStates.add(nextState);
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

        Set<AbaloneCoord> set = new HashSet<AbaloneCoord>(movingPieces);
        if (pushedPieces != null) {
            for (AbaloneCoord coord : pushedPieces) {
                set.add(coord);
            }
        }
        
        // copy player pieces
        for (AbaloneCoord coord : p1Pieces) {
            if (!set.add(coord)) {
                AbaloneCoord newCoord = new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy);
                if (newCoord.isValid()) {
                    newP1Pces.add(newCoord);
                } else { 
                    System.out.println("Piece pushed out: " + newCoord);
                }
            } else {
                newP2Pces.add(new AbaloneCoord(coord.x, coord.y));
            }
        }
        for (AbaloneCoord coord : p2Pieces) {
            if (!set.add(coord)) {
                AbaloneCoord newCoord = new AbaloneCoord(coord.x + direction.dx, coord.y + direction.dy);
                if (newCoord.isValid()) {
                    newP2Pces.add(newCoord);
                }
            } else {
                newP2Pces.add(new AbaloneCoord(coord.x, coord.y));
            }
        }
       /*
        // determine who is the moving player and who is pushed player
        List<AbaloneCoord> movingPlayerPieces = (turn % 2 == 0 ? newP1Pces : newP2Pces);
        List<AbaloneCoord> pushedPlayerPieces = (turn % 2 == 0 ? newP2Pces : newP1Pces);
        
        // move moving pieces
        for (AbaloneCoord movingPiece : movingPieces) {
            for (AbaloneCoord playerPiece : movingPlayerPieces) {
                if (movingPiece.equals(playerPiece)) {
                    playerPiece.setCoord(playerPiece.x + direction.dx, playerPiece.y + direction.dy);
                }
            }
        }
        // move pushed pieces
        if (pushedPieces != null) {
            for (AbaloneCoord pushedPiece : pushedPieces) {
                for(AbaloneCoord playerPiece : pushedPlayerPieces) {
                    if (pushedPiece.equals(playerPiece)) {
                        playerPiece.setCoord(playerPiece.x + direction.dx, playerPiece.y + direction.dy);
                    }
                }
            }
        }
*/
        AbaloneState nextState = new AbaloneState(newP1Pces, newP2Pces, turn + 1);

        return nextState;
    }

    public String toString() {
        TreeSet<AbaloneCoord> ordered = new TreeSet<AbaloneCoord>();
        String toWrite = "";
        for (AbaloneCoord blackPiece : this.p1Pieces) {
            char row = (char) (blackPiece.y + 65);
            int col = blackPiece.x + 1;
            toWrite += "" + row + col + "b,";
        }
        for (AbaloneCoord whitePiece : this.p2Pieces) {
            char row = (char) (whitePiece.y + 65);
            int col = whitePiece.x + 1;
            toWrite += "" + row + col + "w,";
        }
        toWrite = toWrite.replaceAll(",$", "");
        return toWrite;
    }

}
