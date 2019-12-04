package edu.unl.cse.csce361.boggle.logic;

import java.awt.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager {
    private static GameManager uniqueInstance;

    public static GameManager getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new GameManager();
        }
        return uniqueInstance;
    }

	Solver answer;
	GameBoard board = GameBoard.getInstance();
	String gameBoard[][];
	Timer time = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
		}
	};

	public Solver getAnswers(String board[][]) {
		answer.entireSequenceFinder(board);		
		return answer;
	}

	//gets the game board
    public String[][] getBoard() {
		return board.getGameBoard();			
	}

	//makes a new game-board
	public String[][] getNewBoard(){
		board.generateGameBoard();
		return board.getGameBoard();
	}

	//gets the score for singlePlayer
	public int getScores(List singlePlayer1){
	    int scores = 0;
	    return scores;
	}

	//Save for Sprint 2
	public int getScores(ArrayList<ArrayList<String>> Players){
	    int scores = 0;
	    return scores;
	}
	
	//gets timer
	public Timer getTimer() {
		return time;
	}

	//Sets a new timer
	public Timer resetTimer() {
		time = new Timer();
		return time;
	}
}
