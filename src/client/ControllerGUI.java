package client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
//C:\Users\Sara\AppData\Local\SceneBuilder\SceneBuilder.exe
public class ControllerGUI {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}