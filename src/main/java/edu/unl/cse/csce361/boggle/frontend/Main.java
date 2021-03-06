package edu.unl.cse.csce361.boggle.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        var url = getClass().getResource("FXML/GameTypeScreen.fxml");
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Boggle");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
