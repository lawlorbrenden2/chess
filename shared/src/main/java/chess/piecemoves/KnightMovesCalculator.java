package chess.piecemoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

class KnightMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a knight from its current position.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the knight
     * @return a collection of ChessMove objects representing all legal moves for this knight
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(calcMovesHelper(board, startPosition, 2, 1));
        moves.addAll(calcMovesHelper(board, startPosition, 2, -1));
        moves.addAll(calcMovesHelper(board, startPosition, -2, 1));
        moves.addAll(calcMovesHelper(board, startPosition, -2, -1));
        moves.addAll(calcMovesHelper(board, startPosition, 1, 2));
        moves.addAll(calcMovesHelper(board, startPosition, 1, -2));
        moves.addAll(calcMovesHelper(board, startPosition, -1, 2));
        moves.addAll(calcMovesHelper(board, startPosition, -1, -2));

        return moves;
    }

    /**
     * Calculates all possible moves in a single direction for a knight.
     *
     * @param board the chess board containing all pieces
     * @param startPosition the current position of the knight
     * @param rowDirection +1 for down, -1 for up
     * @param colDirection +1 for right, -1 for left
     * @return a collection of ChessMove objects representing legal moves in this direction
     */
    private Collection<ChessMove> calcMovesHelper(
            ChessBoard board,
            ChessPosition startPosition,
            int rowDirection,
            int colDirection) {

        Collection<ChessMove> movesByDirection = new ArrayList<>();
        ChessPiece movingPiece = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = movingPiece.getTeamColor();

        int targetRow = startPosition.getRow();
        int targetCol = startPosition.getColumn();

            targetRow += rowDirection;
            targetCol += colDirection;

            if (!isOnBoard(targetRow, targetCol)) {
                return movesByDirection;
            }

            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece pieceAtTarget = board.getPiece(targetPosition);

            if (pieceAtTarget == null || pieceAtTarget.getTeamColor() != teamColor) {
                movesByDirection.add(new ChessMove(startPosition, targetPosition, null));
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
