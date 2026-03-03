package server.handlers;

import model.request.ClearRequest;
import model.result.ClearResult;
import service.ClearService;
import io.javalin.http.Context;

public class ClearHandler extends BaseHandler<ClearRequest, ClearResult> {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    @Override
    protected ClearRequest parseRequest(Context ctx) {
        return new ClearRequest();
    }

    @Override
    protected ClearResult execute(ClearRequest request) throws Exception {
        return clearService.clear(request);
    }
}