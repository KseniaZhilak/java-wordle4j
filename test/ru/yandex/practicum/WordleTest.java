package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exeption.EmptyDictionaryExeption;
import ru.yandex.practicum.exeption.WordNotFoundInDictionary;

import java.io.*;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    static WordleDictionary dictionary;

    @Test
    void checkWordMask(){
        String answer = "казак";
        String prompt = "камаз";
        String mask = dictionary.getMask(prompt, answer);
        assertEquals("++-+^", mask);

    }

    @Test
    void checkGetPrompt() {
        String pattern = "\\+\\+.\\+.";

        List<String> lastPrompts = List.of("камаз");
        String answer = "казак";

        String prompt = dictionary.getPrompt(lastPrompts, answer);

        String maskCurrentPrompts = dictionary.getMask(prompt, answer);
        assertTrue(maskCurrentPrompts.matches(pattern));

    }

    @Test
    void checkEmptyLastPrompts() {
        List<String> lastPrompts = Collections.emptyList();

        String result = dictionary.getPrompt(lastPrompts, "answer");
        assertTrue(dictionary.getWords().contains(result));
    }

    @Test
    void checkWinWithFirstTry() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Logger logger = new Logger();
        WordleGame game = new WordleGame(dictionary, logger);
        game.setAnswer("казак");
        game.setPrompt("казак");

        assertTrue(game.isWin());

        System.setOut(System.out);
        assertTrue(outContent.toString().contains("Вы выиграли!"));

    }

    @Test
    void checkEmptyDictionary() {
        Logger logger = new Logger();

        WordleDictionary emptyDictionary = new WordleDictionary(Collections.emptyList());
        WordleGame game = new WordleGame(emptyDictionary, logger);
        try{
            game.play();
        } catch (EmptyDictionaryExeption e) {
            logger.log(e.getMessage());
            assertEquals("Загружен пустой словарь", e.getMessage());
        }

    }

    @Test
    void checkUserInputError() {
        Logger logger = new Logger();
        WordleGame game = new WordleGame(dictionary, logger);
        try{
            game.validationWord("test");
        } catch (WordNotFoundInDictionary e) {
            logger.log(e.getMessage());
            assertEquals("Слова нет в словаре или оно меньше пяти символов", e.getMessage());
        }

    }

    @BeforeAll
    static void createDictionary() {
        dictionary = new WordleDictionary(List.of("казак", "гонец", "груша", "камаз", "джава", "питон"));
    }

}
