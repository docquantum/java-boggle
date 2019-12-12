package edu.unl.cse.csce361.boggle.logic;

import java.awt.List;

public class Player {
    private List words = new List();
    private int playerScore;
    private String playerName;
    private int playerTotalScore;
    
    
    
    public int getPlayerTotalScore() {
        return playerTotalScore;
    }
    
    public void setPlayerTotalScore(int playerGameScore) {
        this.playerTotalScore = playerGameScore + playerTotalScore;
        this.playerScore = this.playerTotalScore;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public List getWords() {
        return words;
    }
    public void setWords(List words) {
        this.words = words;
    }
    public int getPlayerScore() {
        return playerScore;
    }
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
    
    
    
    
    
    
}
