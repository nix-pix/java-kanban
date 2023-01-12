package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.net.KVTaskClient;

public class HttpTasksManager extends FileBackedTasksManager {
    private final String url; // http://localhost:8078/
    private KVTaskClient kvTaskClient;

    public HttpTasksManager(String url) {
        super(null);
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
    }
}