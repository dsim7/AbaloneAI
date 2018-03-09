package abalone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            allNextStates.add(ref);
            
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

        Set<AbaloneCoord> newP1PieceSet = new HashSet<AbaloneCoord>();
        Set<AbaloneCoord> newP2PieceSet = new HashSet<AbaloneCoord>();
        List<AbaloneCoord> newP1PieceList = new ArrayList<>();
        List<AbaloneCoord> newP2PieceList = new ArrayList<>();
        List<AbaloneCoord> movingPieces;
        List<AbaloneCoord> pushedPieces;
        Abalone.Dir direction;

        movingPieces = move.getMovingPieces();
        pushedPieces = move.getPushedPieces();
        direction = move.getDirection();
        newP1PieceSet.clear();
        newP2PieceSet.clear();
        newP1PieceList.clear();
        newP2PieceList.clear();

        for (AbaloneCoord coord : p1Pieces) {
            newP1PieceList.add(new AbaloneCoord(coord.x, coord.y));
        }

        for (AbaloneCoord coord : p2Pieces) {
            newP2PieceList.add(new AbaloneCoord(coord.x, coord.y));
        }
       
        if (turn % 2 == 0) {

            for (AbaloneCoord coord : movingPieces) {
                
                for (AbaloneCoord coords : p1Pieces ) {
                    if(coord.equals(coords)) {
                        newP1PieceList.remove(coords);
                        newP1PieceList.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }
            if (pushedPieces != null) {
                for (AbaloneCoord coord : pushedPieces) {
                    
                    for(AbaloneCoord coords : p2Pieces) {
                        if(coord.equals(coords)) {
                            newP2PieceList.remove(coords);
                            newP2PieceList.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                        }
                    }
                 
                }
            }
        } else {
            if (pushedPieces != null) {
                for (AbaloneCoord coord : pushedPieces) {
                    for(AbaloneCoord coords : p1Pieces) {
                        if(coord.equals(coords)) {
                            newP1PieceList.remove(coords);
                            newP1PieceList.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                        }
                    }
                }
            }

            for (AbaloneCoord coord : movingPieces) {

                for (AbaloneCoord coords : p2Pieces) {
                    if(coord.equals(coords)) {
                        newP2PieceList.remove(coords);
                        newP2PieceList.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }
        }
        
        for(AbaloneCoord coord: newP1PieceList) {
            newP1PieceSet.add(new AbaloneCoord(coord.x, coord.y));
        }
        for(AbaloneCoord coord: newP2PieceList) {
            newP2PieceSet.add(new AbaloneCoord(coord.x, coord.y));
        }
        
        AbaloneState nextState = new AbaloneState(newP1PieceSet, newP2PieceSet, turn + 1);

        return nextState;
    }

    public String toString() {
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
