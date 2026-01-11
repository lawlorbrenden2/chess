package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a bishop from its current position.
     *
     * @param board         the chess board containing all the pieces
     * @param startPosition the current position of the bishop
     * @return a collection of ChessMove objects representing all legal moves for this bishop
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, 1, 1));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, 1, -1));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, -1, 1));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, -1, -1));

        return moves;
    }
}
