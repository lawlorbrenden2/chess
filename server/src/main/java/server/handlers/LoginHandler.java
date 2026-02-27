package server.handlers;

import model.request.LoginRequest;
import model.result.LoginResult;
import service.UserService;
import io.javalin.http.Context;


public class LoginHandler extends BaseHandler<LoginRequest, LoginResult> {
    public LoginHandler(UserService userService) {
        super(userService);
    }

    @Override
    protected LoginRequest parseRequest(Context ctx) {
        return gson.fromJson(ctx.body(), LoginRequest.class);
    }

    @Override
    protected LoginResult execute(LoginRequest request) throws Exception {
        return userService.login(request);
    }
}