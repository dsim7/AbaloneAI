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

       /* for (int j = 0; j < moves.size(); j++) {
            AbaloneState nextState = this.getNextState(moves.get(j));
            allNextStates.add(nextState);
            
        } */
        
        AbaloneState a = new AbaloneState(p1Pieces, p2Pieces, turn);
        AbaloneState b = new AbaloneState(p1Pieces, p2Pieces, turn);
      
        a.getNextState(moves.get(0));
        b.getNextState(moves.get(1));
        
        allNextStates.add(a);
        allNextStates.add(b);
        
        System.out.println("\nAfter the return");
        for(AbaloneCoord coord : a.p2Pieces) {
            System.out.print(" " + coord);

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
        List<AbaloneCoord> movingPieces;
        List<AbaloneCoord> pushedPieces;
        Abalone.Dir direction;

        movingPieces = move.getMovingPieces();
        pushedPieces = move.getPushedPieces();
        direction = move.getDirection();
        newP1Pces.clear();
        newP2Pces.clear();

        for (AbaloneCoord coord : p1Pieces) {
            newP1Pces.add(new AbaloneCoord(coord.x, coord.y));
        }

        for (AbaloneCoord coord : p2Pieces) {
            newP2Pces.add(new AbaloneCoord(coord.x, coord.y));
        }
       
        if (turn % 2 == 0) {

            for (AbaloneCoord coord : movingPieces) {
                
                for (AbaloneCoord coords : p1Pieces ) {
                    if(coord.equals(coords)) {
                        newP1Pces.remove(coords);
                        newP1Pces.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }
            if (pushedPieces != null) {
                for (AbaloneCoord coord : pushedPieces) {
                    
                    for(AbaloneCoord coords : p2Pieces) {
                        if(coord.equals(coords)) {
                            newP2Pces.remove(coords);
                            newP2Pces.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                        }
                    }
                 
                }
            }
        } else {
            if (pushedPieces != null) {
                for (AbaloneCoord coord : pushedPieces) {
                    for(AbaloneCoord coords : p1Pieces) {
                        if(coord.equals(coords)) {
                            newP1Pces.remove(coords);
                            newP1Pces.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                        }
                    }
                }
            }

            for (AbaloneCoord coord : movingPieces) {

                for (AbaloneCoord coords : p2Pieces) {
                    if(coord.equals(coords)) {
                        newP2Pces.remove(coords);
                        newP2Pces.add(new AbaloneCoord(coords.x + direction.dx, coord.y + direction.dy));
                    }
                }
            }
        }

        AbaloneState nextState = new AbaloneState(newP1Pces, newP2Pces, turn + 1);

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
