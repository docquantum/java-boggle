package edu.unl.cse.csce361.boggle.logic;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import edu.unl.cse.csce361.boggle.backend.DictionaryLoader;

import java.util.*;

public class GameManager {
	private Solver answer;
	private static GameBoard board;
	private static BackendManager bm;
	private static GameManager uniqueInstance = null;
	private String gameBoard[][];
	private String PlayerName;
	private ArrayList<String> playerWordInput = new ArrayList<String>();

	private GameManager(){
		board = GameBoard.getInstance();
		bm = BackendManager.getInstance();
		answer = new Solver();
	}

	public static GameManager getInstance() {
		if(uniqueInstance == null){
			uniqueInstance = new GameManager();
		}
		return uniqueInstance;
	}
	public void ready() {

    }

    public void startGame() {

    }

    public void endGame() {

    }
	
	public Set<String> getAnswers(String board[][]) {
		answer.setDic(bm.getDictionary());
		answer.entireSequenceFinder(board);		
		return answer.words;
	}

	public String getPlayerName(){
        return PlayerName;
    }

    public void setPlayerName(String Name){
		PlayerName = Name;
	}

	public void setPlayerInput(ArrayList<String> playerInput){
	    playerWordInput = playerInput;
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
	public int getScores(Set<String> singlePlayer){
	    return BoggleUtils.calculateScore(bm.getDictionary(), singlePlayer);
	}

	//Save for Sprint 2
	private int getScores(ArrayList<ArrayList<String>> Players){
	    int scores = 0;
	    return scores;
	}
}
