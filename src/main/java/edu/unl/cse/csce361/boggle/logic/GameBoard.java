package edu.unl.cse.csce361.boggle.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBoard {
    private String[][] gameBoard;
    private static GameBoard instance = null;
    /**
     * The dice as a string matrix
     */
    private static final String[][] dice =
        {
            {"R", "I", "F", "O", "B", "X"},
            {"I", "F", "E", "H", "E", "Y"},
            {"D", "E", "N", "O", "W", "S"},
            {"U", "T", "O", "K", "N", "D"},
            {"H", "M", "S", "R", "A", "O"},
            {"L", "U", "P", "E", "T", "S"},
            {"A", "C", "I", "T", "O", "A"},
            {"Y", "L", "G", "K", "U", "E"},
            {"Qu","B", "M", "J", "O", "A"},
            {"E", "H", "I", "S", "P", "N"},
            {"V", "E", "T", "I", "G", "N"},
            {"B", "A", "L", "I", "Y", "T"},
            {"E", "Z", "A", "V", "N", "D"},
            {"R", "A", "L", "E", "S", "C"},
            {"U", "W", "I", "L", "R", "G"},
            {"P", "A", "C", "E", "M", "D"}
        };

    /**
     * returns the dice used for boggle.
     * @return
     */
    public static String[][] getDice(){
        return dice;
    }

    /**
     * Returns an instance of the GameBoard class
     * that contains all methods necessary to generate
     * new boggle game boards.
     * @return
     */
    public static GameBoard getInstance() {
        if(instance == null){
            instance = new GameBoard();
        }
        return instance;
    }

    private GameBoard(){
        generateGameBoard();
    }

    /**
     * Returns the game board as a string matrix
     * (needed for "Qu" as a die side)
     * @return
     */
    public String[][] getGameBoard() {
        return gameBoard;
    }

    /**
     * Allows for the game board to be set to a specific
     * game board, mostly for testing or future features.
     * @param gameBoard
     */
    public void setGameBoard(String[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Generates the game board using a random order of the dice
     * and a random side of each die. Automatically sets the class's
     * internal game board to the newly generated game board.
     */
    public void generateGameBoard(){
        this.gameBoard = new String[4][4];
        List<Integer> diceOrder = Arrays.asList(new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        Collections.shuffle(diceOrder);
        Random randSide = new Random();

        int die = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                this.gameBoard[i][j] = dice[diceOrder.get(die)][randSide.nextInt(dice[0].length)];
                die++;
            }
        }
    }

}
