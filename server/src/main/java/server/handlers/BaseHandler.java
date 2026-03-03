package server.handlers;

import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;
import service.exceptions.AlreadyTakenException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;


public abstract class BaseHandler<Request, Result> implements Handler {
        protected final Gson gson = new Gson();

        @Override
        public void handle(@NotNull Context ctx) {
            try {
                Request request = parseRequest(ctx);
                Result result = execute(request);
                ctx.result(new Gson().toJson(result));
            } catch (Exception e) {
                handleException(e, ctx);
            }
        }

        protected abstract Request parseRequest(Context ctx);

        protected abstract Result execute(Request request) throws Exception;

        private void handleException(Exception ex, Context ctx) {
            if (ex instanceof BadRequestException) {
                ctx.status(400);
                ctx.result(gson.toJson(new ErrorMessage("Error: bad request")));
            } else if (ex instanceof UnauthorizedException) {
                ctx.status(401);
                ctx.result(gson.toJson(new ErrorMessage("Error: unauthorized")));
            } else if (ex instanceof AlreadyTakenException) {
                ctx.status(403);
                ctx.result(gson.toJson(new ErrorMessage("Error: already taken")));
            } else {
                ctx.status(500);
                ctx.result(gson.toJson(new ErrorMessage("Error: internal server error")));
            }
        }

}


