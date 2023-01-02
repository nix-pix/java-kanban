package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.FileBackedTasksManager;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestMain {
    public static void main(String[] args) {
        Path path = Paths.get("." + File.separator + "resources" + File.separator + "testFile.csv");

        TaskManager fileTaskManager = new FileBackedTasksManager(path);

        fileTaskManager.createTask("Задача 1", "id-1");
        fileTaskManager.createEpic("Эпик 1", "id-2");
        List<Task> tasks = fileTaskManager.getAllTasks();
        System.out.println(tasks);
        List<Epic> epics = fileTaskManager.getAllEpics();
        System.out.println(epics);
        Task task = tasks.get(0);
        System.out.println(task);
        Epic epic = epics.get(0);
        System.out.println(epic);
        System.out.println(fileTaskManager.getHistory());
        TaskManager loadedFileTaskManager = FileBackedTasksManager.loadFromFile(path);
        List<Task> loadedTasks = loadedFileTaskManager.getAllTasks();
        System.out.println(loadedTasks);
        List<Epic> loadedEpics = loadedFileTaskManager.getAllEpics();
        System.out.println(loadedEpics);
        Task loadedTask = loadedTasks.get(0);
        System.out.println(loadedTask);
        Epic loadedEpic = loadedEpics.get(0);
        System.out.println(loadedEpic);
        System.out.println(loadedFileTaskManager.getHistory());
    }
}