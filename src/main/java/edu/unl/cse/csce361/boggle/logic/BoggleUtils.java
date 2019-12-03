package edu.unl.cse.csce361.boggle.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BoggleUtils {
    /**
     * Set of static utils to help with calculations
     */

    public static int calculateScore(List<String> solutions, List<String> inputs){
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

    public static List<String> removeDupilicates(List<String> ... lists){
        return null;
    }

}
