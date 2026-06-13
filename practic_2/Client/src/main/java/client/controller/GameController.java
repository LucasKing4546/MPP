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
import javafx.scene.control.Label;

public class GameController {
    private final HttpService http = new HttpService();

    @FXML
    private Button higherButton;

    @FXML
    private Label lastNumberLabel;

    @FXML
    private Button lowerButton;

    @FXML
    private Label scoreLabel;

    private int score = 0;
    private int lastNumber = -1;

    @FXML
    public void initialize() {
        http.get("/games/" + Session.gameId,
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    lastNumber = json.get("lastNumber").getAsInt();
                    scoreLabel.setText("Score: " + score);
                    lastNumberLabel.setText(Integer.toString(lastNumber));
                },
                error -> System.out.println("Failed to load game")
        );
    }


    @FXML
    void handleHigher(ActionEvent event) {
        http.post("/games/" + Session.gameId + "/guess?user_guess=" + higherButton.getText(), "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    lastNumber = json.get("lastNumber").getAsInt();
                    scoreLabel.setText("Score: " + score);
                    lastNumberLabel.setText(Integer.toString(lastNumber));

                    if (json.get("finished").getAsBoolean()) {
                        showAlert("Game over!");
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> System.out.println("Failed to guess")
        );
    }

    @FXML
    void handleLower(ActionEvent event) {
        http.post("/games/" + Session.gameId + "/guess?user_guess=" + lowerButton.getText(), "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    lastNumber = json.get("lastNumber").getAsInt();
                    scoreLabel.setText("Score: " + score);
                    lastNumberLabel.setText(Integer.toString(lastNumber));

                    if (json.get("finished").getAsBoolean()) {
                        showAlert("Game over!");
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> System.out.println("Failed to guess")
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
