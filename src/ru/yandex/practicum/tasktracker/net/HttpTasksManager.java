package ru.yandex.practicum.tasktracker.net;

import ru.yandex.practicum.tasktracker.service.FileBackedTasksManager;

import java.nio.file.Path;

public class HttpTasksManager extends FileBackedTasksManager {

    public HttpTasksManager(Path path) {
        super(path);
    }
}
