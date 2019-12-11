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
        String[][] gameboard = {{"W","E","Y","I"}, {"B", "T", "U", "A"},{"S", "I", "Z", "O"}, {"T", "T", "H", "N"}};
        Set<String> expectedAnswers = new HashSet<>();

        expectedAnswers.add("tye");
        expectedAnswers.add("tua");
        expectedAnswers.add("nozi");
        expectedAnswers.add("tue");
        expectedAnswers.add("stue");
        expectedAnswers.add("aute");
        expectedAnswers.add("tui");
        expectedAnswers.add("ist");
        expectedAnswers.add("bit");
        expectedAnswers.add("tib");
        expectedAnswers.add("stib");
        expectedAnswers.add("bis");
        expectedAnswers.add("bet");
        expectedAnswers.add("tite");
        expectedAnswers.add("biz");
        expectedAnswers.add("tibey");
        expectedAnswers.add("hit");
        expectedAnswers.add("hist");
        expectedAnswers.add("stite");
        expectedAnswers.add("his");
        expectedAnswers.add("stith");
        expectedAnswers.add("bey");
        expectedAnswers.add("tit");
        expectedAnswers.add("itza");
        expectedAnswers.add("tew");
        expectedAnswers.add("ute");
        expectedAnswers.add("this");
        expectedAnswers.add("bitt");
        expectedAnswers.add("its");
        expectedAnswers.add("etui");
        expectedAnswers.add("aye");
        expectedAnswers.add("stu");
        expectedAnswers.add("sty");
        expectedAnswers.add("ayu");
        expectedAnswers.add("outsit");
        expectedAnswers.add("noa");
        expectedAnswers.add("hon");
        expectedAnswers.add("sib");
        expectedAnswers.add("zoa");
        expectedAnswers.add("out");
        expectedAnswers.add("etua");
        expectedAnswers.add("tst");
        expectedAnswers.add("uayeb");
        expectedAnswers.add("web");
        expectedAnswers.add("nou");
        expectedAnswers.add("iao");
        expectedAnswers.add("bite");
        expectedAnswers.add("azo");
        expectedAnswers.add("tuza");
        expectedAnswers.add("sit");
        expectedAnswers.add("stey");
        expectedAnswers.add("stew");
        expectedAnswers.add("thon");
        expectedAnswers.add("yao");
        expectedAnswers.add("wet");
        expectedAnswers.add("autist");
        expectedAnswers.add("yet");
        expectedAnswers.add("htt");
        expectedAnswers.add("wey");
        expectedAnswers.add("yew");
        expectedAnswers.add("tiza");
        expectedAnswers.add("zibet");
        expectedAnswers.add("thou");
        expectedAnswers.add("betis");
        expectedAnswers.add("tibet");
        expectedAnswers.add("site");
        expectedAnswers.add("onza");
        expectedAnswers.add("azon");
        expectedAnswers.add("tho");
        expectedAnswers.add("yuit");
        expectedAnswers.add("sith");


        assertEquals(expectedAnswers, gameManager.getAnswers(gameboard));
    }
}
