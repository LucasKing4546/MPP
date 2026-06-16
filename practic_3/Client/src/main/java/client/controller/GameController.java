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
    private Button PaperButton;

    @FXML
    private Label drawLabel;

    @FXML
    private Label lastGuessLabel;

    @FXML
    private Button rockButton;

    @FXML
    private Button scissorsButton;

    @FXML
    private Label scoreLabel;

    private int score = 0;
    private int draws = 0;
    private String lastServerGuess = "?";

    @FXML
    void handlePaper(ActionEvent event) {
        http.post("/games/" + Session.gameId + "/guess?user_guess=" + PaperButton.getText(), "",
                response ->{
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    draws = json.get("draws").getAsInt();

                    if (json.get("lastServerGuess").getAsInt() == 0)
                        lastServerGuess = "Rock";
                    else if  (json.get("lastServerGuess").getAsInt() == 1)
                        lastServerGuess = "Paper";
                    else lastServerGuess = "Scissors";

                    scoreLabel.setText("Score: " + score);
                    drawLabel.setText("Draw: " + draws);
                    lastGuessLabel.setText(lastServerGuess);

                    if (json.get("finished").getAsBoolean()) {
                        showAlert("Game over!");
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> showAlert("Failed to guess")
        );
    }

    @FXML
    void handleRock(ActionEvent event) {
        http.post("/games/" + Session.gameId + "/guess?user_guess=" + rockButton.getText(), "",
                response ->{
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    draws = json.get("draws").getAsInt();

                    if (json.get("lastServerGuess").getAsInt() == 0)
                        lastServerGuess = "Rock";
                    else if  (json.get("lastServerGuess").getAsInt() == 1)
                        lastServerGuess = "Paper";
                    else lastServerGuess = "Scissors";

                    scoreLabel.setText("Score: " + score);
                    drawLabel.setText("Draw: " + draws);
                    lastGuessLabel.setText(lastServerGuess);

                    if (json.get("finished").getAsBoolean()) {
                        showAlert("Game over!");
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> showAlert("Failed to guess")
        );
    }

    @FXML
    void handleScissors(ActionEvent event) {
        http.post("/games/" + Session.gameId + "/guess?user_guess=" + scissorsButton.getText(), "",
                response ->{
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    draws = json.get("draws").getAsInt();

                    if (json.get("lastServerGuess").getAsInt() == 0)
                        lastServerGuess = "Rock";
                    else if  (json.get("lastServerGuess").getAsInt() == 1)
                        lastServerGuess = "Paper";
                    else lastServerGuess = "Scissors";

                    scoreLabel.setText("Score: " + score);
                    drawLabel.setText("Draw: " + draws);
                    lastGuessLabel.setText(lastServerGuess);

                    if (json.get("finished").getAsBoolean()) {
                        showAlert("Game over!");
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> showAlert("Failed to guess")
        );
    }

    @FXML
    public void initialize() {
        http.get("/games/" + Session.gameId,
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    score = json.get("score").getAsInt();
                    draws = json.get("draws").getAsInt();

                    if (json.get("lastServerGuess").getAsInt() == 0)
                        lastServerGuess = "Rock";
                    else if  (json.get("lastServerGuess").getAsInt() == 1)
                        lastServerGuess = "Paper";
                    else lastServerGuess = "Scissors";

                    scoreLabel.setText("Score: " + score);
                    drawLabel.setText("Draw: " + draws);
                    lastGuessLabel.setText(lastServerGuess);
                },
                error -> System.out.println("Failed to load game")
        );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
