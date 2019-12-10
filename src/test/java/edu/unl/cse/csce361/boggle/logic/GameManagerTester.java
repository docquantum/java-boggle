package edu.unl.cse.csce361.boggle.logic;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
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
        String[][] gameboard = {{"D","A","D","D"}, {"A", "A", "A", "A"},{"A", "A", "A", "A"}, {"A", "A", "A", "A"}};
        Set<String> expectedAnswers = new HashSet<>();
        expectedAnswers.add("dad");
        expectedAnswers.add("dada");
        assertEquals(expectedAnswers, gameManager.getAnswers(gameboard));
    }
}
