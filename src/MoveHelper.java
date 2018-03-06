import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/*
 * 1. Generates legal moves based on groups of pieces provided.
 * 2. Checks if a move is legal
 */
public final class MoveHelper {

    public static List<AbaloneMove> generateAllMoves(final List<List<AbaloneCoord>> legalGroups,
                                                     final Set<AbaloneCoord> playerOnePieces,
                                                     final Set<AbaloneCoord> playerTwoPieces) {
        List<AbaloneMove> legalMoves = new ArrayList<>();
        // evaluate group-by-group
        for (List<AbaloneCoord> group : legalGroups) {
            List<AbaloneMove> groupMoves =  generateAllMovesForThisGroup(group, playerOnePieces, playerTwoPieces);
            legalMoves.addAll(groupMoves);
        }

        return legalMoves;
    }

    private static List<AbaloneMove> generateAllMovesForThisGroup(final List<AbaloneCoord> group,
                                                                  final Set<AbaloneCoord> playerOnePieces,
                                                                  final Set<AbaloneCoord> playerTwoPieces) {
        // must check moves for 6 directions

        // find which directions are in-line
        // for 1 piece group, all directions are broadside
        // for 2,3 piece groups, 2 inline and 4 broadside direcions.
        List<AbaloneMove> legalMoves = new ArrayList<>();
        List<Abalone.Dir> broadsideDirections = new ArrayList<>();
        List<Abalone.Dir> inlineDirections = new ArrayList<>();

        if (group.size() == 1) {
            // All broadside moves
            broadsideDirections.add(Abalone.Dir.UL);
            broadsideDirections.add(Abalone.Dir.L);
            broadsideDirections.add(Abalone.Dir.DL);
            broadsideDirections.add(Abalone.Dir.UR);
            broadsideDirections.add(Abalone.Dir.R);
            broadsideDirections.add(Abalone.Dir.DR);

            List<AbaloneMove> broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            legalMoves.addAll(broadsideMoves);
        } else if (group.size() == 2) {
            // 2 inline, 4 broadside
            final AbaloneCoord alpha = group.get(0);
            final AbaloneCoord beta = group.get(1);
            final int dx = Math.abs(alpha.x - beta.x);
            final int dy = Math.abs(alpha.y - beta.y);

            AbaloneCoord frontPieceMinimum = group.get(0);
            AbaloneCoord frontPieceMaximum = group.get(0);

            List<AbaloneMove> broadsideMoves = new ArrayList<>();
            List<AbaloneMove> inlineMoves = new ArrayList<>();

            // No change in X, change in Y
            if (dx == 0 && dy != 0) {
                for (int i = 0; i < 2; i++) {
                    if (group.get(i).y > frontPieceMaximum.y) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).y < frontPieceMinimum.y) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(2, frontPieceMaximum, group, Abalone.Dir.UL, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(2, frontPieceMinimum, group, Abalone.Dir.DR, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UR);
                broadsideDirections.add(Abalone.Dir.L);
                broadsideDirections.add(Abalone.Dir.R);
                broadsideDirections.add(Abalone.Dir.DL);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }

            // No change in Y, change in X
            else if (dx != 0 && dy == 0) {
                for (int i = 0; i < 2; i++) {
                    if (group.get(i).x > frontPieceMaximum.x) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).x < frontPieceMinimum.x) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(2, frontPieceMaximum, group, Abalone.Dir.R, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(2, frontPieceMinimum, group, Abalone.Dir.L, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UL);
                broadsideDirections.add(Abalone.Dir.UR);
                broadsideDirections.add(Abalone.Dir.DL);
                broadsideDirections.add(Abalone.Dir.DR);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }

            // Change in X, change in Y
            else if (dx != 0 && dy != 0) {
                for (int i = 0; i < 2; i++) {
                    if (group.get(i).x > frontPieceMaximum.x && group.get(i).y > frontPieceMaximum.y) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).x < frontPieceMinimum.x && group.get(i).y < frontPieceMinimum.y) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(2, frontPieceMaximum, group, Abalone.Dir.UR, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(2, frontPieceMinimum, group, Abalone.Dir.DL, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UL);
                broadsideDirections.add(Abalone.Dir.L);
                broadsideDirections.add(Abalone.Dir.R);
                broadsideDirections.add(Abalone.Dir.DR);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }
            legalMoves.addAll(inlineMoves);
            legalMoves.addAll(broadsideMoves);
        } else if (group.size() == 3) {
            // 2 inline, 4 broadside
            final AbaloneCoord alpha = group.get(0);
            final AbaloneCoord beta = group.get(1);
            final int dx = Math.abs(alpha.x - beta.x);
            final int dy = Math.abs(alpha.y - beta.y);

            AbaloneCoord frontPieceMinimum = group.get(0);
            AbaloneCoord frontPieceMaximum = group.get(0);

            List<AbaloneMove> broadsideMoves = new ArrayList<>();
            List<AbaloneMove> inlineMoves = new ArrayList<>();

            // No change in X, change in Y
            if (dx == 0 && dy != 0) {
                for (int i = 0; i < 3; i++) {
                    if (group.get(i).y > frontPieceMaximum.y) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).y < frontPieceMinimum.y) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(3, frontPieceMaximum, group, Abalone.Dir.UL, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(3, frontPieceMinimum, group, Abalone.Dir.DR, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UR);
                broadsideDirections.add(Abalone.Dir.L);
                broadsideDirections.add(Abalone.Dir.R);
                broadsideDirections.add(Abalone.Dir.DL);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }

            // No change in Y, change in X
            else if (dx != 0 && dy == 0) {
                for (int i = 0; i < 3; i++) {
                    if (group.get(i).x > frontPieceMaximum.x) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).x < frontPieceMinimum.x) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(3, frontPieceMaximum, group, Abalone.Dir.R, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(3, frontPieceMinimum, group, Abalone.Dir.L, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UL);
                broadsideDirections.add(Abalone.Dir.UR);
                broadsideDirections.add(Abalone.Dir.DL);
                broadsideDirections.add(Abalone.Dir.DR);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }

            // Change in X, change in Y
            else if (dx != 0 && dy != 0) {
                for (int i = 0; i < 3; i++) {
                    if (group.get(i).x > frontPieceMaximum.x && group.get(i).y > frontPieceMaximum.y) {
                        frontPieceMaximum = group.get(i);
                    }
                    if (group.get(i).x < frontPieceMinimum.x && group.get(i).y < frontPieceMinimum.y) {
                        frontPieceMinimum = group.get(i);
                    }
                }
                inlineMoves.addAll(generateInlineMove(3, frontPieceMaximum, group, Abalone.Dir.UR, playerOnePieces, playerTwoPieces));
                inlineMoves.addAll(generateInlineMove(3, frontPieceMinimum, group, Abalone.Dir.DL, playerOnePieces, playerTwoPieces));

                broadsideDirections.add(Abalone.Dir.UL);
                broadsideDirections.add(Abalone.Dir.L);
                broadsideDirections.add(Abalone.Dir.R);
                broadsideDirections.add(Abalone.Dir.DR);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
            }
            legalMoves.addAll(inlineMoves);
            legalMoves.addAll(broadsideMoves);
        }

        return legalMoves;
    }

