package edu.unl.cse.csce361.boggle.logic;

import edu.unl.cse.csce361.boggle.backend.BackendManager;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
	private Solver solver;
	private static GameBoard board;
	private static BackendManager bm;
	private static GameManager uniqueInstance = null;
	private String gameBoard[][];
	private String playerName;
	private List<String> playerWordInput = new ArrayList<>();
	private Set<String> answers;

	private GameManager(){
		Comparator<String> c = new Comparator<String>() {
			@Override
			public int compare(String s, String t1) {
				return 0;
			}
		};
		board = GameBoard.getInstance();
		bm = BackendManager.getInstance();
		solver = new Solver();
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
		solver.setDic(bm.getDictionary());
		solver.entireSequenceFinder(board);
		return solver.words;
	}

	/**
	 * Gets the solutions for the current board. Should be done concurrently with the player playing, etc
	 * so that things run smooth and fast.
	 */
	public void cacheAnswers(){
		this.answers = getAnswers(getBoard());
	}

	public String getPlayerName(){
        return playerName;
    }

    public void setPlayerName(String name){
		this.playerName = name;
	}

	public void setPlayerInput(List<String> playerInput){
	    playerWordInput = playerInput;
	}

	//gets the game board
    public String[][] getBoard() {
		return board.getGameBoard();			
	}

	//makes a new game-board
	public void genNewBoard(){
		board.generateGameBoard();
	}

	//gets the score for singlePlayer
	public int getScores(){
	    return BoggleUtils.calculateScore(this.answers, playerWordInput.stream().collect(Collectors.toSet()));
	}

	//Save for Sprint 2
	private int getScores(ArrayList<ArrayList<String>> Players){
	    int scores = 0;
	    return scores;
	}
}
