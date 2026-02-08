package ru.yandex.practicum;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class WordleDictionary {

    final Set<Character> forbidden = new HashSet<>();
    final Set<Character> required = new HashSet<>();
    final StringBuilder sb = new StringBuilder(".....");

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
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
            return words.get(ThreadLocalRandom.current().nextInt(0, words.size()));
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
        return words.stream()
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
