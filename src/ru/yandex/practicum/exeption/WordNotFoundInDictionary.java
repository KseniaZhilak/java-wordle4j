package ru.yandex.practicum.exeption;

public class WordNotFoundInDictionary extends Exception {

    public WordNotFoundInDictionary(final String message) {
        super(message);
    }
}
