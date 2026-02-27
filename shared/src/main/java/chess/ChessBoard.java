package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private final ChessPiece[][] squares;
    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard copy) {
        this.squares = new ChessPiece[8][8];
        for (int r = 0; r < 8; r++) {
            this.squares[r] = Arrays.copyOf(copy.squares[r], 8);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        clearBoard();
        setPawns();
        setBackRank(1, ChessGame.TeamColor.WHITE);
        setBackRank(8, ChessGame.TeamColor.BLACK);
    }

    /**
     * Clears the board to a clean state
     *
     */
    private void clearBoard() {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                squares[row-1][col-1] = null;
            }
        }
    }

    /**
     * Sets the pawns in their starting positions
     *
     */
    private void setPawns() {
        for (int col = 1; col <= 8; col++) {
            addPiece(new ChessPosition(2, col), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, col), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN ));
        }
    }


    /**
     * Sets the remaining pieces in their starting positions
     *
     * @param row Either 1 or 8 for the white and black back ranks
     * @param teamColor Color of the piece being set
     */
    private void setBackRank(int row, ChessGame.TeamColor teamColor) {
        addPiece(new ChessPosition(row, 1), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(row, 2), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 3), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 4), new ChessPiece(teamColor, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row, 5), new ChessPiece(teamColor, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(row, 6), new ChessPiece(teamColor, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(row, 7), new ChessPiece(teamColor, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(row, 8), new ChessPiece(teamColor, ChessPiece.PieceType.ROOK));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int r = 8; r >= 1; r--) {
            sb.append("|");
            for (int c = 1; c <= 8; c++) {
                ChessPiece piece = getPiece(new ChessPosition(r, c));
                if (piece == null) {
                    sb.append(" |");
                } else {
                    char ch = switch (piece.getPieceType()) {
                        case KING -> 'k';
                        case QUEEN -> 'q';
                        case ROOK -> 'r';
                        case BISHOP -> 'b';
                        case KNIGHT -> 'n';
                        case PAWN -> 'p';
                    };
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        ch = Character.toUpperCase(ch);
                    }
                    sb.append(ch).append("|");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}


