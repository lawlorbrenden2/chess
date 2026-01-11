package chess;

import java.util.ArrayList;
import java.util.Collection;

public class SlidingMovesCalculator {

    /**
     * Calculates all the possible sliding moves for either a bishop, queen, or rook
     *
     * @param board Chess board with all the pieces on it
     * @param startPosition Starting position of piece
     * @param rowDirection Row moving direction
     * @param colDirection Column moving direction
     * @return Array of possible moves
     */
    public static Collection<ChessMove> slidingMovesHelper(
            ChessBoard board,
            ChessPosition startPosition,
            int rowDirection,
            int colDirection) {

        Collection<ChessMove> movesByDirection = new ArrayList<>();
        ChessPiece movingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = movingPiece.getTeamColor();

        int targetRow = startPosition.getRow();
        int targetCol = startPosition.getColumn();

        while (true) {
            targetRow += rowDirection;
            targetCol += colDirection;

            if (!isOnBoard(targetRow, targetCol)) {
                break;
            }

            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece pieceAtTarget = board.getPiece(targetPosition);

            if (pieceAtTarget == null) {
                movesByDirection.add(new ChessMove(startPosition, targetPosition, null));
            } else if (pieceAtTarget.getTeamColor() == teamColor) {
                break;
            } else {
                movesByDirection.add(new ChessMove(startPosition, targetPosition, null));
                break;
            }
        }
        return movesByDirection;
    }

        /**
         * Determines if a specific square is within the bounds of a chess board
         *
         * @param row the row of the board
         * @param col the column of the board
         * @return bool value of if the square is within the bounds of a chess board
         */
        private static boolean isOnBoard(int row, int col) {
            return row >= 1 && row <= 8 && col >= 1 && col <= 8;
        }
    }
