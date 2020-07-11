package com.mateusz.jakuszko.roomforyou.opencagegeocoderapi;

import org.springframework.stereotype.Component;

@Component
public class PolishLettersChanger {
    public String changePolishLettersToEnglish(String word) {
        StringBuilder resultWord = new StringBuilder();
        char[] wordArray = word.toCharArray();
        for (char letter : wordArray) {
            switch (letter) {
                case 'ą':
                    resultWord.append('a');
                    break;
                case 'ć':
                    resultWord.append('c');
                    break;
                case 'ę':
                    resultWord.append('e');
                    break;
                case 'ł':
                    resultWord.append('l');
                    break;
                case 'ń':
                    resultWord.append('n');
                    break;
                case 'ó':
                    resultWord.append('o');
                    break;
                case 'ś':
                    resultWord.append('s');
                    break;
                case 'ź':
                    resultWord.append('z');
                    break;
                case 'ż':
                    resultWord.append('z');
                    break;
                case 'Ą':
                    resultWord.append('A');
                    break;
                case 'Ć':
                    resultWord.append('C');
                    break;
                case 'Ę':
                    resultWord.append('E');
                    break;
                case 'Ł':
                    resultWord.append('L');
                    break;
                case 'Ń':
                    resultWord.append('N');
                    break;
                case 'Ó':
                    resultWord.append('O');
                    break;
                case 'Ś':
                    resultWord.append('S');
                    break;
                case 'Ź':
                    resultWord.append('Z');
                    break;
                case 'Ż':
                    resultWord.append('Z');
                    break;
                case ' ':
                    resultWord.append("%20");
                    break;
                default:
                    resultWord.append(letter);
                    break;
            }
        }
        return resultWord.toString();
    }
}
