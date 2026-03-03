package server.handlers;

import model.request.CreateGameRequest;
import model.result.CreateGameResult;
import service.GameService;
import io.javalin.http.Context;


public class CreateGameHandler extends BaseHandler<CreateGameRequest, CreateGameResult> {

    private final GameService gameService;

    public CreateGameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected CreateGameRequest parseRequest(Context ctx) {
        String authToken = ctx.header("Authorization");
        CreateGameRequest body = gson.fromJson(ctx.body(), CreateGameRequest.class);
        return new CreateGameRequest(authToken, body.gameName());
    }

    @Override
    protected CreateGameResult execute(CreateGameRequest request) throws Exception {
        return gameService.createGame(request);
    }
}