package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startingPosition) {
         Collection<ChessMove> moves = new ArrayList<>();
         int startRow = startingPosition.getRow();
         int startCol = startingPosition.getColumn();

         moves.addAll(calcMovesHelper(startRow, startCol, 1, 1));
         moves.addAll(calcMovesHelper(startRow, startCol, 1, -1));
         moves.addAll(calcMovesHelper(startRow, startCol, -1, 1));
         moves.addAll(calcMovesHelper(startRow, startCol, -1, -1));

         return moves;
    }

    private Collection<ChessMove> calcMovesHelper(int startRow, int startCol, int rowDirection, int colDirection) {
        Collection<ChessMove> movesByDirection = new ArrayList<>();
        int currentRow = startRow;
        int currentCol = startCol;

        boolean isOnBoard = true;

        while (isOnBoard) {
            currentRow += rowDirection;
            currentCol += colDirection;
            movesByDirection.add(new ChessMove(startingPosition, new ChessPosition(currentRow, currentCol), null));
            isOnBoard = (currentRow >= 1) && (currentRow <= 8) && (currentCol >= 1 ) && (currentCol <= 8);
        }
        return movesByDirection;
    }
}
