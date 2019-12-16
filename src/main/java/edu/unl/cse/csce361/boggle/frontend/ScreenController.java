package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.backend.network.NetworkUtils;
import edu.unl.cse.csce361.boggle.logic.GameManager;
import edu.unl.cse.csce361.boggle.logic.Player;
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
import javafx.stage.Stage;
import javafx.stage.Window;
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
    private Button clientStartGameButt;
    @FXML
    private Button clientWaitStartButt;
    @FXML
    private Label clientWaitLabel;

    @FXML
    public void singlePlay (Event event) throws IOException {
        manage.setMultiPlayer(false);
        manage.setPlayerName("Single Player");
        switchScreen(event, "FXML/BoggleScreen.fxml");
        new Thread(() -> manage.cacheAnswers()).start();
    }

    @FXML
    public void endPlay () throws IOException {
        switchScreen("FXML/SinglePlayerEndScreen.fxml");
    }

    @FXML
    public void multiPlay (Event event) throws IOException {
        manage.setMultiPlayer(true);
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
    public void gamePlayHost (Event event) throws IOException {
        multiNameError.setVisible(false);
        multiNumPlayerError.setVisible(false);
        String playerName = this.playerNameField.getText();
        String numberPlayers = numPlayer.getText();

        if(playerName.trim().isBlank()){
            multiNameError.setVisible(true);
        }
        else if(numberPlayers.trim().isBlank()){
            multiNumPlayerError.setVisible(true);
        }
        else{
            manage.setPlayerName(playerName);
            manage.setLocalPlayer(manage.addPlayer(playerName));
            new Thread(() -> manage.cacheAnswers()).start();
            BackendManager.getInstance().setHostMode(true);
            BackendManager.getInstance().setNumOfClients(Integer.parseInt(numberPlayers));
            BackendManager.getInstance().startNetwork();
            switchScreen(event, "FXML/HostWaitScreen.fxml");
        }
    }

    @FXML
    public void gamePlayClient (Event event) throws IOException {
        spinner.setText("");
        spinner.setTextFill(Color.BLACK);
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
            spinner.setText("Connecting");
            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.seconds(1),
                    ae -> spinWaitAnimate()));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            new Thread(() -> {
                String error = BackendManager.getInstance().startNetwork();
                Platform.runLater(() -> {
                    if(!error.isEmpty()){
                        timeline.stop();
                        spinner.setText(error);
                        spinner.setTextFill(Color.FIREBRICK);
                        ((Button) event.getSource()).setDisable(false);
                    } else{
                        try {
                            timeline.stop();
                            switchScreen(event, "FXML/ClientChooseNameScreen.fxml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }).start();
        }
    }

    public void clientTryName(Event event){
        nameError.setVisible(false);
        clientStartGameButt.setDisable(true);
        String playerName = playerNameField.getText();
        if(playerName.trim().isBlank()){
            nameError.setText("Please enter a name");
            nameError.setVisible(true);
            clientStartGameButt.setDisable(false);
        } else {
            BackendManager.getInstance().getNameTakenProperty().addListener((observableValue, oldInt, newInt) -> {
                if(oldInt.intValue() < newInt.intValue()){
                    Platform.runLater(() -> {
                        nameError.setText("Name is taken, please try another");
                        nameError.setVisible(true);
                        clientStartGameButt.setDisable(false);
                    });
                } else if(oldInt.intValue() > newInt.intValue()){
                    manage.setPlayerName(playerName);
                    manage.setLocalPlayer(new Player(playerName));
                    Platform.runLater(() -> {
                        try {
                            switchScreen( "FXML/ClientWaitScreen.fxml");
                            BackendManager.getInstance().getAllReadyProperty().addListener((obsBool, oldBool, newBool) -> {
                                if(newBool){
                                    Platform.runLater(() -> {
                                        try {
                                            switchScreen("FXML/BoggleScreen.fxml");
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            new Thread(() -> BackendManager.getInstance().sendPlayerName(playerName)).start();
        }

    }

    public void handleEndGame(){
        if(manage.isMultiPlayer()){
            manage.getGotAllScoresProperty().addListener((observable, oldBool, newBool) -> {
                if (newBool) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("FXML/MultiScoreScreen.fxml");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            });
            if(!BackendManager.getInstance().getHostMode()){
                BackendManager.getInstance().sendPlayerObject(GameManager.getInstance().getLocalPlayer());
            } else {
                BackendManager.getInstance().getAllWordsProperty().addListener((observable, oldBool, newBool) -> {
                    if(newBool){
                        GameManager.getInstance().calculateMultiPlayerScores();
                        BackendManager.getInstance().sendAllScores();
                    }
                });
            }
        } else {
            try {
                endPlay();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void spinWaitAnimate(){
        if(spinner.getText().equals("Connecting")) spinner.setText("Connecting.");
        else if(spinner.getText().equals("Connecting.")) spinner.setText("Connecting..");
        else if(spinner.getText().equals("Connecting..")) spinner.setText("Connecting...");
        else if(spinner.getText().equals("Connecting...")) spinner.setText("Connecting");
    }

    @FXML
    public void mainMenu(Event event) throws IOException {
        switchScreen(event, "FXML/GameTypeScreen.fxml");
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

    public void switchScreen(Stage window, String ScreenName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ScreenName));
        Parent parent  =  loader.load();

        Scene scene = new Scene(parent);

        window.setScene(scene);
        window.show();
    }

    public void switchScreen(String ScreenName) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(ScreenName));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);
        Stage window = Stage.getWindows().stream()
                .filter(Window::isShowing)
                .map(window1 -> (Stage) window1)
                .findFirst()
                .orElse(null);
        if(window == null) throw new IOException();
        window.setScene(scene);
        window.show();
    }

}