    private static List<AbaloneMove> generateBroadsideMoves(final List<Abalone.Dir> directions,
                                                            final List<AbaloneCoord> group,
                                                            final Set<AbaloneCoord> playerOnePieces,
                                                            final Set<AbaloneCoord> playerTwoPieces) {
        List<AbaloneMove> moves = new ArrayList<AbaloneMove>();
        for (Abalone.Dir direction : directions) {
            boolean isValidMove = true;
            for (AbaloneCoord piece : group) {
                AbaloneCoord destination = new AbaloneCoord(piece.x + direction.dx, piece.y + direction.dy);
                if (playerOnePieces.contains(destination) || playerTwoPieces.contains(destination)) {
                    isValidMove = false;
                    break;
                }
            }

            if (isValidMove) {
                moves.add(new AbaloneMove(group, null, direction, false, 0));
            }
        }

        return moves;
    }

    private static List<AbaloneMove> generateInlineMove(final int size,
                                                        final AbaloneCoord frontPiece,
                                                        final List<AbaloneCoord> group,
                                                        final Abalone.Dir direction,
                                                        final Set<AbaloneCoord> playerOnePieces,
                                                        final Set<AbaloneCoord> playerTwoPieces) {
        List<AbaloneMove> moves = new ArrayList<>();
        final boolean playerOneMove = playerOnePieces.contains(frontPiece);
        final AbaloneCoord destination = new AbaloneCoord(frontPiece.x, frontPiece.y);

        boolean isValidMove = false;
        int numPushedPieces = 0;
        for (int i = 0; i < size; i++) {
            destination.x += direction.dx;
            destination.y += direction.dy;

            if (!playerOnePieces.contains(destination) && !playerTwoPieces.contains(destination)) {
                isValidMove = true;
                break;
            }

            // ally piece cannot be moved
            if (playerOneMove) {
                if (playerOnePieces.contains(destination)) {
                    break;
                }
            } else {
                if (playerTwoPieces.contains(destination)) {
                    break;
                }
            }
            numPushedPieces++;
        }

        if (isValidMove) {
            List<AbaloneCoord> pushedPieces = new ArrayList<>();
            for (int i = 1; i <= numPushedPieces; i++) {
                pushedPieces.add(new AbaloneCoord(frontPiece.x + i * direction.dx, frontPiece.y + i * direction.dy));
            }

            moves.add(new AbaloneMove(group, pushedPieces, direction, true, numPushedPieces));
        }

        return moves;
    }
}
