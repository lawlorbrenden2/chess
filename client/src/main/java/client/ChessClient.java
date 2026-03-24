package client;

import java.util.Arrays;
import java.util.Scanner;

import model.result.*;
import model.request.*;
import server.ServerFacade;
import com.google.gson.Gson;
import model.*;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String username;

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
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[" + state + "] >>> " + SET_TEXT_COLOR_GREEN);
    }

    public String register(String... params) throws Exception {
        if (params.length >= 3) {
            var request = new RegisterRequest(params[0], params[1], params[2]);
            var result = server.register(request);
            authToken = result.authToken();
            server.setAuthToken(authToken);
            state = State.SIGNEDIN;
            username = params[0];

            return "Registered as " + username;
        }
        throw new Exception("Expected: <username> <password>");
    }

    public String login(String... params) throws Exception {
        if (params.length >= 2) {
            var request = new LoginRequest(params[0], params[1]);
            var result = server.login(request);
            authToken = result.authToken();
            server.setAuthToken(authToken);
            state = State.SIGNEDIN;
            username = params[0];

            return "Logged in as " + username;
        }
        throw new Exception("Expected: <username> <password>");
    }

    public String createGame(String... params) throws Exception {
        assertSignedIn();
        if (params.length >= 1) {
            var request = new CreateGameRequest(params[0], authToken);
            var result = server.createGame(request);

            return "Created game with gameID: " + result.gameID();
        }
        throw new Exception("Expected: <username> <password>");
    }

    public String listGames() throws Exception {
        assertSignedIn();
        var result = server.listGames();

        var output = new StringBuilder();
        for (var game : result.games()) {
            output.append(game.gameID())
                    .append(": ")
                    .append(game.gameName())
                    .append("\n");
        }
        return output.toString();
    }

    public String joinGame(String... params) throws Exception {
        if (params.length >= 2) {
            int gameID = Integer.parseInt(params[0]);
            String color = params[1].toUpperCase();
            var request = new JoinGameRequest(authToken, color, gameID);
            server.joinGame(request);

            return "Joined game with gameID: " + gameID;
        }

        throw new Exception("Expected: <gameID> <WHITE|BLACK>");
    }

    public String logout() throws Exception {
        assertSignedIn();
        server.logout();

        state = State.SIGNEDOUT;
        return "";
    }


    public String eval(String input) throws Exception {
        String[] tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "register" -> register(params);
            case "login" -> login(params);
            case "createGame" -> createGame(params);
            case "listGames" -> listGames();
            case "joinGame" -> joinGame(params);
            case "logout" -> logout();
            case "quit" -> "quit";
            default -> help();
        };
    }


    public String help() {
        if (state == State.SIGNEDOUT) {
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

    private void assertSignedIn() throws Exception {
        if (state == State.SIGNEDOUT) {
            throw new Exception("You must sign in");
        }
    }

}
