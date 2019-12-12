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
    private TextField numPlayer;
    @FXML
    private TextField ipAddress;
    @FXML
    private Label PlayerScore;
    @FXML
    private Button Host;
    @FXML
    private Button Client;
    @FXML
    private Label nameError;
    @FXML
    private Label multiNameError;
    @FXML
    private Label multiNumPlayerError;
    @FXML
    private Label IPAddressError;
    @FXML
    private Label nameErrorClient;


    @FXML
    public void singlePlay (Event event) throws IOException {
        switchScreen(event, "FXML/SinglePlayerScreen.fxml");
        new Thread(new Runnable() {
            @Override
            public void run() {
                manage.cacheAnswers();
            }
        }).start();
    }

    @FXML
    public void endPlay (Event event) throws IOException {
        switchScreen(event, "FXML/EndScreen.fxml");
    }

    @FXML
    public void multiPlay (Event event) throws IOException {
        switchScreen(event, "FXML/ConnectAsScreen.fxml");
    }

    @FXML
    public void connectAsHostPlay (Event event) throws IOException {
        switchScreen(event, "FXML/HostConnectingScreen.fxml");
    }

    @FXML
    public void connectAsClientPlay (Event event) throws IOException {
        switchScreen(event, "FXML/ClientConnectingScreen.fxml");
    }

    @FXML
    public void newPlay (Event event) throws IOException {
        manage.genNewBoard();
        switchScreen(event, "FXML/GameTypeScreen.fxml");
    }

    @FXML
    public void submitName(){
    }

    @FXML
    public void gamePlay (Event event) throws IOException {
        String playerName = PlayerName.getText();
        if(playerName.trim().isBlank()){
            nameError.setVisible(true);
        } else{
            manage.setPlayerName(playerName);
            switchScreen(event, "FXML/BoggleScreen.fxml");
        }
    }

    @FXML
    public void gamePlayHost (Event event) throws IOException {
        String playerName = PlayerName.getText();
        String numberPlayers = numPlayer.getText();

        if(playerName.trim().isBlank()){
            multiNameError.setVisible(true);
        }
        else if( numberPlayers.trim().isBlank()){
            multiNumPlayerError.setVisible(true);
        }
        else{
            manage.setPlayerName(playerName);
            switchScreen(event, "FXML/BoggleScreen.fxml");
        }
    }

    @FXML
    public void gamePlayClient (Event event) throws IOException {
        String playerName = PlayerName.getText();
        String IPAddress = ipAddress.getText();

        if(playerName.trim().isBlank()){
            nameErrorClient.setVisible(true);
        }
        else if(IPAddress.trim().isBlank()){
            IPAddressError.setVisible(true);
        }
        else{
            manage.setPlayerName(playerName);
            switchScreen(event, "FXML/BoggleScreen.fxml");
        }
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
