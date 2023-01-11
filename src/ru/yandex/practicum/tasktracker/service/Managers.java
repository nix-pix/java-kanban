package ru.yandex.practicum.tasktracker.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Managers {

    private Managers() {
    }

//    public static TaskManager getDefault() { //с ТЗ-8
//        return new HttpTaskServer();
//    }

    public static TaskManager getDefault() { //до ТЗ-8
        return new InMemoryTaskManager();

    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBacked() {
        Path path = Paths.get("." + File.separator + "resources" + File.separator + "memoryFileHttp.csv");
        return new FileBackedTasksManager(path);
    }
}