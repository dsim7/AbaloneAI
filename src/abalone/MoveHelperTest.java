package abalone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class MoveHelperTest {

    @Test
    public void testGenerateAllMoves() {
        Set<List<AbaloneCoord>> group = generateMockGroups();
        Set<AbaloneCoord> p1Pieces = generatePlayerOnePieces();
        Set<AbaloneCoord> p2Pieces = generatePlayerTwoPieces();

        List<AbaloneMove> moves = MoveHelper.generateAllMoves(group, p1Pieces, p2Pieces);
        assertNotNull(moves);
        
        List<AbaloneState> newStates = new ArrayList<AbaloneState>();
        List<AbaloneCoord> p1Pces = new ArrayList<AbaloneCoord>();
        List<AbaloneCoord> p2Pces = new ArrayList<AbaloneCoord>();

    // Tests the getNextState method and prints out the different pieces lists
       /* p1Pces = moves.get(1).getMovingPieces();
        p2Pces = moves.get(1).getPushedPieces();

        System.out.println(moves.get(1).getDirection());
        System.out.println(moves.get(1).getNumPushedPieces());

        for (AbaloneCoord coord : p1Pces) {
            System.out.println(coord);
        }

        System.out.println("--------------------------");

        for (AbaloneCoord coord : p2Pces) {
            System.out.println(coord);
        }

        System.out.println("--------------------------\n\n NEW STATE");

        AbaloneState a = new AbaloneState(p1Pces, p2Pces, 2);

        a.getNextState(moves.get(1));

        System.out.println("P1 Pces\n----");
        for (AbaloneCoord coord : a.getP1()) {
            System.out.println(coord);
        }

        System.out.println("\nP2 Pces\n----");
        for (AbaloneCoord coord : a.getP2()) {
            System.out.println(coord);
        }*/
        
        for (int j = 0; j < moves.size(); j++) {
            if (moves.get(j).getIsInlineMove()) {
                p1Pces = moves.get(j).getMovingPieces();
                p2Pces = moves.get(j).getPushedPieces();
                
               // AbaloneState newState = new AbaloneState(p1Pces, p2Pces, 2);

              //  newState.getNextState(moves.get(j));
                //newStates.add(newState);
            }

        }
        
        
    }

    private Set<List<AbaloneCoord>> generateMockGroups() {

        Set<List<AbaloneCoord>> groups = new HashSet<>();
        List<AbaloneCoord> group = new ArrayList<>();
        group.add(new AbaloneCoord(1, 1));
        group.add(new AbaloneCoord(2, 1));
        group.add(new AbaloneCoord(3, 1));
        groups.add(group);
        return groups;
    }

    private Set<AbaloneCoord> generatePlayerOnePieces() {

        Set<AbaloneCoord> coordinates = new HashSet<>();
        coordinates.add(new AbaloneCoord(1, 1));
        coordinates.add(new AbaloneCoord(2, 1));
        coordinates.add(new AbaloneCoord(3, 1));
        return coordinates;
    }

    private Set<AbaloneCoord> generatePlayerTwoPieces() {

        Set<AbaloneCoord> coordinates = new HashSet<>();
        coordinates.add(new AbaloneCoord(0, 1));
        coordinates.add(new AbaloneCoord(4, 1));
        coordinates.add(new AbaloneCoord(2, 2));
        coordinates.add(new AbaloneCoord(5, 1));
        return coordinates;
    }

}
