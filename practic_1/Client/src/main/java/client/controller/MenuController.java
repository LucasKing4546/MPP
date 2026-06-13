package client.controller;

import client.MainApp;
import client.Session;
import client.service.HttpService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class MenuController {

    private final HttpService http = new HttpService();

    @FXML
    private Button clearButton;

    @FXML
    private Button startButton;

    @FXML
    private Button viewButton;

    @FXML
    void handleStart(ActionEvent event) {
        http.post("/games/start?userId=" + Session.userId, "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    Session.gameId = json.get("id").getAsLong();
                    MainApp.switchScene("/game.fxml");
                },
                error -> showAlert("Failed to start game")
        );
    }

    @FXML
    void handleView(ActionEvent event) {
        http.get("/games/stats?userId=" + Session.userId,
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    showAlert("Stats for user: " + Session.userId + "\n\n" + json.toString());
                },
                error -> showAlert("Failed to load stats")
        );
    }

    @FXML
    void handleClear(ActionEvent event) {
        http.delete("/games/stats?userId=" + Session.userId,
                response -> showAlert("Statistics cleared!"),
                error -> showAlert("Failed to clear stats")
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}