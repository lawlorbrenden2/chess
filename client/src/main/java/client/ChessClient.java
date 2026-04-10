package client;

import java.util.Arrays;
import java.util.Scanner;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import jdk.jshell.spi.ExecutionControlProvider;
import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import server.ServerFacade;
import ui.ChessBoardUI;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.commands.*;
import websocket.messages.*;

import static ui.EscapeSequences.*;

public class ChessClient implements NotificationHandler {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;
    private String username;
    private ChessGame.TeamColor teamColor;
    private final java.util.List<Integer> gameIDMap = new java.util.ArrayList<>();
    private WebSocketFacade ws;
    private Integer currentGameID;
    private ChessGame currentGame;


    public ChessClient(String serverURL) throws Exception {
        server = new ServerFacade(serverURL);
    }

    public void run() {
        System.out.println("👑 Welcome to 240 chess. Type Help to get started. 👑");
        System.out.println(help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                System.out.print(result);
            } catch (Exception e) {
                System.out.println(SET_TEXT_COLOR_RED + "Error: " + e.getMessage());
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + (username != null ? ":" + username : "")
                + "] >>> " + SET_TEXT_COLOR_GREEN);
    }

    public String eval(String input) throws Exception {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        return switch (state) {
            case LOGGEDOUT -> getLoggedOutInput(params, cmd);
            case LOGGEDIN -> getLoggedInInput(params, cmd);
            case GAMEPLAY -> getGameplayInput(params, cmd);
            case OBSERVER -> getObserverInput(params, cmd);
        };
    }

