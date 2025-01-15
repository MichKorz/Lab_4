package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application
{
    Client client;
    Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException
    {
        primaryStage = stage; // Save stage for further use

        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("connect-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);

        stage.setTitle("G̶e̶r̶m̶a̶n̶?̶ Chinese Checkers");
        stage.setScene(scene);
        stage.show();

        Button connectButton = (Button) scene.lookup("#connectButton");
        TextField ipField = (TextField) scene.lookup("#ipField");
        TextField portField = (TextField) scene.lookup("#portField");

        connectButton.setOnAction(event -> {
            String ip = ipField.getText();
            int port = Integer.parseInt(portField.getText());

            if (client == null) client = createClient();
            client.connect(ip, port);
        });
    }

    public static void main(String[] args)
    {
        launch();
    }

    Client createClient()
    {
        return new Client(this);
    }

    void launchGame(String gameVariant, int playerCount)
    {
        FXMLLoader gameLoader = new FXMLLoader(ClientApplication.class.getResource(gameVariant + ".fxml"));
        try
        {
            Scene gameScene = new Scene(gameLoader.load(), 820, 650);
            primaryStage.setTitle("'Chinese' Checkers");
            primaryStage.setScene(gameScene);
            primaryStage.show();
            GUIController controller = gameLoader.getController();
            controller.playerCount = playerCount;
            controller.generateBoard();

            client.LaunchClient(gameLoader.getController());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}