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

import java.util.ArrayList;
import java.util.Timer;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BoggleScreenController implements Initializable {

    GameManager manage = GameManager.getInstance();
    String[][] dice = manage.getBoard();
    String playerName = manage.getPlayerName();

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
    private int time = 180;
    private ArrayList<String> playerInputs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBoggleScreenLabels();
        setPlayerName();
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
        timeline.setCycleCount(180);
        timeline.play();
    }

    public void changeTimer(){
        time--;
        timer.setText(time + " seconds");
        canChange();
    }

    public void canChange(){
        if(time == 0){
            playerInput1.setEditable(false);
            playerInput2.isDisable();
            playerInputs.addAll(wordViewer.getItems());
            manage.getPlayerInput(playerInputs);
        }
    }


    @FXML
    public void submitWord (Event event) throws IOException {
        if(!playerInput1.getText().isBlank()){
            wordViewer.getItems().add(playerInput1.getText().trim());
        }
        playerInput1.clear();
    }
}
