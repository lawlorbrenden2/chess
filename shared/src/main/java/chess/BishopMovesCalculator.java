package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        ChessPiece movingPiece = board.getPiece(startingPosition);
        ChessGame.TeamColor teamColor = movingPiece.getTeamColor();
        Collection<ChessMove> movesByDirection = new ArrayList<>();
        int startRow = startingPosition.getRow();
        int startCol = startingPosition.getColumn();
        int currentRow = startRow;
        int currentCol = startCol;

        while (true) {
            currentRow += rowDirection;
            currentCol += colDirection;

            if (!isOnBoard(currentRow, currentCol)) {
                break;
            }

            ChessPosition currentPosition = new ChessPosition(currentRow, currentCol);
            ChessPiece pieceAtPosition = board.getPiece((currentPosition));

            if (pieceAtPosition == null) {
                movesByDirection.add(new ChessMove(startingPosition, currentPosition, null));
            } else if (pieceAtPosition.getTeamColor() == teamColor) {
                break;
            } else {
                movesByDirection.add(new ChessMove(startingPosition, currentPosition, null));
                break;
            }
        }
        return movesByDirection;
    }

    private boolean isOnBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }
}