    private String getLoggedOutInput(String[] params, String cmd) throws Exception {
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String getLoggedInInput(String[] params, String cmd) throws Exception {
        return switch (cmd) {
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }

    private String getGameplayInput(String[] params, String cmd) throws Exception {
        return switch (cmd) {
            case "redraw" -> redrawBoard(params);
            case "leave" -> leave(params);
            case "move" -> makeMove(params);
            case "resign" -> resign(params);
            case "highlight" -> highlightLegalMoves(params);
            default -> help();
        };
    }

    private String getObserverInput(String[] params, String cmd) throws Exception {
        return switch (cmd) {
            case "redraw" -> redrawBoard(params);
            case "leave" -> leave(params);
            case "highlight" -> highlightLegalMoves(params);
            default -> help();
        };
    }

    public String help() {
        return switch (state) {
            case LOGGEDOUT ->
                    SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" +
                    SET_TEXT_COLOR_MAGENTA + " - to create an account\n" +
                    SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" +
                    SET_TEXT_COLOR_MAGENTA + " - to play chess\n" +
                    SET_TEXT_COLOR_BLUE + "quit" +
                    SET_TEXT_COLOR_MAGENTA + " - exit\n" +
                    SET_TEXT_COLOR_BLUE + "help" +
                    SET_TEXT_COLOR_MAGENTA + " - show commands\n";

            case LOGGEDIN ->
                    SET_TEXT_COLOR_BLUE + "create <NAME>" +
                    SET_TEXT_COLOR_MAGENTA + " - create a game\n" +
                    SET_TEXT_COLOR_BLUE + "list" +
                    SET_TEXT_COLOR_MAGENTA + " - list games\n" +
                    SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" +
                    SET_TEXT_COLOR_MAGENTA + " - join a game\n" +
                    SET_TEXT_COLOR_BLUE + "observe <ID>" +
                    SET_TEXT_COLOR_MAGENTA + " - observe a game\n" +
                    SET_TEXT_COLOR_BLUE + "logout" +
                    SET_TEXT_COLOR_MAGENTA + " - logout\n" +
                    SET_TEXT_COLOR_BLUE + "quit" +
                    SET_TEXT_COLOR_MAGENTA + " - exit\n";

            case GAMEPLAY ->
                    SET_TEXT_COLOR_BLUE + "redraw" +
                    SET_TEXT_COLOR_MAGENTA + " - redraw board\n" +
                    SET_TEXT_COLOR_BLUE + "move <from> <to>" +
                    SET_TEXT_COLOR_MAGENTA + " - make a move\n" +
                    SET_TEXT_COLOR_BLUE + "leave" +
                    SET_TEXT_COLOR_MAGENTA + " - leave game\n" +
                    SET_TEXT_COLOR_BLUE + "resign" +
                    SET_TEXT_COLOR_MAGENTA + " - resign game\n" +
                    SET_TEXT_COLOR_BLUE + "highlight <SQUARE>" +
                    SET_TEXT_COLOR_MAGENTA + " - show legal moves\n";

            case OBSERVER ->
                    SET_TEXT_COLOR_BLUE + "redraw" +
                    SET_TEXT_COLOR_MAGENTA + " - redraw board\n" +
                    SET_TEXT_COLOR_BLUE + "leave" +
                    SET_TEXT_COLOR_MAGENTA + " - leave game\n" +
                    SET_TEXT_COLOR_BLUE + "highlight <SQUARE>" +
                    SET_TEXT_COLOR_MAGENTA + " - show legal moves\n";
        };
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            var request = new RegisterRequest(params[0], params[1], params[2]);
            var result = server.register(request);
            authToken = result.authToken();
            server.setAuthToken(authToken);
            state = State.LOGGEDIN;
            username = params[0];

            return "Registered as " + username;
        }
        throw new Exception("Expected: <username> <password> <email>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {
            var request = new LoginRequest(params[0], params[1]);
            var result = server.login(request);
            authToken = result.authToken();
            server.setAuthToken(authToken);
            state = State.LOGGEDIN;
            username = params[0];

            return "Logged in as " + username;
        }
        throw new Exception("Expected: <username> <password>");
    }

    public String createGame(String... params) throws Exception {
        assertLoggedIn();
        if (params.length >= 1) {
            var request = new CreateGameRequest(authToken, params[0]);
            var result = server.createGame(request);

            gameIDMap.add(result.gameID());

            return "Created game " + params[0] + " with game number: " + result.gameID();
        }
        throw new Exception("Expected: gameName");
    }

    public String listGames() throws Exception {
        assertLoggedIn();
        gameIDMap.clear();
        var result = server.listGames();

        var output = new StringBuilder();
        int gameNumber = 1;

        for (var game : result.games()) {
            gameIDMap.add(game.gameID());

            output.append(gameNumber)
                  .append(": ")
                  .append(game.gameName());

            if (game.whiteUsername() != null) {
                output.append(" [White: ").append(game.whiteUsername()).append("]");
            }
            else {
                output.append(" [White: <available>]");
            }
            if (game.blackUsername() != null) {
                output.append(" [Black: ").append(game.blackUsername()).append("]");
            }
            else {
                output.append(" [Black: <available>]");
            }
            output.append("\n");
            gameNumber++;
        }
        if (gameIDMap.isEmpty()) {
            throw new Exception("No games found. Add a game using 'create'");
        }

        return output.toString();
    }

    public String joinGame(String... params) throws Exception {
        assertLoggedIn();
        if (params.length >= 2) {
            int gameNumber;

            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new Exception("Game ID must be a number");
            }

            if (gameIDMap.isEmpty()) {
                throw new Exception("No games loaded. Run 'list' first");
            }
            if (gameNumber < 1 || gameNumber > gameIDMap.size()) {
                System.out.println(gameIDMap);
                throw new Exception("Invalid game number. Run 'list' to see valid games");
            }
            int gameID = gameIDMap.get(gameNumber - 1);

            String colorInput = params[1].toUpperCase();
            if (!colorInput.equals("WHITE") && !colorInput.equals("BLACK")) {
                throw new Exception("Color must be WHITE or BLACK");
            }

            teamColor = colorInput.equals("WHITE") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK ;
            var request = new JoinGameRequest(authToken, colorInput, gameID);
            server.joinGame(request);
            state = State.GAMEPLAY;
            currentGameID = gameID;

            ws = new WebSocketFacade(this);
            ws.connect("ws://localhost:8080/ws");
            Thread.sleep(200);
            ws.sendCommand(new ConnectCommand(authToken, gameID));

            System.out.println("Joined game with gameID: " + gameNumber + " and color " + teamColor + "\n");

            return "";
        }

        throw new Exception("Expected: <game number> <WHITE|BLACK>");
    }

    public String observeGame(String... params) throws Exception {
        assertLoggedIn();

        if (params.length >= 1) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
            } catch (NumberFormatException e) {
                throw new Exception("Game number must be a number");
            }

            if (gameNumber < 1 || gameNumber > gameIDMap.size()) {
                throw new Exception("Invalid game number.");
            }

