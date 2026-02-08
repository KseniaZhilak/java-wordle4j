package ru.yandex.practicum;

import ru.yandex.practicum.exeption.EmptyDictionaryExeption;
import ru.yandex.practicum.exeption.WordNotFoundInDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class WordleGame {

    private final Logger logger;

    private static final int COUNT_MAX = 6;
    private final WordleDictionary dictionary;

    private final Scanner scanner = new Scanner(System.in);
    private final List<String> lastPrompts = new ArrayList<>();
    private String answer;
    private String prompt;

    public WordleGame(WordleDictionary dictionary, Logger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
    }

    public void play() throws EmptyDictionaryExeption {
        if (dictionary.getWords().isEmpty()) throw new EmptyDictionaryExeption("Загружен пустой словарь");
        answer = dictionary.getWords().get((ThreadLocalRandom.current().nextInt(dictionary.getWords().size())));
        System.out.println("Угадай слово: ");

        int steps = 0;
        while (steps < COUNT_MAX) {
            prompt = getUserInput();
            if (isWin()) return;
            System.out.println(dictionary.getMask(prompt, answer));
            steps++;
        }
        System.out.println("Вы проиграли!");
        System.out.println("Было загадано слово: " + answer);

    }

    public String getUserInput() {
        boolean isWord = false;
        while (!isWord) {
            prompt = scanner.nextLine();
            if (!prompt.isEmpty()) {
                try {
                    validationWord(prompt);
                    lastPrompts.add(prompt);
                    isWord = true;
                } catch (WordNotFoundInDictionary e) {
                    System.out.println(e.getMessage());
                    logger.log(e.getMessage());
                }
            } else {
                prompt = dictionary.getPrompt(lastPrompts, answer);
                lastPrompts.add(prompt);
                System.out.println(prompt);
                isWord = true;
            }
        }
        return prompt;
    }

    public void validationWord(String word) throws WordNotFoundInDictionary {
        if (!dictionary.getWords().contains(word) || word.length() != 5) {
            throw new WordNotFoundInDictionary("Слова нет в словаре или оно меньше пяти символов");
        }

    }

    public boolean isWin() {
        if (answer.equals(prompt)) {
            System.out.println("Вы выиграли!");
            return true;
        }
        return false;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

}
