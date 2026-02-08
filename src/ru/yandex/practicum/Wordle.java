package ru.yandex.practicum;

import ru.yandex.practicum.exeption.EmptyDictionaryExeption;
import ru.yandex.practicum.exeption.WordNotFoundInDictionary;

import java.io.IOException;

public class Wordle {

    public static void main(String[] args) {

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        Logger logger = new Logger();

        try {
            logger.createLogFile("log.txt");
            WordleDictionary dictionary = loader.readFile();
            WordleGame game = new WordleGame(dictionary, logger);
            game.play();
        } catch (IOException | EmptyDictionaryExeption e) {
            logger.log(e.getMessage());
        } finally {
            logger.close();
        }

    }

}
