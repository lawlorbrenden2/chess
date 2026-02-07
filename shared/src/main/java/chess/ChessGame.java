package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private boolean whiteKingMoved;
    private boolean blackKingMoved;
    private boolean whiteKingsideRookMoved;
    private boolean blackKingsideRookMoved;
    private boolean whiteQueensideRookMoved;
    private boolean blackQueensideRookMoved;


    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteKingsideRookMoved = false;
        blackKingsideRookMoved = false;
        whiteQueensideRookMoved = false;
        blackQueensideRookMoved = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> possibleMoves = new ArrayList<>(piece.pieceMoves(board, startPosition));
        Collection<ChessMove> legalMoves = new ArrayList<>();

        for (ChessMove move : possibleMoves) {
            ChessBoard boardCopy = new ChessBoard(board);
            makeMoveHelper(boardCopy, move, piece);
            if (!isInCheckHelper(piece.getTeamColor(), boardCopy)) {
                legalMoves.add(move);
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            addCastleMoves(legalMoves, startPosition);
        }

        return legalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece piece = this.board.getPiece(start);

        if (piece == null) {
            throw new InvalidMoveException("No piece at square");
        }
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your turn");
        }
        Collection<ChessMove> validMoves = validMoves(start);
        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Illegal move");
        }
        makeMoveHelper(this.board, move, piece);
        updateCastlingRights(piece, move);
        switchTurns();

    }


    /**
     * Makes a move in a chess game on a given board
     *
     * @param board the board we're making the move on
     * @param move chess move to perform
     * @param piece the piece we move to the next square
     */
    private void makeMoveHelper(ChessBoard board, ChessMove move, ChessPiece piece) {
        // remove piece from start square
        board.addPiece(move.getStartPosition(), null);

        // update piece type if promoting
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        // replace piece at target square with new piece
        board.addPiece(move.getEndPosition(), piece);
    }


    /**
     * Updates castling rights based on the piece and move performed.
     *
     * @param piece the piece being moved
     * @param move the move just made
     */
    private void updateCastlingRights(ChessPiece piece, ChessMove move) {
        ChessPosition startPos = move.getStartPosition();

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        }

        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                if (startPos.equals(new ChessPosition(1, 1))) {
                    whiteQueensideRookMoved = true;
                } else if (startPos.equals(new ChessPosition(1, 8))) {
                    whiteKingsideRookMoved = true;
                }
            } else {
                if (startPos.equals(new ChessPosition(8, 1))) {
                    blackQueensideRookMoved = true;
                } else if (startPos.equals(new ChessPosition(8, 8))) {
                    blackKingsideRookMoved = true;
                }
            }
        }
    }

    private void addCastleMoves(Collection<ChessMove> moves, ChessPosition kingPos) {

    }

    private boolean canCastleKingside(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            if (whiteKingMoved || whiteKingsideRookMoved) return false;
            if (isInCheck(teamColor)) return false;
            if (board.getPiece(new ChessPosition(1, 6)) != null ||
                    board.getPiece(new ChessPosition(1, 7)) != null) return false;
            if (isSquareAttacked(new ChessPosition(1, 6), TeamColor.BLACK) ||
                    isSquareAttacked(new ChessPosition(1, 7), TeamColor.BLACK)) return false;


            return true;
        }
        return true;
    }

    private boolean canCastleQueenside(TeamColor teamColor) {
        return true;
    }

    private boolean isSquareAttacked(ChessPosition square, TeamColor attackingTeam) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition attackerPos = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(attackerPos);
                if (isAttackedByPiece(square, attackingTeam, piece, attackerPos, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAttackedByPiece(ChessPosition square, TeamColor attackingTeam, ChessPiece piece, ChessPosition attackerPos, ChessBoard board) {
        if (piece != null && piece.getTeamColor() == attackingTeam) {
            for (ChessMove move : piece.pieceMoves(board, attackerPos)) {
                if (move.getEndPosition().equals(square)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Switches the turns of the game
     *
     */
    private void switchTurns() {
        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckHelper(teamColor, this.board);
    }

    /**
     * Determines if the given team is in check for a given chess board
     *
     * @param teamColor which team to check for check
     * @param board the board to check
     * @return True if the specified team is in check
     */
    private boolean isInCheckHelper(TeamColor teamColor, ChessBoard board) {
        // locate the king
        ChessPosition kingPos = null;
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition square = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(square);
                if (
                        piece != null &&
                                piece.getPieceType() == ChessPiece.PieceType.KING &&
                                piece.getTeamColor() == teamColor
                ) {
                    kingPos = square;
                    break;
                }
            }
            if (kingPos != null) {
                break;
            }
        }
        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        return isSquareAttacked(kingPos, enemyColor);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        return availableMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if (isInCheck(teamColor)) {
            return false;
        }

        return availableMoves(teamColor);

    }

    /**
     * Determines if a specified side has any legal moves available to them
     *
     * @param teamColor which team to check for
     * @return True if the specified team has at least one available move, otherwise false
     */
    private boolean availableMoves(TeamColor teamColor) {
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition square = new ChessPosition(r, c);
                ChessPiece piece = board.getPiece(square);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(square).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;

    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
