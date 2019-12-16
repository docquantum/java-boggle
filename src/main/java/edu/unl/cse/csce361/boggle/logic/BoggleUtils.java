package edu.unl.cse.csce361.boggle.logic;

import java.util.*;
import java.util.stream.Collectors;

public class BoggleUtils {
    /**
     * Set of static utils to help with calculations
     */

    public static int calculateScore(Set<String> solutions, Set<String> inputs){
        int score = 0;

        List<Integer> lengths = inputs.stream()
                .map(s -> s.toLowerCase())
                .filter(s -> solutions.contains(s))
                .map(s -> s.length())
                .collect(Collectors.toList());

        for (Integer length : lengths) {
            if(length <=4){
                score += 1;
            } else if(length == 5){
                score += 2;
            } else if(length == 6){
                score += 3;
            } else if(length == 7){
                score += 5;
            } else if(length >= 8){
                score += 11;
            }
        }

        return score;
    }

    public static void calculateAllScores(Set<String> solutions, Set<Player> players){
        removeDupilicates(players);
        players.forEach(p -> p.addToPlayerScore(calculateScore(solutions, p.getWords())));
    }

    public synchronized static void removeDupilicates(Set<Player> players){
        List<String> allWords = players.stream()
                .map(player -> player.getWords())
                .map(set -> set.stream().collect(Collectors.toList()))
                .flatMap(l -> l.stream())
                .collect(Collectors.toList());

        Set<String> unique = new HashSet<>();

        Set<String> duplicates = allWords.stream().filter(s -> !unique.add(s))
                .collect(Collectors.toSet());

        for (Player p: players) {
            p.getWords().removeAll(duplicates);
        }
    }
}
