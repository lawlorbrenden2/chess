package server;

import com.google.gson.Gson;
import model.data.UserData;
import model.result.RegisterResult;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverURL;

    public ServerFacade(String serverPort) {
        this.serverURL = serverURL;
    }

    public RegisterResult register(UserData user) throws Exception {
        var request = buildRequest("Post", "/user", user);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverURL + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }
}
