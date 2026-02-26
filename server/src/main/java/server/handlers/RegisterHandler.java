package server.handlers;

import dataaccess.DataAccessException;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import service.UserService;
import com.google.gson.Gson;
import service.AlreadyTakenException;
import io.javalin.http.Context;


public class RegisterHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegisterHandler(UserService userService) {
        this.userService = userService;
    }

    public void handleRegister(Context ctx) {
        try {
            RegisterRequest request = gson.fromJson(ctx.body(), RegisterRequest.class);
            RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.json(result);
        } catch (Exception e) {
            handleException(e, ctx);
        }
    }

    private void handleException(Exception ex, Context ctx) {
        if (ex instanceof AlreadyTakenException) {
            ctx.status(403);
            ctx.result(new Gson().toJson(new ErrorMessage("Error: already taken")));
        } else if (ex instanceof DataAccessException) {
            ctx.status(500);
            ctx.result(new Gson().toJson(new ErrorMessage("Error: internal server error")));
        } else {
            ctx.status(400);
            ctx.result(new Gson().toJson(new ErrorMessage("Error: bad request")));
        }
    }
}
