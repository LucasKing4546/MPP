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
    private Button checkButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button startButton;

    @FXML
    void handleCheck(ActionEvent event) {
        http.get("/games/stats?userId=" + Session.userId,
                response -> {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    showAlert("Stats for user" + Session.userId + "\n\n\n" + jsonObject.toString());
                },
                error -> showAlert("Failed to load stats")
        );
    }

    @FXML
    void handleClear(ActionEvent event) {
        http.delete("/games/stats?userId=" + Session.userId,
                response -> showAlert("Stats cleared!"),
                error -> showAlert("Failed to clear stats")
        );
    }

    @FXML
    void handleStrat(ActionEvent event) {
        http.post("/games/start?userId=" + Session.userId, "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    Session.gameId = json.get("id").getAsLong();
                    MainApp.switchScene("/game.fxml");
                },
                error -> showAlert("Failed to start game")
                );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
