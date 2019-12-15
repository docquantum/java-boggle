package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

import java.net.URL;
import java.util.ResourceBundle;

public class EndScreenController implements Initializable {

    ScreenController sc = ScreenController.getInstance();
    BoggleScreenController bc = BoggleScreenController.getInstance();
    GameManager manage = GameManager.getInstance();

    @FXML
    private Label thisRoundScore;

    @FXML
    private Label totalScore;

    @FXML
    private ListView<String> multiScoreList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(manage.isMultiPlayer()){
            multiScoreList.getItems().setAll(manage.getMultiPlayerScores());
        } else {
            thisRoundScore.setText(String.valueOf(manage.getSinglePlayerScore()));
            totalScore.setText(String.valueOf(manage.getTotalScore()));
        }
    }

    @FXML
    public void newRound (Event event) throws IOException {
        if(manage.isMultiPlayer()){

        } else{
            manage.genNewBoard();
            new Thread(() -> manage.cacheAnswers()).start();
            sc.switchScreen( event, "FXML/BoggleScreen.fxml");
        }
    }

    @FXML
    public void mainMenu (Event event) throws IOException {
        manage.resetState();
        sc.mainMenu(event);
    }
}
