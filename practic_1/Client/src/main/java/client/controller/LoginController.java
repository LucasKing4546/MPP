package client.controller;

import client.MainApp;
import client.Session;
import client.service.HttpService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String username = usernameTextField.getText().trim();
        if (username.isEmpty()) return;

        http.post("/users?username=" + username, "",
                response -> {
                    JsonObject json = JsonParser.parseString(response).getAsJsonObject();
                    Session.userId = json.get("id").getAsLong();
                    Session.username = username;
                    MainApp.switchScene("/menu.fxml");
                },
                error -> loginButton.setText("Connection failed")
        );
    }
}