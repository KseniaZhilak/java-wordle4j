package ru.yandex.practicum;

import ru.yandex.practicum.exeption.EmptyDictionaryExeption;
import ru.yandex.practicum.exeption.WordNotFoundInDictionary;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class WordleGame {

    private static final int COUNT_MAX = 6;

    private final Set<Character> forbidden = new HashSet<>();
    private final Set<Character> required = new HashSet<>();
    private final StringBuilder sb = new StringBuilder(".....");
    private final Logger logger;
    private final WordleDictionary dictionary;
    private final Scanner scanner = new Scanner(System.in);
    private final List<String> lastPrompts = new ArrayList<>();
    private String answer;

    public WordleGame(WordleDictionary dictionary, Logger logger) {
        this.dictionary = dictionary;
        this.logger = logger;
    }

    public void play() {
        if (dictionary.getWords().isEmpty()) throw new EmptyDictionaryExeption("Загружен пустой словарь");
        answer = dictionary.getWords().get((ThreadLocalRandom.current().nextInt(dictionary.getWords().size())));
        System.out.println("Угадай слово: ");

        int steps = 0;
        while (steps < COUNT_MAX) {
            String prompt = getUserInput();
            if (isWin(answer, prompt)) return;
            System.out.println(getMask(prompt, answer));
            steps++;
        }
        System.out.println("Вы проиграли!");
        System.out.println("Было загадано слово: " + answer);

    }

    public String getUserInput() {
        String prompt = "";
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
                prompt = getPrompt(lastPrompts, answer);
                lastPrompts.add(prompt);
                System.out.println(prompt);
                isWord = true;
            }
        }
        return prompt;
    }

    public void validationWord(String word) {
        if (!dictionary.getWords().contains(word) || word.length() != 5) {
            throw new WordNotFoundInDictionary("Слова нет в словаре или оно меньше пяти символов");
        }

    }

    public boolean isWin(String answer, String prompt) {
        if (answer.equals(prompt)) {
            System.out.println("Вы выиграли!");
            return true;
        }
        return false;
    }

    public String getMask(String prompt, String word) {
        StringBuilder masked = new StringBuilder();
        for (int i = 0; i < prompt.length(); i++) {
            if (prompt.charAt(i) == word.charAt(i)) {
                masked.append("+");
            } else if (word.indexOf(prompt.charAt(i)) != -1) {
                masked.append("^");
            } else {
                masked.append("-");
            }

        }
        return masked.toString();
    }

    public String getPrompt(List<String> lastPrompts, String answer) {
        if (lastPrompts.isEmpty()) {
            return dictionary.getWords().get(ThreadLocalRandom.current().nextInt(0, dictionary.getWords().size()));
        }

        String curr = lastPrompts.getLast();
        String mask = getMask(curr, answer);

        for (int j = 0; j < curr.length(); j++) {
            char c = curr.charAt(j);
            char m = mask.charAt(j);

            if (m == '+') {
                sb.replace(j, j + 1, String.valueOf(c));
            } else if (m == '^') {
                required.add(c);
            } else {
                forbidden.add(c);
            }
        }

        return pullTogether(required, forbidden, lastPrompts);

    }

    public String pullTogether(Set<Character> required, Set<Character> forbidden, List<String> lastPrompts) {
        return dictionary.getWords().stream()
                .filter(word -> word.matches(sb.toString()))
                .filter(word -> {
                    for (Character c : required) {
                        if (!word.contains(String.valueOf(c))) {
                            return false;
                        }
                    }
                    return true;
                })
                .filter(word -> {
                    for (Character c : forbidden) {
                        if (word.contains(String.valueOf(c))) {
                            return false;
                        }
                    }
                    return true;
                })
                .filter(word -> !lastPrompts.contains(word))
                .findFirst().orElseThrow();

    }

}
