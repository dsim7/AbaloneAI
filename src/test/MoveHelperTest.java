import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

public class MoveHelperTest {

    @Test
    public void testGenerateAllMoves() {
        List<List<AbaloneCoord>> group = generateMockGroups();
        Set<AbaloneCoord> p1Pieces = generatePlayerOnePieces();
        Set<AbaloneCoord> p2Pieces = generatePlayerTwoPieces();

        List<AbaloneMove> moves = MoveHelper.generateAllMoves(group, p1Pieces, p2Pieces);
        assertNotNull(moves);
    }

    private List<List<AbaloneCoord>> generateMockGroups() {

        List<List<AbaloneCoord>> groups = new ArrayList<>();
        List<AbaloneCoord> group = new ArrayList<>();
        group.add(new AbaloneCoord(1,1));
        group.add(new AbaloneCoord(2,1));
        group.add(new AbaloneCoord(3,1));
        groups.add(group);
        return groups;
    }

    private Set<AbaloneCoord> generatePlayerOnePieces() {

        Set<AbaloneCoord> coordinates = new HashSet<>();
        coordinates.add(new AbaloneCoord(1,1));
        coordinates.add(new AbaloneCoord(2,1));
        coordinates.add(new AbaloneCoord(3,1));
        return coordinates;
    }

    private Set<AbaloneCoord> generatePlayerTwoPieces() {

        Set<AbaloneCoord> coordinates = new HashSet<>();
        coordinates.add(new AbaloneCoord(0,1));
        coordinates.add(new AbaloneCoord(4,1));
        coordinates.add(new AbaloneCoord(2,2));
        coordinates.add(new AbaloneCoord(5,1));
        return coordinates;
    }

}
