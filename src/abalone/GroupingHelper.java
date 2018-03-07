package abalone;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates groups based on the player's pieces on the board.
 */
public final class GroupingHelper {

    /*
     * Given coordinates of pieces, generate groups.
     * @return List<List<AbaloneCoord>>
     */
    public static List<List<AbaloneCoord>> generateGroups(final Set<AbaloneCoord> playerPieces) {
        AbaloneCoord[] playerPiecesArr = playerPieces.toArray(new AbaloneCoord[playerPieces.size()]);
        
        final List<List<AbaloneCoord>> groups = new ArrayList<List<AbaloneCoord>>();
        final Set<List<AbaloneCoord>> uniqueGroups = new HashSet<List<AbaloneCoord>>();
        
        
        for (int i = 0; i < playerPiecesArr.length; i++) {
            for (int j = i; j < playerPiecesArr.length; j++) {
                if (validateGrouping(playerPiecesArr[i], playerPiecesArr[j], playerPieces)) {
                    ArrayList<AbaloneCoord> groupCoordinates = generateCoordinates(playerPiecesArr[i], playerPiecesArr[j]);
                    uniqueGroups.add(groupCoordinates);
                }
            }
        }

        for (List<AbaloneCoord> group : uniqueGroups) {
            groups.add(group);
        }

        return groups;
    }

    /**
     * Given two coordinates that form a group, generates a list of coordinates that represents the group.
     * @return List<AbaloneCoord>
     */
    private static ArrayList<AbaloneCoord> generateCoordinates(final AbaloneCoord alpha,
                                                          final AbaloneCoord beta) {
        ArrayList<AbaloneCoord> coordinates = new ArrayList<>();

        /**
         * Both X and Y are the same. In this case, we are looking at a single coordinate.
         */
        if (alpha.x == beta.x && alpha.y == beta.y) {
            coordinates.add(alpha);
        }

        /**
         * X is the same, Y is different.
         */
        else if (alpha.x == beta.x && alpha.y != beta.y) {
            final boolean betaHasHigherValue = beta.y > alpha.y;
            final int yDifference = Math.abs(alpha.y - beta.y);

            if (betaHasHigherValue) {
                if (yDifference == 1) {
                    coordinates.add(alpha);
                    coordinates.add(beta);
                } else if (yDifference == 2) {
                    coordinates.add(alpha);
                    coordinates.add(new AbaloneCoord(alpha.x, alpha.y + 1));
                    coordinates.add(beta);
                }
            } else {
                if (yDifference == 1) {
                    coordinates.add(beta);
                    coordinates.add(alpha);
                } else if (yDifference == 2) {
                    coordinates.add(beta);
                    coordinates.add(new AbaloneCoord(beta.x, beta.y + 1));
                    coordinates.add(alpha);
                }
            }
        }

        /**
         * Y is the same, X is different.
         */
        else if (alpha.y == beta.y && alpha.x != beta.x) {
            final boolean betaHasHigherValue = beta.x > alpha.x;
            final int xDifference = Math.abs(alpha.x - beta.x);

            if (betaHasHigherValue) {
                if (xDifference == 1) {
                    coordinates.add(alpha);
                    coordinates.add(beta);
                } else if (xDifference == 2) {
                    coordinates.add(alpha);
                    coordinates.add(new AbaloneCoord(alpha.x + 1, alpha.y));
                    coordinates.add(beta);
                }
            } else {
                if (xDifference == 1) {
                    coordinates.add(beta);
                    coordinates.add(alpha);
                } else if (xDifference == 2) {
                    coordinates.add(beta);
                    coordinates.add(new AbaloneCoord(beta.x + 1, beta.y));
                    coordinates.add(alpha);
                }
            }
        }

        /**
         * Both X and Y are different.
         */
        else if (alpha.x != beta.x && alpha.y != beta.y) {
            final boolean betaHasHigherValue = beta.y > alpha.y;
            final int difference = Math.abs(alpha.x - beta.x);

            if (betaHasHigherValue) {
                if (difference == 1) {
                    coordinates.add(alpha);
                    coordinates.add(beta);
                } else if (difference == 2) {
                    coordinates.add(alpha);
                    coordinates.add(new AbaloneCoord(alpha.x + 1, alpha.y + 1));
                    coordinates.add(beta);
                }
            } else {
                if (difference == 1) {
                    coordinates.add(beta);
                    coordinates.add(alpha);
                } else if (difference == 2) {
                    coordinates.add(beta);
                    coordinates.add(new AbaloneCoord(beta.x + 1, beta.y + 1));
                    coordinates.add(alpha);
                }
            }

        }

        return coordinates;
    }

    private static boolean validateGrouping(final AbaloneCoord alpha,
                                            final AbaloneCoord beta,
                                            final Set<AbaloneCoord> playerPieces) {

        /**
         * Both X and Y are the same. In this case, we are looking at a single coordinate.
         */
        if (alpha.x == beta.x && alpha.y == beta.y) {
            return true;
        }

        /**
         * X is the same, Y is different.
         */
        else if (alpha.x == beta.x && alpha.y != beta.y) {
            final boolean betaHasHigherValue = beta.y > alpha.y;
            final int yDifference = Math.abs(alpha.y - beta.y);

            // y coordinate should differ by 2 or less for valid grouping
            if (yDifference <= 2) {
                // check if middle coordinate is owned by this player when y coordinate is different by 2.
                if (yDifference == 2) {
                    final AbaloneCoord middleCoordinate = new AbaloneCoord(alpha.x,
                            betaHasHigherValue ? alpha.y + 1 : beta.y + 1);
                    for (AbaloneCoord pieceCoordinates : playerPieces) {
                        if (middleCoordinate.equals(pieceCoordinates)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }

        /**
         * Y is the same, X is different.
         */
        else if (alpha.y == beta.y && alpha.x != beta.x) {
            final boolean betaHasHigherValue = beta.x > alpha.x;
            final int xDifference = Math.abs(alpha.x - beta.x);

            // x coordinate should differ by 2 or less for valid grouping
            if (xDifference <= 2) {
                // check if middle coordinate is owned by this player when x coordinate is different by 2.
                if (xDifference == 2) {
                    final AbaloneCoord middleCoordinate = new AbaloneCoord(betaHasHigherValue ? alpha.x + 1 : beta.x + 1,
                            alpha.y);
                    for (AbaloneCoord pieceCoordinates : playerPieces) {
                        if (middleCoordinate.equals(pieceCoordinates)) {
                            return true;
                        }
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }

        /**
         * Both X and Y are different.
         */
        else if (alpha.x != beta.x && alpha.y != beta.y) {

            // beta has higher x and y coordinate value.
            if (alpha.x < beta.x && alpha.y < beta.y) {
                if (beta.x - alpha.x == beta.y - alpha.y) {
                    if (Math.abs(beta.x - alpha.x) <= 2) {
                        return true;
                    }
                }
            }

            // alpha has higher x and y coordinate value.
            else if (alpha.x > beta.x && alpha.y > beta.y) {
                if (alpha.x - beta.x == alpha.y - beta.y) {
                    if (Math.abs(beta.x - alpha.x) <= 2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /* Do not instantiate this. */
    private GroupingHelper() {}
}
