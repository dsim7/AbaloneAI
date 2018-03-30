package abalone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AbaloneState {
    Set<AbaloneCoord> p1Pieces = new HashSet<AbaloneCoord>();
    Set<AbaloneCoord> p2Pieces = new HashSet<AbaloneCoord>();
    int turn;
    double stateValue = Double.MIN_VALUE;
    
    private List<AbaloneState> allNextStates = null;
    
    private List<AbaloneMove> allRedMoves = null;
    private List<AbaloneMove> allBlueMoves = null;
    
    public AbaloneState(Set<AbaloneCoord> p1Pieces, Set<AbaloneCoord> p2Pieces, int turn) {
        this.p1Pieces = p1Pieces;
        this.p2Pieces = p2Pieces;
        this.turn = turn;

    }

    public Set<AbaloneCoord> getCurPlayerPieces() {
        return turn % 2 == 0 ? p1Pieces : p2Pieces;
    }

    public Set<AbaloneCoord> getEnemyPieces() {
        return turn % 2 == 0 ? p2Pieces : p1Pieces;
    }

    public double getStateValueRedPerspective() {
        //long time = System.nanoTime();
        if (stateValue == Double.MIN_VALUE) {
            if (p1Pieces.size() <= 8) {
                stateValue = Double.MIN_VALUE;
            } else if (p2Pieces.size() <= 8) {
                stateValue = Double.MAX_VALUE;
            } else {
                double result = 0;
                result += valueMovesRedPerspective();
                result += valueCoordsRedPerspective();
                stateValue = result;
            }
        }
        //System.out.println("Time to get value : " + (System.nanoTime() - time));
        return stateValue;
    }

    public List<AbaloneMove> getAllNextMoves() {
        
        return turn % 2 == 0 ? getRedMoves() : getBlueMoves();   
    }
    
    public List<AbaloneMove> getRedMoves() {
        if (allRedMoves == null) {
            Set<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(p1Pieces); 
            allRedMoves = MoveHelper.generateAllMoves(groups, p1Pieces, p2Pieces);
        }
        return allRedMoves;   
    }
    
    public List<AbaloneMove> getBlueMoves() {
        if (allBlueMoves == null) {
            Set<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(p2Pieces); 
            allBlueMoves = MoveHelper.generateAllMoves(groups, p1Pieces, p2Pieces);
        }
        return allBlueMoves;   
    }
    
    /**
     * Takes the list of moves, iterates through it, and calls getNextState() on
     * every move, then adds all the new states into a List of <AbaloneState>
     */
    public List<AbaloneState> getAllNextStates() {
        if (allNextStates == null) {
            List<AbaloneMove> moves = getAllNextMoves();                                                                    
            List<AbaloneState> allNextStates = new ArrayList<AbaloneState>();
    
            for (int j = 0; j < moves.size(); j++) {
                AbaloneState nextState;
                nextState = this.getNextState(moves.get(j));
                if (nextState != null) {   // move was a suicide move
                    allNextStates.add(nextState);
                }
            }
            this.allNextStates = allNextStates;
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
    public AbaloneState getNextState(AbaloneMove move) {
        //if (!calculatedStates.containsKey(move)) {
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
                    } /* else {
                        if (turn % 2 == 0) {  // player1 piece moved player1 piece off bounds
                            return null;
                        }
                    } */
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
                    } /*else {
                        if (turn % 2 == 1) { // player2 piece moved player2 piece off bounds
                            return null;
                        }
                    } */
                } else {
                    newP2Pces.add(new AbaloneCoord(coord.x, coord.y));
                }
            
            }
            AbaloneState nextState = new AbaloneState(newP1Pces, newP2Pces, turn + 1);
            //calculatedStates.put(move, nextState);
            return nextState;
        //} else {
        //    return calculatedStates.get(move);
        //}
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
        toWrite += (" ---> " + getStateValueRedPerspective());
        toWrite = toWrite.replaceAll(",$", "");
        return toWrite;
    }

    private double valueMovesRedPerspective() {
        double movesValue = 0;
        for (AbaloneMove move : getRedMoves()) {
            movesValue += move.getValue();
        }
        for (AbaloneMove move : getBlueMoves()) {
            movesValue -= move.getValue();
        }
        return movesValue / (getRedMoves().size() + getBlueMoves().size());
    }
    
    private double valueCoordsRedPerspective() {
        double coordsSum = 0;
        for (AbaloneCoord redCoord : p1Pieces) {
            coordsSum += AbaloneCoord.coordDistValues.get(redCoord);
        }
        for (AbaloneCoord blueCoord : p2Pieces) {
            coordsSum -= AbaloneCoord.coordDistValues.get(blueCoord);
        }
        return coordsSum;
    }
    
    
}
