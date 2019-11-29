package edu.unl.cse.csce361.boggle.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class GameBoardTester {
    public static GameBoard gameBoard;

    @Before
    public void setup(){
        gameBoard = GameBoard.getInstance();
    }

    @Test
    public void gameBoardValidateDiceUsed(){
        List<String> diceSides = Arrays.stream(GameBoard.getDice())
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        assertTrue(
                Arrays.stream(gameBoard.getGameBoard())
                        .flatMap(Arrays::stream)
                        .allMatch(s -> diceSides.contains(s))
        );

    }

    @Test
    public void generateGameBoardTest(){
        String[][] oldGameBoard = gameBoard.getGameBoard();
        gameBoard.generateGameBoard();
        assertFalse(oldGameBoard.equals(gameBoard.getGameBoard()));
    }
}
