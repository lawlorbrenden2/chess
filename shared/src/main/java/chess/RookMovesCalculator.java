package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a rook from its current position.
     *
     * @param board         the chess board containing all the pieces
     * @param startPosition the current position of the rook
     * @return a collection of ChessMove objects representing all legal moves for this rook
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, 1, 0));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, -1, 0));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, 0, 1));
        moves.addAll(SlidingMovesCalculator.slidingMovesHelper(board, startPosition, 0, -1));

        return moves;
    }
}


