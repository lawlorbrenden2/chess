package client;

import java.util.Arrays;
import java.util.Scanner;

import model.request.CreateGameRequest;
import model.request.JoinGameRequest;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import server.ServerFacade;

import static ui.EscapeSequences.*;

public class ChessClient {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;
    private String username;
    private String teamColor;
    private java.util.List<Integer> gameIDMap = new java.util.ArrayList<>();

    public ChessClient(String serverURL) {
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
                throw new Exception("Invalid game number. Run 'list' to see valid games.");
            }

            int gameID = gameIDMap.get(gameNumber - 1);

            String colorInput = params[1].toUpperCase();

            if (!colorInput.equals("WHITE") && !colorInput.equals("BLACK")) {
                throw new Exception("Color must be WHITE or BLACK");
            }

            teamColor = colorInput;

            var request = new JoinGameRequest(authToken, teamColor, gameID);
            server.joinGame(request);

            return "Joined game with gameID: " + gameNumber + " and color " + teamColor;
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

            return "Observing game " + gameNumber;
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


    public String eval(String input) throws Exception {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "create" -> createGame(params);
            case "list" -> listGames();
            case "join" -> joinGame(params);
            case "observe" -> observeGame(params);
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL>" +
                    SET_TEXT_COLOR_MAGENTA + " - to create an account\n" +
                    SET_TEXT_COLOR_BLUE + "login <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_MAGENTA + " - to play chess\n" +
                    SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_MAGENTA + " - playing chess\n" +
                    SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_MAGENTA + " - with possible commands\n";
        }
        return SET_TEXT_COLOR_BLUE + "create <NAME>" + SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                SET_TEXT_COLOR_BLUE + "list" + SET_TEXT_COLOR_MAGENTA + " - games\n" +
                SET_TEXT_COLOR_BLUE + "join <ID> [WHITE|BLACK]" + SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                SET_TEXT_COLOR_BLUE + "observe <ID>" + SET_TEXT_COLOR_MAGENTA + " - a game\n" +
                SET_TEXT_COLOR_BLUE + "logout" + SET_TEXT_COLOR_MAGENTA + " - when you are done\n" +
                SET_TEXT_COLOR_BLUE + "quit" + SET_TEXT_COLOR_MAGENTA + " - playing chess\n" +
                SET_TEXT_COLOR_BLUE + "help" + SET_TEXT_COLOR_MAGENTA + " - with possible commands\n";
    }

    private void assertLoggedIn() throws Exception {
        if (state == State.LOGGEDOUT) {
            throw new Exception("Please log in first");
        }
    }

}
