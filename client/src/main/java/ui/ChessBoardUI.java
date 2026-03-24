package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static ui.EscapeSequences.*;

public class ChessBoardUI {
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 1;

    private static final String[] ROW_LABELS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] COLUMN_LABELS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    public static void drawChessBoard(PrintStream out) {
        out.println(ERASE_SCREEN);
    }

    public static void drawLetters(PrintStream out) {

    }

    public static void drawNumbers(PrintStream out) {

    }

    public static void drawSquares(PrintStream out) {

    }


}

