package ru.yandex.practicum;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class Logger {

    private PrintWriter writer;

    public void createLogFile(String fileName) throws IOException {
        Path logPath = Paths.get(fileName);

        writer = new PrintWriter(Files.newOutputStream(logPath, CREATE, APPEND), true, UTF_8);
    }

    public void log(String message) {
        if (writer != null) {
            writer.println(LocalDateTime.now() + ": " + message);
        }
    }

    public void close() {
        if (writer != null) {
            writer.close();
        }
    }


}
