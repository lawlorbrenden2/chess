package server;

import chess.ChessGame;
import chess.ChessPiece;

public class ServerMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);

        Server server = new Server();
        server.run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);
    }
}
