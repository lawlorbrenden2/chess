package model.data;

import model.chess.ChessGame;

public record GameData(
        Integer gameID,
        String whiteUsername,
        String blackUsername,
        String gameName,
        ChessGame game
) {
}