            state = State.OBSERVER;
            System.out.println("Observing game with gameID: " + gameNumber + " and color " + teamColor + "\n");

            int gameID = gameIDMap.get(gameNumber - 1);
            currentGameID = gameID;


            ws = new WebSocketFacade(this);
            ws.connect("ws://localhost:8080/ws");
            Thread.sleep(200);
            ws.sendCommand(new ConnectCommand(authToken, gameID));

            return "";
        }

        throw new Exception("Expected: <game number>");
    }

    public String logout() throws Exception {
        assertLoggedIn();
        server.logout();
        username = null;
        authToken = null;
        state = State.LOGGEDOUT;
        return "";
    }

    public String redrawBoard(String[] params) {
        return "";
    }

    public String leave(String[] params) {
        ws.sendCommand(new LeaveCommand(authToken, currentGameID));
        state = State.LOGGEDIN;
        int oldGameID = currentGameID;
        currentGame = null;
        currentGameID = null;
        return SET_TEXT_COLOR_BLUE + "Left game " + oldGameID;
    }

    public String makeMove(String[] params) throws Exception {
        if (params.length == 0 || params.length > 2) {
            throw new Exception("Expected format: move <FROM><TO> <PROMOTION> (e.g., e2e4 or e7e8 queen)");
        }

        String moveString = params[0];
        if (moveString.length() != 4) {
            throw new Exception("Coordinates must be exactly 4 characters (e.g., e2e4).");
        }

        String startString = moveString.substring(0, 2);
        String endString = moveString.substring(2, 4);

        ChessPosition startPos = parseCoordinatesHelper(startString);
        ChessPosition endPos = parseCoordinatesHelper(endString);

        ChessPiece.PieceType promotionPiece = null;

        if (params.length == 2) {
            promotionPiece = checkPromotionPieceHelper(params[1]);
        }

        ChessMove move = new ChessMove(startPos, endPos, promotionPiece);
        ws.sendCommand(new MakeMoveCommand(authToken, currentGameID, move));
        return "";
    }


    public String resign(String[] params) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print(SET_TEXT_COLOR_GREEN + "Are you sure you want to resign? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (!response.equals("yes") && (!response.equals("y"))) {
            return SET_TEXT_COLOR_BLUE + "Resignation cancelled";
        }
        ws.sendCommand(new ResignCommand(authToken, currentGameID));
        state = State.LOGGEDIN;
        int oldGameID = currentGameID;
        currentGame = null;
        currentGameID = null;
        return SET_TEXT_COLOR_BLUE + "Resigned from game " + oldGameID;
}

    public String highlightLegalMoves(String[] params) {
        return "";
    }

    private ChessPosition parseCoordinatesHelper(String pos) throws Exception {
        if (pos == null || pos.length() != 2) {
            throw new Exception("Invalid position format: " + pos);
        }

        pos = pos.toLowerCase();
        char colChar = pos.charAt(0);
        char rowChar = pos.charAt(1);

        // Use ASCII math
        int col = (colChar - 'a') + 1;
        int row = (rowChar - '1') + 1;

        if (col < 0 || col > 7 || row < 0 || row > 7) {
            throw new Exception("Position out of bounds. Must be between a1 and h8.");
        }

        return new ChessPosition(row, col);
    }

    private ChessPiece.PieceType checkPromotionPieceHelper(String piece) throws Exception {
        return switch (piece.toUpperCase()) {
            case "Q", "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "R", "ROOK" -> ChessPiece.PieceType.ROOK;
            case "B", "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "N", "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new Exception("Invalid promotion piece. Use Q, R, B, or N.");
        };
    }

    @Override
    public void notifyMessage(NotificationMessage notificationMessage) {
        System.out.println("\n" + notificationMessage.getMessage());
        printPrompt();
    }

    @Override
    public void notifyError(ErrorMessage errorMessage) {
        System.out.println("\n" + SET_TEXT_COLOR_RED + "\nERROR: " + errorMessage.getError());
        printPrompt();
    }

    @Override
    public void loadGame(ChessGame game) {
        this.currentGame = game;
        System.out.println("\nGame updated:");
        ChessBoardUI.drawChessBoard(game, String.valueOf(teamColor != null ? teamColor : ChessGame.TeamColor.WHITE));
        printPrompt();
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Please log in first");
        }
    }

}
