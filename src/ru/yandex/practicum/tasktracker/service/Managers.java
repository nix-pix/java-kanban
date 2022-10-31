package ru.yandex.practicum.tasktracker.service;

public final class Managers {

    public static TaskManager getDefault() {
        TaskManager taskManager = new InMemoryTaskManager();
        return taskManager;
    }

    public static void getDefaultHistory() {
    }
}