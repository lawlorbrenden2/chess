package model.chess.piecemoves;

import model.chess.ChessBoard;
import model.chess.ChessMove;
import model.chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition myPosition);
}
