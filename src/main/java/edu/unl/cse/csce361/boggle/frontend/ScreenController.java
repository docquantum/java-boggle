package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.backend.network.NetworkUtils;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private TextField playerNameField;
    @FXML
    private TextField numPlayer;
    @FXML
    private TextField ipAddressField;
    @FXML
    private TextField portField;
    @FXML
    private ListView multiScoreList;
    @FXML
    private Button Host;
    @FXML
    private Button Client;
    @FXML
    private Label PlayerScore;
    @FXML
    private Label nameError;
    @FXML
    private Label multiNameError;
    @FXML
    private Label multiNumPlayerError;
    @FXML
    private Label ipErrorLabel;
    @FXML
    private Label portErrorLabel;
    @FXML
    private Label spinner;


    @FXML
    public void singlePlay (Event event) throws IOException {
        manage.setMode(1);
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
        if(manage.getMode() == 1) {
            switchScreen(event, "FXML/EndScreen.fxml");
        }
        else if(manage.getMode() == 2){
            switchScreen(event, "FXML/MultiScoreScreen.fxml");
        }
    }

    @FXML
    public void multiPlay (Event event) throws IOException {
        manage.setMode(2);
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
        String playerName = this.playerNameField.getText();
        if(playerName.trim().isBlank()){
            nameError.setVisible(true);
        } else{
            manage.setPlayerName(playerName);
            switchScreen(event, "FXML/BoggleScreen.fxml");
        }
    }

    @FXML
    public void gamePlayHost (Event event) throws IOException {
        String playerName = this.playerNameField.getText();
        String numberPlayers = numPlayer.getText();

        if(playerName.trim().isBlank()){
            multiNameError.setVisible(true);
        }
        else if( numberPlayers.trim().isBlank()){
            multiNumPlayerError.setVisible(true);
        }
        else{
            // TODO make player into its own class, create, set the GM player as that player obj
            manage.setPlayerName(playerName);
            BackendManager.getInstance().setHostMode(true);
            BackendManager.getInstance().setNumOfClients(Integer.parseInt(numberPlayers));
            BackendManager.getInstance().startNetwork();
            switchScreen(event, "FXML/HostWaitScreen.fxml");
        }
    }

    @FXML
    public void gamePlayClient (Event event) throws IOException {
        String ipAddress = ipAddressField.getText();
        String port = portField.getText();

        if(!ipAddress.trim().matches(NetworkUtils.IPV4_REGEX) && !port.trim().matches(NetworkUtils.PORT_REGEX)) {
            ipErrorLabel.setVisible(true);
            portErrorLabel.setVisible(true);
        }
        else if(!ipAddress.trim().matches(NetworkUtils.IPV4_REGEX)){
            portErrorLabel.setVisible(false);
            ipErrorLabel.setVisible(true);
        }
        else if(!port.trim().matches(NetworkUtils.PORT_REGEX)){
            ipErrorLabel.setVisible(false);
            portErrorLabel.setVisible(true);
        }
        else{
            ((Button) event.getSource()).setDisable(true);
            ipErrorLabel.setVisible(false);
            portErrorLabel.setVisible(false);
            BackendManager.getInstance().setAddress(ipAddress);
            BackendManager.getInstance().setPort(Integer.parseInt(port));
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(1),
                    ae -> spinWaitAnimate()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String error = BackendManager.getInstance().startNetwork();
                    if(!error.isEmpty()){
                        timeline.stop();
                        spinner.setText(error);
                        spinner.setTextFill(Color.FIREBRICK);
                        spinner.setFont(Font.font("Arial", 14));
                        ((Button) event.getSource()).setDisable(false);
                    }

                }
            }).start();

            //switchScreen(event, "FXML/BoggleScreen.fxml");
        }
    }

    private void spinWaitAnimate(){
        if(spinner.getText().equals("")) spinner.setText(".");
        else if(spinner.getText().equals(".")) spinner.setText("..");
        else if(spinner.getText().equals("..")) spinner.setText("...");
        else if(spinner.getText().equals("...")) spinner.setText("");
    }

    private boolean validateIP(String ip) {
        String partialBlock = "(([01]?[0-9]{0,2})|(2[0-4][0-9])|(25[0-5]))";
        String subsequentPartialBlock = "(\\."+partialBlock+")";
        String ipAddress = partialBlock+"?"+subsequentPartialBlock+"{0,3}";
        String reg = "^"+ipAddress;
        return ip.matches(reg);
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
