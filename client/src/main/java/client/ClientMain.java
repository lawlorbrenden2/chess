package client;


public class ClientMain {
    public static void main(String[] args) throws Exception {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);

        String serverURL = "http://localhost:8080";

        var client = new ChessClient(serverURL);
        client.run();
    }
}
