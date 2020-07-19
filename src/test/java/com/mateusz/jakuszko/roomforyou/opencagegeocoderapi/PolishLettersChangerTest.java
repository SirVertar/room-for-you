package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PolishLettersChangerTest {

    @Autowired
    PolishLettersChanger polishLettersChanger;

    @Test
    public void whenGiveWordsWithPolishLettersShouldReturnEquivalentInWordInEnglishLetters() {
        //Given
        String word1 = "ąrewćtręłńó";
        String word2 = "śźżdswĄĆĘfdf";
        String word3 = "fdsDĘŁŃfdÓ";
        String word4 = "Śf32Źfd3ŻA";
        //When
        String expectedWord1 = "arewctrelno";
        String expectedWord2 = "szzdswACEfdf";
        String expectedWord3 = "fdsDELNfdO";
        String expectedWord4 = "Sf32Zfd3ZA";
        String changedWord1 = polishLettersChanger.changePolishLettersToEnglish(word1);
        String changedWord2 = polishLettersChanger.changePolishLettersToEnglish(word2);
        String changedWord3 = polishLettersChanger.changePolishLettersToEnglish(word3);
        String changedWord4 = polishLettersChanger.changePolishLettersToEnglish(word4);
        //Then
        assertEquals(expectedWord1, changedWord1);
        assertEquals(expectedWord2, changedWord2);
        assertEquals(expectedWord3, changedWord3);
        assertEquals(expectedWord4, changedWord4);
    }
}
