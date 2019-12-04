package edu.unl.cse.csce361.boggle.logic;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.backend.DictionaryLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager {
	Solver answer;
	GameBoard board;
	String gameBoard[][];
	private static BackendManager bm = BackendManager.getInstance();

	private static GameManager uniqueInstance = new GameManager();
	private GameManager() {}
	public static GameManager getInstance() {
		return uniqueInstance;
	}


	private List<String> getAnswers(String board[][]) {
		answer.setDic(bm.returnDictionary());
		answer.entireSequenceFinder(board);		
		return answer.words;
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
	public int getScores(List singlePlayer){
	    int scores = 0;
	    return scores;
	}

	//Save for Sprint 2
	private int getScores(ArrayList<ArrayList<String>> Players){
	    int scores = 0;
	    return scores;
	}
}
