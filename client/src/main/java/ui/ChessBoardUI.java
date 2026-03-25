package ui;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int BOARD_SIZE_PLUS_BORDER_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static final String[] ROW_LABELS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] COLUMN_LABELS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static void drawChessBoard(ChessGame game, String teamColor) {
        boolean isBlack = teamColor.equals("BLACK");

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.println(ERASE_SCREEN);

        drawHorizontalBorder(out, isBlack);
        drawRows(out, isBlack);
        drawHorizontalBorder(out, isBlack);
    }

    private static void drawHorizontalBorder(PrintStream out, boolean isBlack) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);

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

    public static void drawRows(PrintStream out, boolean isBlack) {
        if (!isBlack) {
            for (int i = BOARD_SIZE_IN_SQUARES - 1; i >= 0; i--) {
                out.print(SET_BG_COLOR_LIGHT_GREY);
                out.print(SET_TEXT_COLOR_BLACK);
                out.print(" " + ROW_LABELS[i] + " ");

                for
            }
        }
    }


}

