package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exeption.EmptyDictionaryExeption;
import ru.yandex.practicum.exeption.WordNotFoundInDictionary;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordleTest {

    static WordleGame game;
    static Logger logger;
    static WordleDictionary dictionary;

    @Test
    void checkWordMask() {
        String answer = "казак";
        String prompt = "камаз";
        String mask = game.getMask(prompt, answer);
        assertEquals("++-+^", mask);

    }

    @Test
    void checkGetPrompt() {
        String pattern = "\\+\\+.\\+.";

        List<String> lastPrompts = List.of("камаз");
        String answer = "казак";

        String prompt = game.getPrompt(lastPrompts, answer);

        String maskCurrentPrompts = game.getMask(prompt, answer);
        assertTrue(maskCurrentPrompts.matches(pattern));

    }

    @Test
    void checkEmptyLastPrompts() {
        List<String> lastPrompts = Collections.emptyList();

        String result = game.getPrompt(lastPrompts, "answer");
        assertTrue(dictionary.getWords().contains(result));
    }

    @Test
    void checkWinWithFirstTry() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        dictionary = new WordleDictionary(List.of("казак"));
        game = new WordleGame(dictionary, logger);

        assertTrue(game.isWin("казак", "казак"));

        System.setOut(System.out);
        assertTrue(outContent.toString().contains("Вы выиграли!"));

    }

    @Test
    void checkEmptyDictionary() {
        WordleDictionary emptyDictionary = new WordleDictionary(Collections.emptyList());
        game = new WordleGame(emptyDictionary, logger);
        try {
            game.play();
        } catch (EmptyDictionaryExeption e) {
            logger.log(e.getMessage());
            assertEquals("Загружен пустой словарь", e.getMessage());
        }

    }

    @Test
    void checkUserInputError() {
        try {
            game.validationWord("test");
        } catch (WordNotFoundInDictionary e) {
            logger.log(e.getMessage());
            assertEquals("Слова нет в словаре или оно меньше пяти символов", e.getMessage());
        }

    }

    @BeforeAll
    static void createDictionary() {
        dictionary = new WordleDictionary(List.of("казак", "гонец", "груша", "камаз", "джава", "питон"));
        logger = new Logger();
        game = new WordleGame(dictionary, logger);
    }

}
