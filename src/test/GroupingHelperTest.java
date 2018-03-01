import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GroupingHelperTest {

    @Test
    public void testGenerateAllGroupsWith3Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithTriangle3Pieces());

        assertTrue(groups.size() == 6);
    }

    @Test
    public void testGenerateAllGroupsWithInlines2Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithInline2Pieces());

        assertTrue(groups.size() == 3);
    }


    @Test
    public void testGenerateAllGroupsWithInlines3Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithInline3Pieces());

        assertTrue(groups.size() == 6);
    }

    @Test
    public void testGenerateAllGroupsWithInlines4Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithInline4Pieces());

        assertTrue(groups.size() == 9);
    }

    @Test
    public void testGenerateAllGroupsWithInlines5Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithInline5Pieces());

        assertTrue(groups.size() == 12);
    }

    @Test
    public void testGenerateAllGroupsWithMiddle7Pieces() {
        List<List<AbaloneCoord>> groups = GroupingHelper.generateGroups(mockPlayerPieceCoordinatesWithMiddle7Pieces());

        assertTrue(groups.size() == 22);
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithTriangle3Pieces() {
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(0,0));
        playerPieces.add(new AbaloneCoord(1,1));
        playerPieces.add(new AbaloneCoord(1,0));

        return playerPieces;
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithInline2Pieces() {
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(0,0));
        playerPieces.add(new AbaloneCoord(1,1));

        return playerPieces;
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithInline3Pieces() {
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(0,0));
        playerPieces.add(new AbaloneCoord(1,1));
        playerPieces.add(new AbaloneCoord(2,2));

        return playerPieces;
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithInline4Pieces() {
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(0,0));
        playerPieces.add(new AbaloneCoord(1,0));
        playerPieces.add(new AbaloneCoord(2,0));
        playerPieces.add(new AbaloneCoord(3,0));

        return playerPieces;
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithInline5Pieces() {
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(0,0));
        playerPieces.add(new AbaloneCoord(1,0));
        playerPieces.add(new AbaloneCoord(2,0));
        playerPieces.add(new AbaloneCoord(3,0));
        playerPieces.add(new AbaloneCoord(4,0));

        return playerPieces;
    }

    public List<AbaloneCoord> mockPlayerPieceCoordinatesWithMiddle7Pieces() {
        // 3,4 3,3 4,3 5,4 5,5 4,4 4,5
        final List<AbaloneCoord> playerPieces = new ArrayList<AbaloneCoord>();
        playerPieces.add(new AbaloneCoord(3,4));
        playerPieces.add(new AbaloneCoord(3,3));
        playerPieces.add(new AbaloneCoord(4,3));
        playerPieces.add(new AbaloneCoord(5,4));
        playerPieces.add(new AbaloneCoord(5,5));
        playerPieces.add(new AbaloneCoord(4, 4));
        playerPieces.add(new AbaloneCoord(4,5));

        return playerPieces;
    }
}
