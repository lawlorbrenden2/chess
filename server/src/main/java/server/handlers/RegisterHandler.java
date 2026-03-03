package server.handlers;

import model.request.RegisterRequest;
import model.result.RegisterResult;
import service.UserService;
import io.javalin.http.Context;


public class RegisterHandler extends BaseHandler<RegisterRequest, RegisterResult> {

    private final UserService userService;

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected RegisterRequest parseRequest(Context ctx) {
        return gson.fromJson(ctx.body(), RegisterRequest.class);
    }

    @Override
    protected RegisterResult execute(RegisterRequest request) throws Exception {
        return userService.register(request);
    }
}
