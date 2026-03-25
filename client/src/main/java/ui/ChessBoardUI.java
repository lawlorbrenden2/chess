package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String LIGHT_SQUARE_COLOR = SET_BG_COLOR_WHITE;
    private static final String DARK_SQUARE_COLOR = SET_BG_COLOR_LIGHT_GREY;
    private static final String BORDER_COLOR = SET_BG_COLOR_DARK_GREY;
    private static final String BORDER_TEXT_COLOR = SET_TEXT_COLOR_WHITE;
    private static final String PIECE_TEXT_COLOR = SET_TEXT_COLOR_BLACK;

    private static final String[] ROW_LABELS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] COLUMN_LABELS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static void drawChessBoard(ChessGame game, String teamColor) {
        boolean isBlack = teamColor.equals("BLACK");

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(ERASE_SCREEN);

        drawHorizontalBorder(out, isBlack);
        drawRows(out, game, isBlack);
        drawHorizontalBorder(out, isBlack);
    }

    private static void drawHorizontalBorder(PrintStream out, boolean isBlack) {
        out.print(BORDER_COLOR);
        out.print(BORDER_TEXT_COLOR);
        out.print(EMPTY);

        if (!isBlack) {
            for (int col = 0; col < BOARD_SIZE_IN_SQUARES; col++) {
                out.print(" " + COLUMN_LABELS[col] + " ");
            }
        }
        else {
            for (int col = BOARD_SIZE_IN_SQUARES - 1; col >= 0; col--) {
                out.print(" " + COLUMN_LABELS[col] + " ");
            }
        }

        out.print(EMPTY);
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
        out.println();
    }

    public static void drawRows(PrintStream out, ChessGame game, boolean isBlack) {
        ChessBoard board = game.getBoard();

        // set starting and ending rows and columns plus the direction we're moving through them
        int startRow = isBlack ? 0 : BOARD_SIZE_IN_SQUARES - 1;
        int endRow = isBlack ? BOARD_SIZE_IN_SQUARES - 1 : 0;
        int rowDirection = isBlack ? 1 : -1;

        int startCol = isBlack ? BOARD_SIZE_IN_SQUARES - 1 : 0;
        int endCol = isBlack ? 0 : BOARD_SIZE_IN_SQUARES - 1;
        int colDirection = isBlack ? -1 : 1;

        for (int row = startRow; isBlack ? row <= endRow : row >= endRow; row += rowDirection) {
            out.print(BORDER_COLOR);
            out.print(BORDER_TEXT_COLOR);
            out.print(" " + ROW_LABELS[row] + " ");

            for (int col = startCol; isBlack ? col >= endCol : col <= endCol; col += colDirection) {
                if ((row + col) % 2 == 0) {
                    out.print(DARK_SQUARE_COLOR);
                } else {
                    out.print(LIGHT_SQUARE_COLOR);
                }
                ChessPosition square = new ChessPosition(row + 1, col + 1);
                ChessPiece piece = board.getPiece(square);
                out.print(PIECE_TEXT_COLOR);
                out.print(getPieceString(piece));
            }

            out.print(BORDER_COLOR);
            out.print(BORDER_TEXT_COLOR);
            out.print(" " + ROW_LABELS[row] + " ");

            out.print(RESET_BG_COLOR);
            out.print(RESET_TEXT_COLOR);
            out.println();
        }
    }

    public static String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }

        boolean isBlack = piece.getTeamColor() == ChessGame.TeamColor.BLACK;

        return switch (piece.getPieceType()) {
            case KING -> isBlack ? BLACK_KING : WHITE_KING;
            case QUEEN -> isBlack ? BLACK_QUEEN : WHITE_QUEEN;
            case BISHOP -> isBlack ? BLACK_BISHOP : WHITE_BISHOP;
            case KNIGHT -> isBlack ? BLACK_KNIGHT : WHITE_KNIGHT;
            case ROOK -> isBlack ? BLACK_ROOK : WHITE_ROOK;
            case PAWN -> isBlack ? BLACK_PAWN : WHITE_PAWN;
        };
    }

}

