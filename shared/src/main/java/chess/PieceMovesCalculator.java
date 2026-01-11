package chess;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition);
}
