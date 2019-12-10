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
        expectedAnswers.add("aue");
        expectedAnswers.add("autist");
        expectedAnswers.add("aye");
        expectedAnswers.add("ayu");
        expectedAnswers.add("bet");
        expectedAnswers.add("yuit");
        expectedAnswers.add("betis");
        expectedAnswers.add("bis");
        expectedAnswers.add("bite");
        expectedAnswers.add("zoa");
        expectedAnswers.add("his");
        expectedAnswers.add("noa");
        expectedAnswers.add("stu");
        expectedAnswers.add("hout");
        expectedAnswers.add("tibet");
        expectedAnswers.add("noh");
        expectedAnswers.add("sit");
        expectedAnswers.add("site");
        expectedAnswers.add("etua");
        expectedAnswers.add("sitz");
        expectedAnswers.add("sty");
        expectedAnswers.add("thibet");
        expectedAnswers.add("onza");
        expectedAnswers.add("tis");
        expectedAnswers.add("tst");
        expectedAnswers.add("uayeb");
        expectedAnswers.add("tye");
        expectedAnswers.add("utis");
        expectedAnswers.add("wey");
        expectedAnswers.add("itza");
        expectedAnswers.add("nou");
        expectedAnswers.add("yetis");
        expectedAnswers.add("zho");
        expectedAnswers.add("zit");
        expectedAnswers.add("azo");
        expectedAnswers.add("bets");
        expectedAnswers.add("stith");
        expectedAnswers.add("bist");
        expectedAnswers.add("biz");
        expectedAnswers.add("ewt");
        expectedAnswers.add("hist");
        expectedAnswers.add("stib");
        expectedAnswers.add("hoa");
        expectedAnswers.add("nout");
        expectedAnswers.add("outsit");
        expectedAnswers.add("tib");
        expectedAnswers.add("stew");
        expectedAnswers.add("stye");
        expectedAnswers.add("thon");
        expectedAnswers.add("tit");
        expectedAnswers.add("tibey");
        expectedAnswers.add("stite");
        expectedAnswers.add("tui");
        expectedAnswers.add("uey");
        expectedAnswers.add("uts");
        expectedAnswers.add("wet");
        expectedAnswers.add("yet");
        expectedAnswers.add("ist");
        expectedAnswers.add("yew");
        expectedAnswers.add("zibet");
        expectedAnswers.add("zite");
        expectedAnswers.add("azon");
        expectedAnswers.add("bey");
        expectedAnswers.add("aute");
        expectedAnswers.add("bit");
        expectedAnswers.add("bitt");
        expectedAnswers.add("etui");
        expectedAnswers.add("hit");
        expectedAnswers.add("stue");
        expectedAnswers.add("hon");
        expectedAnswers.add("its");
        expectedAnswers.add("iao");
        expectedAnswers.add("out");
        expectedAnswers.add("tuza");
        expectedAnswers.add("htt");
        expectedAnswers.add("yao");
        expectedAnswers.add("tiza");
        expectedAnswers.add("sib");
        expectedAnswers.add("sith");
        expectedAnswers.add("tua");
        expectedAnswers.add("stey");
        expectedAnswers.add("tew");
        expectedAnswers.add("this");
        expectedAnswers.add("tite");
        expectedAnswers.add("nozi");
        expectedAnswers.add("ute");
        expectedAnswers.add("web");
        expectedAnswers.add("tue");
        expectedAnswers.add("thou");
        expectedAnswers.add("yeti");
        expectedAnswers.add("tho");
        expectedAnswers.add("yutz");


        assertEquals(expectedAnswers, gameManager.getAnswers(gameboard));
    }
}
