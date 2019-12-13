package edu.unl.cse.csce361.boggle.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Player {
    private Set<String> words;
    private int playerScore;
    private String playerName;
    private int playerTotalScore;

    public Player(String name){
        this.words = new HashSet<>();
        this.playerScore = 0;
        this.playerName = name;
        this.playerTotalScore = 0;
    }

    public int getPlayerTotalScore() {
        return playerTotalScore;
    }
    
    public void addToPlayerTotalScore(int playerGameScore) {
        this.playerTotalScore = playerGameScore + playerTotalScore;
        this.playerScore = this.playerTotalScore;
    }

    public void setPlayerTotalScore(int totalScore){
        this.playerTotalScore = totalScore;
    }
    
    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    public Set<String> getWords() {
        return words;
    }
    public void setWords(Set<String> words) {
        this.words = words;
    }
    public int getPlayerScore() {
        return playerScore;
    }
    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

}
