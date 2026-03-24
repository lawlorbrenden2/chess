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
}
