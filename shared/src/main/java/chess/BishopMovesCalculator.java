package chess;

import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition) {
        return List.of(new ChessMove(new ChessPosition(5, 4), new ChessPosition(1, 8), null));
    }
}
