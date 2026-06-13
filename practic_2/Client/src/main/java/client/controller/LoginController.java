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
import javafx.scene.control.TextField;

public class LoginController {

    private final HttpService http = new HttpService();

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameTextField;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameTextField.getText();
        if (username.isEmpty()) {
            return;
        }

        http.post("/users?username=" + username, "",
                response ->{
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    Session.userId =jsonObject.get("id").getAsLong();
                    Session.username = jsonObject.get("username").getAsString();
                    MainApp.switchScene("/menu.fxml");
                },
                error ->{
                    showAlert("Login failed");
                }
                );
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
