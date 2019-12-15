package edu.unl.cse.csce361.boggle.logic;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameManager {
	private Solver solver;
	private static GameBoard board;
	private static BackendManager bm;
	private static GameManager uniqueInstance = null;
	private String gameBoard[][];
	private String playerName;
	private Player localPlayer;
	private List<String> playerWordInput;
	private Set<String> answers;
	private Set<Player> players = new HashSet<>();
	private int totalScore = 0;
	private boolean isMultiPlayer = false;
	private BooleanProperty gotAllScores;

	private GameManager(){
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

    public Set<Player> getPlayers(){
		return this.players;
	}

	public Player addPlayer(String playerName){
		Player player = new Player(playerName);
		this.players.add(player);
		return player;
	}

	public Player getLocalPlayer(){
		return this.localPlayer;
	}

	public void setLocalPlayer(Player player){
		this.localPlayer = player;
	}

	/**
	 * Adds a player to the list of players if the player doesn't already exist.
	 * If the player does exist, merge them.
	 * @param playerToAdd Player object
	 */
	public void addPlayerObject(Player playerToAdd){
		Player foundPlayer = players.stream()
				.filter(player -> player.getPlayerName().equals(playerToAdd.getPlayerName()))
				.findAny().orElse(null);
		if(foundPlayer != null){
			foundPlayer.setWords(playerToAdd.getWords());
		} else {
			players.add(playerToAdd);
		}
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

	public void setGameBoard(String[][] gameBoard){
		board.setGameBoard(gameBoard);
	}

	public void resetState(){
		this.setMultiPlayer(false);
		genNewBoard();
		this.setPlayerName(null);
		this.setTotalScore(0);
	}

	//gets the score for singlePlayer
	public int getSinglePlayerScore(){
		int score = BoggleUtils.calculateScore(this.answers, playerWordInput.stream().collect(Collectors.toSet()));
	    addToTotalScore(score);
	    return score;
	}

	public void calculateMultiPlayerScores(){
		BoggleUtils.removeDupilicates(players);
		if(this.answers == null){
			cacheAnswers();
		}
		BoggleUtils.calculateAllScores(this.answers, players);
	}

	public void setPlayers(Set<Player> players){
		this.players = players;
	}

	public List<String> getMultiPlayerScores(){
		return this.players.stream()
				.map(player -> "Name: " + player.getPlayerName() +
						"\tScore: " +player.getPlayerScore())
				.collect(Collectors.toList());
	}

	public void addToTotalScore(int score){
		this.totalScore += score;
	}

	public int getTotalScore(){
		return this.totalScore;
	}

	public void setTotalScore(int score){
		this.totalScore = score;
	}

	public void setMultiPlayer(boolean isMultiPlayer){
		if(isMultiPlayer){
			gotAllScores = new SimpleBooleanProperty(false);
		}
		this.isMultiPlayer = isMultiPlayer;
	}

	public BooleanProperty getGotAllScoresProperty(){
		return this.gotAllScores;
	}

	public boolean isMultiPlayer(){
		return this.isMultiPlayer;
	}
}
