package ru.yandex.practicum.exeption;

public class WordNotFoundInDictionary extends RuntimeException {

    public WordNotFoundInDictionary(final String message) {
        super(message);
    }
}
