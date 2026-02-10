package ru.yandex.practicum.exeption;

public class EmptyDictionaryExeption extends RuntimeException {

    public EmptyDictionaryExeption(final String message) {
        super(message);
    }

}
