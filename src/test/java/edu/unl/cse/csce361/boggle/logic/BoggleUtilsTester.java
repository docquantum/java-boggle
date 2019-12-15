package edu.unl.cse.csce361.boggle.logic;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoggleUtilsTester {

    @Test
    public void calculateScoreTest(){
        String[] solutions = new String[]{"hello", "one", "four", "fives"};
        String[] inputs = new String[]{"Hello", "one", "five"};
        int expected = 3;
        assertEquals(expected,
                BoggleUtils.calculateScore(
                        Arrays.stream(solutions).collect(Collectors.toSet()),
                        Arrays.stream(inputs).collect(Collectors.toSet())
                )
        );
    }

    @Test
    public void removeDuplicatesTest(){
        Set<Player> players = new HashSet<>();
        Player player1 = new Player("Test");
        Player player2 = new Player("Test2");
        Set<String> set1 = new HashSet<>();
        Set<String> set2 = new HashSet<>();
        set1.add("hello");
        set1.add("not");
        set2.add("hello");
        set2.add("Duplicate");
        player1.setWords(set1);
        player2.setWords(set2);
        players.add(player1);
        players.add(player2);
        BoggleUtils.removeDupilicates(players);
        player1.getWords().retainAll(player2.getWords());
        player2.getWords().retainAll(player1.getWords());
        assertTrue(player1.getWords().equals(player2.getWords()));
    }

    @Test
    public void calculateAllScoresTest(){
        Set<Player> players = new HashSet<>();
        players.add(new Player("Test"));
        players.add(new Player("Test2"));
        String[] solutions = new String[]{"hello", "one", "four", "fives"};
        String[] inputs = new String[]{"Hello", "one", "five"};
        int expected = 3;
        assertEquals(expected,
                BoggleUtils.calculateScore(
                        Arrays.stream(solutions).collect(Collectors.toSet()),
                        Arrays.stream(inputs).collect(Collectors.toSet())
                )
        );
    }
}
