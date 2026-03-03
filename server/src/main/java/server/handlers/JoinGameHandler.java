package server.handlers;

import model.request.JoinGameRequest;
import model.result.JoinGameResult;
import service.GameService;
import io.javalin.http.Context;


public class JoinGameHandler extends BaseHandler<JoinGameRequest, JoinGameResult> {

    private final GameService gameService;

    public JoinGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected JoinGameRequest parseRequest(Context ctx) {
        String authToken = ctx.header("Authorization");
        JoinGameRequest body = gson.fromJson(ctx.body(), JoinGameRequest.class);

        return new JoinGameRequest(authToken, body.playerColor(), body.gameID());
    }

    @Override
    protected JoinGameResult execute(JoinGameRequest request) throws Exception {
        return gameService.joinGame(request);
    }
}