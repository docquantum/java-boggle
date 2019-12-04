package edu.unl.cse.csce361.boggle.logic;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class BoggleUtilsTester {

    @Test
    public void calculateScoreTest(){
        String[] solutions = new String[]{"hello", "one", "four", "fives"};
        String[] inputs = new String[]{"Hello", "one", "five"};
        int expected = 3;
        assertEquals(expected, BoggleUtils.calculateScore(Arrays.asList(solutions), Arrays.asList(inputs)));
    }
}
