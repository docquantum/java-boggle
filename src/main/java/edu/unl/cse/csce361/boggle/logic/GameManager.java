package edu.unl.cse.csce361.boggle.logic;

import java.awt.List;
import java.util.ArrayList;
import java.util.Timer;

public class GameManager {

    Solver answer;
    GameBoard board;
    String gameBoard[][];
    Timer time = new Timer();

    Solver getAnswers(String board[][]) {
        answer.entireSequenceFinder(board);
        return answer;
    }

    String[][] getBoard() {
        return board.getGameBoard();
    }

    String[][] getNewBoard() {
        board.generateGameBoard();
        return board.getGameBoard();
    }

    int getScores(List singlePlayer1) {
        List singlePlayer = singlePlayer1;
        int scores = 0;
        return scores;
    }


    Timer getTimer() {
        return time;
    }

    Timer resetTimer() {
        time = new Timer();
        return time;
    }

}
