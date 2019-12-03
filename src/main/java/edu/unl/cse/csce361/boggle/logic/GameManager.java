package edu.unl.cse.csce361.boggle.logic;

import java.awt.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameManager {

	Solver answer;
	GameBoard board;
	String gameBoard[][];
	Timer time = new Timer();
	TimerTask task = new TimerTask() {

		@Override
		public void run() {
			
			
		}
		
	};
	Solver getAnswers(String board[][]) {
		answer.entireSequenceFinder(board);		
		return answer;
	}
	//gets the game board
	String[][] getBoard() {
		return board.getGameBoard();			
	}
	//makes a new game-board
	String[][] getNewBoard(){
		board.generateGameBoard();
		return board.getGameBoard();
	}
	//gets the score for singlePlayer
	int getScores(List singlePlayer1){
		
		
		int scores = 0;
		
		
		
		
		return scores;
	}
	//Save for Sprint 2
	int getScores(ArrayList<ArrayList<String>> Players){
		
		int scores = 0;
		
		
		return scores;
	}
	
	//gets timer
	Timer getTimer() {
		return time;
	}
	//Sets a new timer
	Timer resetTimer() {
		time = new Timer();
		return time;
	}
	
}
