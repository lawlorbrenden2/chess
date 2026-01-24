package chess.piecemoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;

class PawnMovesCalculator implements PieceMovesCalculator {

    /**
     * Calculates all possible moves for a pawn from its current position.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the pawn
     * @return a collection of ChessMove objects representing all legal moves for this pawn
     */
    @Override
    public Collection<ChessMove> calcMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece pawn = board.getPiece(startPosition);
        ChessGame.TeamColor teamColor = pawn.getTeamColor();

        int forward = (teamColor == WHITE) ? 1 : -1;

        addForwardMove(board, startPosition, forward, moves);
        addDoubleMove(board, startPosition, forward, moves);
        addDiagonalCaptures(board, startPosition, forward, moves, teamColor);

        return moves;
    }

    /**
     * Adds a single forward move for a pawn.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the pawn
     * @param forward the direction of the pawn
     * @param moves array of possible moves
     */
    private void addForwardMove(
            ChessBoard board,
            ChessPosition startPosition,
            int forward,
            Collection<ChessMove> moves) {

        int targetRow = startPosition.getRow() + forward;
        int targetCol = startPosition.getColumn();

        ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
        ChessPiece pieceAtTarget = board.getPiece((targetPosition));

        if (pieceAtTarget == null) {
            if (isPromotionRow(targetRow, forward)) {
                addPromotionMoves(startPosition, targetPosition, moves);
            } else {
                moves.add(new ChessMove(startPosition, targetPosition, null));
            }
        }
    }

    /**
     * Adds a double move for a pawn.
     *
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the pawn
     * @param forward the direction of the pawn
     * @param moves array of possible pawn moves
     */
    private void addDoubleMove(
            ChessBoard board,
            ChessPosition startPosition,
            int forward,
            Collection<ChessMove> moves) {

        // only allow double move from starting row
        int startRow = startPosition.getRow();
        if ((startRow != 2 && forward == 1) ||
            (startRow != 7 && forward == -1)) {
            return;
        }

        int middleRow = startRow + forward;
        int targetRow = startRow + 2 * forward;
        int targetCol = startPosition.getColumn();

        ChessPosition middlePos = new ChessPosition(middleRow, targetCol);
        ChessPosition targetPos = new ChessPosition(targetRow, targetCol);

        if (board.getPiece(middlePos) == null && board.getPiece(targetPos) == null) {
            moves.add(new ChessMove(startPosition, targetPos, null));
        }
    }

    /**
     *
     * Adds diagonal captures for a pawn.
     * @param board the chess board containing all the pieces
     * @param startPosition the current position of the pawn
     * @param forward the direction the pawn is moving
     * @param moves array of possible moves
     * @param teamColor color of the pawn
     */
    private void addDiagonalCaptures(ChessBoard board,
            ChessPosition startPosition,
            int forward,
            Collection<ChessMove> moves,
            ChessGame.TeamColor teamColor) {

        int targetRow = startPosition.getRow() + forward;

        for (int direction : new int[]{-1, 1}) {
            int targetCol = startPosition.getColumn() + direction;
            if (!isOnBoard(targetRow, targetCol)) {
                continue;
            }

            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece pieceAtTarget = board.getPiece(targetPosition);

            if (pieceAtTarget != null && pieceAtTarget.getTeamColor() != teamColor) {
                if (isPromotionRow(targetRow, forward)) {
                    addPromotionMoves(startPosition, targetPosition, moves);
                } else {
                    moves.add(new ChessMove(startPosition, targetPosition, null));
                }
            }
        }
    }

    /**
     * Adds all possible promotion moves for a pawn.
     *
     * @param startPosition the current position of the pawn
     * @param targetPosition the target position of the pawn
     * @param moves array of possible moves
     */
        private void addPromotionMoves(
                ChessPosition startPosition,
                ChessPosition targetPosition,
                Collection<ChessMove> moves) {

            moves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(startPosition, targetPosition, ChessPiece.PieceType.KNIGHT));
        }

        /**
         * Determines if a specific square is within the bounds of a chess board
         *
         * @param row the row of the board
         * @param col the column of the board
         * @return bool value of if the square is within the bounds of a chess board
         */
        private boolean isOnBoard(int row, int col){
            return row >= 1 && row <= 8 && col >= 1 && col <= 8;
        }


    /**
     * Determines if a row is a promotion row
     *
     * @param row the row of the board
     * @param forward the direction the pawn is moving
     * @return bool value of if the row is a valid promotion row
     */
    private boolean isPromotionRow(int row, int forward) {
            return (forward == 1 && row == 8) || (forward == -1 && row == 1);
        }

    }
