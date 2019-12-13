package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.animation.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.util.*;

import javafx.animation.KeyFrame;

import java.io.IOException;
import java.net.URL;

public class BoggleScreenController implements Initializable {

    private static BoggleScreenController uniqueInstance = null;
    ScreenController sc = ScreenController.getInstance();
    GameManager manage = GameManager.getInstance();
    String[][] dice = manage.getBoard();
    String playerName = manage.getPlayerName();

    @FXML
    private Button results;
    @FXML
    private TextField playerInput1;
    @FXML
    private Button playerInput2;
    @FXML
    private ListView<String> wordViewer;
    @FXML
    private Label lbl0;
    @FXML
    private Label lbl1;
    @FXML
    private Label lbl2;
    @FXML
    private Label lbl3;
    @FXML
    private Label lbl4;
    @FXML
    private Label lbl5;
    @FXML
    private Label lbl6;
    @FXML
    private Label lbl7;
    @FXML
    private Label lbl8;
    @FXML
    private Label lbl9;
    @FXML
    private Label lbl10;
    @FXML
    private Label lbl11;
    @FXML
    private Label lbl12;
    @FXML
    private Label lbl13;
    @FXML
    private Label lbl14;
    @FXML
    private Label lbl15;
    @FXML
    private Label playname;
    @FXML
    private Label timer;
    @FXML
    private Label totalScore;

    private int time = 15;

    public static BoggleScreenController getInstance() {
        if(uniqueInstance == null){
            uniqueInstance = new BoggleScreenController();
        }
        return uniqueInstance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBoggleScreenLabels();
        setPlayerName();
        totalScore.setText(String.valueOf(manage.getTotalScore()));
        timeline();
    }

    public void setBoggleScreenLabels(){
        lbl0.setText(dice[0][0]);
        lbl1.setText(dice[0][1]);
        lbl2.setText(dice[0][2]);
        lbl3.setText(dice[0][3]);
        lbl4.setText(dice[1][0]);
        lbl5.setText(dice[1][1]);
        lbl6.setText(dice[1][2]);
        lbl7.setText(dice[1][3]);
        lbl8.setText(dice[2][0]);
        lbl9.setText(dice[2][1]);
        lbl10.setText(dice[2][2]);
        lbl11.setText(dice[2][3]);
        lbl12.setText(dice[3][0]);
        lbl13.setText(dice[3][1]);
        lbl14.setText(dice[3][2]);
        lbl15.setText(dice[3][3]);
    }


    public void setPlayerName(){
        if(playerName.isEmpty() || playerName.isBlank()){
            playname.setText("Default Player");
        }
        playname.setText(playerName);
    }

    public void timeline(){
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1),
                ae -> changeTimer()));
        timeline.setCycleCount(time);
        timeline.setOnFinished(event -> onTimerFinish());
        timeline.play();
    }

    public void changeTimer(){
        time--;
        timer.setText(time + " seconds");
    }

    public void onTimerFinish(){
        if(time == 0){
            playerInput1.setDisable(true);
            playerInput2.setDisable(true);
            manage.setPlayerInput(wordViewer.getItems());
            seeResults();
        }
    }

    public List<String> getPlayerInputs() {
        return wordViewer.getItems();
    }

    @FXML
    public void submitWord (Event event) throws IOException {
        if(!playerInput1.getText().isBlank() && !wordViewer.getItems().contains(playerInput1.getText().isBlank())){
            wordViewer.getItems().add(playerInput1.getText().trim());
        }
        playerInput1.clear();
    }

    @FXML
    public void seeResults () {
        if (!manage.isMultiplayer()) {
            try {
                sc.switchScreen("FXML/SinglePlayerEndScreen.fxml");
            } catch (IOException e){
                e.printStackTrace();
            }
        } else {

        }
    }



}
