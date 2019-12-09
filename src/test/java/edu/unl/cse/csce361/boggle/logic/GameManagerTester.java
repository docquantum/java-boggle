package edu.unl.cse.csce361.boggle.logic;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static junit.framework.TestCase.*;

public class GameManagerTester {

    public static GameManager gameManager;

    @Before
    public void setup(){
        gameManager = GameManager.getInstance();
    }

    @Test
    public void getAnswersTest(){
        String[][] gameboard = {{"A","B","A","C"}, {"A", "A", "S", "A"},{"A", "L", "A", "M"}, {"A", "A", "P", "A"}};
        List<String> expectedAnswers = new ArrayList<>();
        expectedAnswers.add("ABA");
        assertEquals(expectedAnswers, gameManager.getAnswers(gameboard));
    }
}
