package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a king from its current position.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the king
     * @return a collection of ChessMove objects representing all legal moves for this king
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(calcMovesHelper(board, startPosition, 1, 1));
        moves.addAll(calcMovesHelper(board, startPosition, 1, -1));
        moves.addAll(calcMovesHelper(board, startPosition, -1, 1));
        moves.addAll(calcMovesHelper(board, startPosition, -1, -1));
        moves.addAll(calcMovesHelper(board, startPosition, 1, 0));
        moves.addAll(calcMovesHelper(board, startPosition, 0, 1));
        moves.addAll(calcMovesHelper(board, startPosition, -1, 0));
        moves.addAll(calcMovesHelper(board, startPosition, 0, -1));

        return moves;
    }

    /**
     * Calculates all possible moves in a single direction for a king.
     *
     * @param board the chess board containing all pieces
     * @param startingPosition the current position of the king
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
        int targetRow = startingPosition.getRow() + rowDirection;
        int targetCol = startingPosition.getColumn() + colDirection;

        if (!isOnBoard(targetRow, targetCol)) {
            return movesByDirection;
        }

        ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
        ChessPiece pieceAtPosition = board.getPiece((targetPosition));

        if (pieceAtPosition == null || pieceAtPosition.getTeamColor() != teamColor) {
            movesByDirection.add(new ChessMove(startingPosition, targetPosition, null));
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
