package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a pawn from its current position.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the pawn
     * @return a collection of ChessMove objects representing all legal moves for this pawn
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {

        return new ArrayList<>(calcMovesHelper(board, startPosition, 1, 0));
    }

    /**
     * Calculates all possible moves in a single direction for a pawn.
     *
     * @param board the chess board containing all pieces
     * @param startingPosition the current position of the pawn
     * @param rowDirection +1 for down, -1 for up
     * @param colDirection +1 for right, -1 for left
     * @return a collection of ChessMove objects representing legal moves in this direction
     */
    private Collection<ChessMove> calcMovesHelper(
            ChessBoard board,
            ChessPosition startingPosition,
            int rowDirection,
            int colDirection) {

        Collection<ChessMove> movesByDirection = new ArrayList<>();
        ChessPiece movingPiece = board.getPiece(startingPosition);
        ChessGame.TeamColor teamColor = movingPiece.getTeamColor();

        int targetRow = startingPosition.getRow();
        int targetCol = startingPosition.getColumn();

        while (true) {
            targetRow += rowDirection;
            targetCol += colDirection;

            if (!isOnBoard(targetRow, targetCol)) {
                break;
            }

            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece pieceAtPosition = board.getPiece((targetPosition));

            if (pieceAtPosition == null) {
                movesByDirection.add(new ChessMove(startingPosition, targetPosition, null));
            } else if (pieceAtPosition.getTeamColor() == teamColor) {
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
    private boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

}
