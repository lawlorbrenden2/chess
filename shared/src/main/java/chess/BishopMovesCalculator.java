package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(calcMovesHelper(board, startPosition, 1, 1));
        moves.addAll(calcMovesHelper(board, startPosition, 1, -1));
        moves.addAll(calcMovesHelper(board, startPosition, -1, 1));
        moves.addAll(calcMovesHelper(board, startPosition, -1, -1));

        return moves;
    }

    private Collection<ChessMove> calcMovesHelper(
            ChessBoard board,
            ChessPosition startingPosition,
            int rowDirection,
            int colDirection) {

        Collection<ChessMove> movesByDirection = new ArrayList<>();
        ChessPiece movingPiece = board.getPiece(startingPosition);
        ChessGame.TeamColor teamColor = movingPiece.getTeamColor();

        while (true) {
            int currentRow = startingPosition.getRow() + rowDirection;
            int currentCol = startingPosition.getColumn() + colDirection;

            if (!isOnBoard(currentRow, currentCol)) {
                break;
            }

            ChessPosition currentPosition = new ChessPosition(currentRow, currentCol);
            ChessPiece pieceAtPosition = board.getPiece((currentPosition));

            if (pieceAtPosition == null) {
                movesByDirection.add(new ChessMove(startingPosition, currentPosition, null));
            } else if (pieceAtPosition.getTeamColor() != teamColor) {
                movesByDirection.add(new ChessMove(startingPosition, currentPosition, null));
                break;
            } else {
                break;
            }
        }
        return movesByDirection;
    }

    private boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
