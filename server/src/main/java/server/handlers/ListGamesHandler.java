package server.handlers;

import model.request.ListGamesRequest;
import model.result.ListGamesResult;
import service.GameService;
import io.javalin.http.Context;

public class ListGamesHandler extends BaseHandler<ListGamesRequest, ListGamesResult> {

    private final GameService gameService;

    public ListGamesHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    protected ListGamesRequest parseRequest(Context ctx) {
        String authToken = ctx.header("Authorization");
        return new ListGamesRequest(authToken);
    }

    @Override
    protected ListGamesResult execute(ListGamesRequest request) throws Exception {
        return gameService.listGames(request);
    }
}