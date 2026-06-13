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
import javafx.scene.layout.GridPane;

public class GameController {

    private final HttpService http = new HttpService();

    @FXML
    private GridPane gameGrid;

    @FXML
    private Label scoreLabel;

    @FXML
    private Button undoButton;

    private Button[][] buttons = new Button[5][5];
    private int[] grid;
    private int score = 0;
    private int movesLeft = 6;
    private int lastCellIndex = -1;

    @FXML
    public void initialize() {
        http.get("/games/" + Session.gameId,
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    String gridStr = json.get("grid").getAsString();
                    grid = parseIntArray(gridStr);
                    buildGrid();
                },
                error -> System.out.println("Failed to load game")
        );
    }

    private void buildGrid() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                Button cell = new Button("?");
                cell.setPrefSize(60, 60);
                int r = row, c = col;
                cell.setOnAction(e -> handleCellClick(r, c, cell));
                buttons[row][col] = cell;
                gameGrid.add(cell, col, row);
            }
        }
        scoreLabel.setText("Score: 0 | Moves left: 6");
    }

    private void handleCellClick(int r, int c, Button cell) {
        int cellIndex = r * 5 + c;
        http.post("/games/" + Session.gameId + "/move?cellIndex=" + cellIndex, "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    int value = grid[cellIndex];
                    cell.setText(String.valueOf(value));
                    cell.setDisable(true);
                    score = json.get("score").getAsInt();
                    movesLeft--;
                    lastCellIndex = cellIndex;
                    scoreLabel.setText("Score: " + score + " | Moves left: " + movesLeft);

                    if (movesLeft == 0) {
                        showAlert("Game over! Final score: " + score);
                        MainApp.switchScene("/menu.fxml");
                    }
                },
                error -> System.out.println("Move failed")
        );
    }

    @FXML
    void handleUndo(ActionEvent event) {
        if (lastCellIndex == -1) return;

        http.post("/games/" + Session.gameId + "/undo?cellIndex=" + lastCellIndex, "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    int row = lastCellIndex / 5;
                    int col = lastCellIndex % 5;
                    buttons[row][col].setText("?");
                    buttons[row][col].setDisable(false);
                    score = json.get("score").getAsInt();
                    movesLeft++;
                    lastCellIndex = -1;
                    scoreLabel.setText("Score: " + score + " | Moves left: " + movesLeft);
                },
                error -> System.out.println("Undo failed")
        );
    }

    private int[] parseIntArray(String csv) {
        String[] parts = csv.split(",");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }
        return arr;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}