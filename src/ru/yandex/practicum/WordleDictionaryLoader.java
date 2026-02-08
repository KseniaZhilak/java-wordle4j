package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class WordleDictionaryLoader {

    public WordleDictionary readFile() throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("words_ru.txt", UTF_8))) {
            while (br.ready()) {
                String line = br.readLine();
                if (!line.isBlank() && line.length() == 5) {
                    words.add(line.toLowerCase());
                }
            }
        }

        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).contains("ё")) {
                words.set(i, words.get(i).replace("ё", "е"));
            }
        }

        return new WordleDictionary(words);
    }

}