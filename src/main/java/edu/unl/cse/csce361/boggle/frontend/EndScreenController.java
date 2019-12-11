package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.logic.GameManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EndScreenController implements Initializable {

    ScreenController sc = ScreenController.getInstance();
    BoggleScreenController bc = BoggleScreenController.getInstance();
    GameManager manage = GameManager.getInstance();
    String[][] dice = manage.getBoard();
    String playerName = manage.getPlayerName();

    @FXML
    private Label PlayerScore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPlayerScore();
    }

    private void setPlayerScore(){
        Integer score = manage.getScores();
        PlayerScore.setText(score.toString());
    }

    @FXML
    public void newPlay (Event event) throws IOException {
        sc.newPlay(event);
    }

    @FXML
    public void exitGame (Event event) throws IOException {
        sc.exitGame(event);
    }
}
