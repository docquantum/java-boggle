package edu.unl.cse.csce361.boggle.logic;

import java.util.ArrayList;
import java.util.List;

public class Solver {

    public List<String> words = new ArrayList<String>();
    public List<String> dic = new ArrayList<String>();
    public Integer boardWidth;
    public Integer boardHeight;

    Solver() {
        boardWidth = 4;
        boardHeight = 4;
    }

    public void setDic(List<String> dic) {
        this.dic = dic;
    }

    public List<String> getWords() {
        return words;
    }

    //checks if sequence is in dictionary and not less than three letters
    boolean isInDictionary(String sequence) {
        if (sequence.length() < 3) {
            return false;
        }
        for (int i = 0; i < this.dic.size(); i++) {
            if (sequence.equalsIgnoreCase(this.dic.get(i))) {
                return true;
            }
        }
        return false;
    }

    //appends all valid sequences that start with char at index [i][j] on board to words arraylist
    void localSequenceFinder(String board[][], boolean passed[][], int i, int j, String sequence) {
        //have already passed tree for this index
        passed[i][j] = true;
        sequence = sequence + board[i][j];
        //append valid sequences that aren't duplicates to words arraylist
        if (isInDictionary(sequence) && !this.words.contains(sequence)) {
            this.words.add(sequence);
        }
        //navigating the adjacent cells relative to the specified char at index [i][j] (changes with each recursive call)
        for (int row = i - 1; row <= i + 1 && row < this.boardHeight; row++) {
            for (int col = j - 1; col <= j + 1 && col < this.boardWidth; col++) {
                if (row >= 0 && col >= 0 && !passed[row][col]) {
                    //recursion for depth-first search of words starting with a specified char index within board
                    localSequenceFinder(board, passed, row, col, sequence);
                }
            }
        }
        //resets as false so as to allow for another pass during another iteration of testing different permutations
        passed[i][j] = false;
    }

    //appends all valid sequences, accounting for all sequences with different starting letters, to words arraylist
    void entireSequenceFinder(String board[][]) {
        //initialize passed board to null or false
        boolean passed[][] = new boolean[this.boardHeight][this.boardWidth];
        //accounts for every letter on board as starting letter and then performs depth-first search
        String sequence = "";
        for (int i = 0; i < this.boardHeight; i++) {
            for (int j = 0; j < this.boardWidth; j++) {
                localSequenceFinder(board, passed, i, j, sequence);
            }
        }
    }
}
