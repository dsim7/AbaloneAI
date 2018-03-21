package abalone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import abalone.Abalone.Dir;

/*
 * 1. Generates legal moves based on groups of pieces provided.
 * 2. Checks if a move is legal
 */
public final class MoveHelper {

    public static List<AbaloneMove> generateAllMoves(final Set<List<AbaloneCoord>> legalGroups,
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
        
        for (Abalone.Dir direction : Abalone.Dir.values()) {
            AbaloneMove move = generateMove(group, direction, playerOnePieces, playerTwoPieces);
            if (move != null) {
                legalMoves.add(move);
            }
               
        }
        /*
        List<Abalone.Dir> broadsideDirections = new ArrayList<>();
        List<Abalone.Dir> inlineDirections = new ArrayList<>(); 
        
        if (group.size() == 1) {
            // All broadside moves
            List<AbaloneMove> broadsideMoves = generateBroadsideMoves(Arrays.asList(Dir.values()),
                    group, playerOnePieces, playerTwoPieces);

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.UL, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.DR, playerOnePieces, playerTwoPieces));

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.R, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.L, playerOnePieces, playerTwoPieces));

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.UR, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.DL, playerOnePieces, playerTwoPieces));

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.UL, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.DR, playerOnePieces, playerTwoPieces));

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.R, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.L, playerOnePieces, playerTwoPieces));

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
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.UR, playerOnePieces, playerTwoPieces));
                inlineMoves.add(generateInlineMove(group, Abalone.Dir.DL, playerOnePieces, playerTwoPieces));
                /*
                broadsideDirections.add(Abalone.Dir.UL);
                broadsideDirections.add(Abalone.Dir.L);
                broadsideDirections.add(Abalone.Dir.R);
                broadsideDirections.add(Abalone.Dir.DR);
                broadsideMoves = generateBroadsideMoves(broadsideDirections, group, playerOnePieces, playerTwoPieces);
                
                broadsideMoves.add(generateBroadsideMove(group, Abalone.Dir.UL, playerOnePieces, playerTwoPieces));
                broadsideMoves.add(generateBroadsideMove(group, Abalone.Dir.L, playerOnePieces, playerTwoPieces));
                broadsideMoves.add(generateBroadsideMove(group, Abalone.Dir.R, playerOnePieces, playerTwoPieces));
                broadsideMoves.add(generateBroadsideMove(group, Abalone.Dir.DR, playerOnePieces, playerTwoPieces));
            }
            legalMoves.addAll(inlineMoves);
            legalMoves.addAll(broadsideMoves);
        }*/

        return legalMoves;
    }
    
    private static AbaloneCoord findFrontPiece(List<AbaloneCoord> group, Abalone.Dir direction) {
        Collections.sort(group);
        if (direction == Dir.UL || direction == Dir.UR || direction == Dir.R) {
            return group.get(group.size() - 1);
        } else {
            return group.get(0);
        }
    }
    
    private static boolean isInlineMove(List<AbaloneCoord> group, Abalone.Dir direction) {
        if (group.size() == 1) {
            return false;
        } else {
            final AbaloneCoord alpha = group.get(0);
            final AbaloneCoord beta = group.get(group.size() - 1);
            final int dx = Math.abs(alpha.x - beta.x);
            final int dy = Math.abs(alpha.y - beta.y);
            
            if (((dx == 0 && dy != 0) && (direction == Dir.UL || direction == Dir.DR)) ||
                    ((dx != 0 && dy != 0) && (direction == Dir.UR || direction == Dir.DL)) ||
                    ((dx != 0 && dy == 0) && (direction == Dir.L || direction == Dir.R))) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    public static AbaloneMove generateMove(List<AbaloneCoord> group,
                                            Abalone.Dir direction,
                                            Set<AbaloneCoord> playerOnePieces,
                                            Set<AbaloneCoord> playerTwoPieces) {
        if (direction == null) {
            return null;
        }
        
        if (isInlineMove(group, direction)) {
            return generateInlineMove(group, direction, playerOnePieces, playerTwoPieces);
        } else {
            return generateBroadsideMove(group, direction, playerOnePieces, playerTwoPieces);
        }
    }
    /*
    private static List<AbaloneMove> generateBroadsideMoves(final List<Abalone.Dir> directions,
                                                            final List<AbaloneCoord> group,
                                                            final Set<AbaloneCoord> playerOnePieces,
                                                            final Set<AbaloneCoord> playerTwoPieces) {
        List<AbaloneMove> moves = new ArrayList<AbaloneMove>();
        for (Abalone.Dir direction : directions) {
            moves.add(generateBroadsideMove(group, direction, playerOnePieces, playerTwoPieces));
        }

        return moves;
    }
    */
    private static AbaloneMove generateBroadsideMove(final List<AbaloneCoord> group,
                                                     final Abalone.Dir direction,
                                                     final Set<AbaloneCoord> playerOnePieces,
                                                     final Set<AbaloneCoord> playerTwoPieces) {        
        boolean isValidMove = true;
        for (AbaloneCoord piece : group) {
            AbaloneCoord destination = new AbaloneCoord(piece.x + direction.dx, piece.y + direction.dy);
            if (!destination.isValid() || playerOnePieces.contains(destination) || playerTwoPieces.contains(destination)) {
                isValidMove = false;
                break;
            }
        }

        if (isValidMove) {
            return new AbaloneMove(group, null, direction, false/*, 0*/);
        }
        return null;
    }

    private static AbaloneMove generateInlineMove(final List<AbaloneCoord> group,
                                                  final Abalone.Dir direction,
                                                  final Set<AbaloneCoord> playerOnePieces,
                                                  final Set<AbaloneCoord> playerTwoPieces) {
        final AbaloneCoord frontPiece = findFrontPiece(group, direction);
        final boolean playerOneMove = playerOnePieces.contains(frontPiece);
        final AbaloneCoord destination = new AbaloneCoord(frontPiece.x, frontPiece.y);

        boolean isValidMove = false;
        int numPushedPieces = 0;
        for (int i = 0; i < group.size(); i++) {
            destination.x += direction.dx;
            destination.y += direction.dy;
            
            if (i == 0 && !destination.isValid()) {
                break;
            }

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
            return new AbaloneMove(group, pushedPieces, direction, true/*, numPushedPieces*/);

        }
        return null;
    }
}
