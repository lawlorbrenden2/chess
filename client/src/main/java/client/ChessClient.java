package client;

import java.util.Arrays;
import java.util.Scanner;

import server.ServerFacade;
import com.google.gson.Gson;
import model.*;
import static ui.EscapeSequences.*;

public class ChessClient {
    private String authToken = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;

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

    public String eval(String input) {
        return "";
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
}
