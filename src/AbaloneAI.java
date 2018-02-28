import java.util.ArrayList;
import java.util.List;

public class AbaloneAI {
    
    public AbaloneMove getNextMove(AbaloneSquare[][] board) {
        return null;
    }
    
    private int getValueOfBoard(AbaloneSquare[][] board) {
        return 0;
    }
    
    public static boolean isValidMove(AbaloneState state, int x1, int y1, int x2, int y2, Abalone.Dir dir) {
        // gets the coordinates of the set of moving pieces
        List<AbaloneCoord> origin = getOriginCoords(x1, y1, x2, y2);
        
        // checks that origin pieces are aligned
        if (origin == null) {
            return false;
        }
        
        // determines if the set has at most 3 pieces
        if (!isSetRightSize(origin)) {
            return false;
        }
        
        // gets the current player's pieces
        List<AbaloneCoord> playerPieces = state.turn % 2 == 1 ? state.player1.pieces : state.player2.pieces;
        
        // gets the current player's enemy pieces
        List<AbaloneCoord> enemyPieces = state.turn % 2 == 1 ? state.player2.pieces : state.player1.pieces;
        
        // checks that the set of moving pieces is all owned by the current player
        if (!isAllContained(origin, playerPieces)) {
            return false;
        }
        
        // gets the coordinates of the destination of the set of moving pieces
        List<AbaloneCoord> dest = getDestCoords(origin, dir);
        
        // checks that the destination coordinates are unoccupied (or are currently
        // occupied by other pieces that are moving)
        if (!isDestUnoccupied(origin, dest, playerPieces)) {
            return false;
        }
        
        // determines if the move is an inline move
        if (isInlineMove(x1, y1, x2, y2, dir)) {
            // gets the 3 spaces in front of an inline move
            List<AbaloneCoord> pushed = getSpacesPushed(origin, dir);
        }
        
        
        return true;
    }

    // checks if coordinates x and y are in bounds 
    static boolean inBounds(int x, int y) {
        return x >= 0 && x <= 8 && y >= 0 && y <= 8;
    }

    static boolean isSetRightSize(List<AbaloneCoord> set) {
        return set.size() < 4;
    }
    
    static List<AbaloneCoord> getOriginCoords(int x1, int y1, int x2, int y2) {
        List<AbaloneCoord> result = new ArrayList<AbaloneCoord>();
        int dx = (x1 - x2);
        int dy = (y1 - y2);
        if (dx != 0 && dy != 0 && dy != dx) {
            return null;
        }
        int ddx = getIncrement(dx);
        int ddy = getIncrement(dy);
        for (int i = 0; i <= dx; i++) {
            AbaloneCoord newCoord = new AbaloneCoord(x1 + ddx * i, y1 + ddy * i);
            result.add(newCoord);
        }
        return result;
    }
    
    // returns the sign of x. used to determine which direction to incrementally
    // search when searching between coordinates for pieces
    static int getIncrement(int x) {
        if (x > 0) {
            return -1;
        } else if (x == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    static List<AbaloneCoord> getDestCoords(List<AbaloneCoord> origin, Abalone.Dir dir) {
        List<AbaloneCoord> result = new ArrayList<AbaloneCoord>();
        for (AbaloneCoord coord : origin) {
            result.add(new AbaloneCoord(coord.x + dir.dx, coord.y + dir.dy));
        }
        return result;
    }
    
    static boolean isAllContained(List<AbaloneCoord> set, List<AbaloneCoord> playerPieces) {
        for (AbaloneCoord coord : set) {
            if (!playerPieces.contains(coord)) {
                return false;
            }
        }
        return true;
    }
    
    // purposely does not check if destination is out bounds
    static boolean isDestUnoccupied(List<AbaloneCoord> origin, List<AbaloneCoord> dest,
            List<AbaloneCoord> playerPieces) {
        for (AbaloneCoord coord : dest) {
            if (!origin.contains(coord) && playerPieces.contains(coord)) {
                return false;
            }
        }
        return true;
    }
    
    // determines if a move of pieces between x1,y1 and x2,y2 moving in dir direction
    // is an inline move or not
    static boolean isInlineMove(int x1, int y1, int x2, int y2, Abalone.Dir dir) {
        if ((x1 == x2) && (y1 != y2)) {
             if (dir == Abalone.Dir.UL || dir == Abalone.Dir.DR) {
                 return true;
             }
        }
        if ((x1 != x2) && (y1 == y2)) {
            if (dir == Abalone.Dir.L || dir == Abalone.Dir.R) {
                return true;
            }
        }
        if ((x1 != x2) && (y1 != y2)) {
            if (dir == Abalone.Dir.UR || dir == Abalone.Dir.DL) {
                return true;
            }
        }
        return false;
    }
    
    // PRECONDITION: move is an inline move
    static List<AbaloneCoord> getSpacesPushed(List<AbaloneCoord> origin, Abalone.Dir dir) {
        List<AbaloneCoord> pushedSpaces = new ArrayList<AbaloneCoord>();
        AbaloneCoord headCoord = getHeadOfInline(origin, dir);
        for (int i = 1; i <= 3; i++) {
            pushedSpaces.add(new AbaloneCoord(headCoord.x + dir.dx * i, headCoord.y + dir.dy * i));
        }
        return pushedSpaces;
    }
    
    static AbaloneCoord getHeadOfInline(List<AbaloneCoord> origin, Abalone.Dir dir) {
        AbaloneCoord headCoord = origin.get(0);
        if (dir == Abalone.Dir.DL || dir == Abalone.Dir.UL || dir == Abalone.Dir.L) {
            for (AbaloneCoord coord : origin) {
                if (coord.x < headCoord.x) {
                    headCoord = coord;
                }
            }
        } else {
            for (AbaloneCoord coord : origin) {
                if (coord.x > headCoord.x) {
                    headCoord = coord;
                }
            }
        }
        return headCoord;
    }
    
    static boolean canPush(List<AbaloneCoord> origin, List<AbaloneCoord> pushed,
            List<AbaloneCoord> friendlyPieces, List<AbaloneCoord> enemyPieces) {
        boolean result = false;
        
        
        return result;
    }
    
}
