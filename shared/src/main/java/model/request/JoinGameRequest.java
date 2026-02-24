package model.request;

import model.chess.ChessGame;

public record JoinGameRequest(String authToken, ChessGame.TeamColor playerColor, Integer gameID) {
}
