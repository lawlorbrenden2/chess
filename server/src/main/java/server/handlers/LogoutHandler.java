package server.handlers;

import model.request.LogoutRequest;
import model.result.LogoutResult;
import service.UserService;
import io.javalin.http.Context;


public class LogoutHandler extends BaseHandler<LogoutRequest, LogoutResult> {
    public LogoutHandler(UserService userService) {
        super(userService);
    }

    @Override
    protected LogoutRequest parseRequest(Context ctx) {
        return gson.fromJson(ctx.body(), LogoutRequest.class);
    }

    @Override
    protected LogoutResult execute(LogoutRequest request) throws Exception {
        userService.logout(request);
        return new LogoutResult();
    }
}