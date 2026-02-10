package ru.yandex.practicum;

import ru.yandex.practicum.exeption.EmptyDictionaryExeption;

import java.io.IOException;

public class Wordle {

    public static void main(String[] args) {

        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        try (Logger logger = new Logger()) {
            logger.createLogFile("log.txt");

            try {
                WordleDictionary dictionary = loader.readFile();
                WordleGame game = new WordleGame(dictionary, logger);
                game.play();
            } catch (IOException | EmptyDictionaryExeption e) {
                logger.log(e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Не удалось создать лог-файл: " + e.getMessage());
        }
    }

}
