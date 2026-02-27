package server.handlers;

import dataaccess.DataAccessException;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.jetbrains.annotations.NotNull;
import service.UserService;
import com.google.gson.Gson;
import service.exceptions.AlreadyTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;


public class RegisterHandler extends BaseHandler<RegisterRequest, RegisterResult> {
    public RegisterHandler(UserService userService) {
        super(userService);
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
