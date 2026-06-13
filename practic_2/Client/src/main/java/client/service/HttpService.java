package client.service;

import javafx.application.Platform;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class HttpService {

    private static final Logger logger = Logger.getLogger(HttpService.class.getName());
    private static final String BASE = "http://localhost:8080/api";
    private final HttpClient client = HttpClient.newHttpClient();

    public void get(String path, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .GET().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("GET " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("GET " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }

    public void post(String path, String body, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("POST " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("POST " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }

    public void delete(String path, Consumer<String> onSuccess, Consumer<Exception> onError) {
        Thread.startVirtualThread(() -> {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(BASE + path))
                        .DELETE().build();
                HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
                logger.info("DELETE " + path + " -> " + res.statusCode());
                Platform.runLater(() -> onSuccess.accept(res.body()));
            } catch (Exception e) {
                logger.severe("DELETE " + path + " failed: " + e.getMessage());
                Platform.runLater(() -> onError.accept(e));
            }
        });
    }
}