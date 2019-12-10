package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.logic.GameBoard;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class ScreenController {

    GameManager manage = GameManager.getInstance();
    private static ScreenController uniqueInstance = null;

    public static ScreenController getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new ScreenController();
        }
        return uniqueInstance;
    }

    @FXML
    private TextField PlayerName;
    @FXML
    private Label PlayerScore;
    @FXML
    private Button Host;
    @FXML
    private Button Client;


    @FXML
    public void singlePlay (Event event) throws IOException {
        switchScreen(event, "SinglePlayerScreen.fxml");
    }

    @FXML
    public void endPlay (Event event) throws IOException {
        switchScreen(event, "EndScreen.fxml");
    }

    @FXML
    public void multiPlay (Event event) throws IOException {
        switchScreen(event, "ConnectAsScreen.fxml");
    }

    @FXML
    public void connectAsHostPlay (Event event) throws IOException {
        switchScreen(event, "HostConnectingScreen.fxml");
    }

    @FXML
    public void connectAsClientPlay (Event event) throws IOException {
        switchScreen(event, "ClientConnectingScreen.fxml");
    }

    @FXML
    public void newPlay (Event event) throws IOException {
        switchScreen(event, "GameTypeScreen.fxml");
    }

    @FXML
    public void submitName(){
    }

    @FXML
    public void gamePlay (Event event) throws IOException {
        String playerName = PlayerName.getText();
        manage.playerName(playerName);
        switchScreen(event, "BoggleScreen.fxml");
    }

    @FXML
    public void exitGame (Event event) throws IOException {
        Platform.exit();
    }



    public void switchScreen(Event event, String ScreenName) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ScreenName));
        Parent parent  =  loader.load();

        Scene scene = new Scene(parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.show();
    }

}
