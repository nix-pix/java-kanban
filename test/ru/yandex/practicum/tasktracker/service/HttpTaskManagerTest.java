package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.net.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest {
    private static KVServer server;
    private static TaskManager httpTaskManager;
    private final String url = "http://localhost:8078/";

    @BeforeEach
    void beforeEachInHTM() throws IOException {
        server = new KVServer();
        server.start();
        httpTaskManager = Managers.getDefault();
    }

    @AfterEach
    void afterEachInHTM() {
        server.stop();
    }

    @Test
    void loadFromServerTest() {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        httpTaskManager.createTask("Задача 1", "Задача с id-1");
        httpTaskManager.setTaskStartTimeAndDuration(
                1,
                LocalDateTime.of(2022, 12, 10, 16, 0),
                45);
        httpTaskManager.createTask("Задача 2", "Задача с id-2");
        httpTaskManager.createEpic("Эпик 1", "Эпик с id-3");
        httpTaskManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        httpTaskManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
        httpTaskManager.setSubtaskStartTimeAndDuration(
                5,
                LocalDateTime.of(2022, 12, 11, 14, 0),
                10);
        Task task = httpTaskManager.getTaskById(1);
        Epic epic = httpTaskManager.getEpicById(3);
        Subtask subtask = httpTaskManager.getSubtaskById(5);
        List<Task> tasks = httpTaskManager.getAllTasks();
        List<Epic> epics = httpTaskManager.getAllEpics();
        List<Subtask> subtasks = httpTaskManager.getAllSubtasks();
        List<Task> history = httpTaskManager.getHistory();

        HttpTaskManager loadedHttpTaskManager = httpTaskManager.loadFromServer(url);
        List<Task> loadedTasks = loadedHttpTaskManager.getAllTasks();
        List<Epic> loadedEpics = loadedHttpTaskManager.getAllEpics();
        List<Subtask> loadedSubtasks = loadedHttpTaskManager.getAllSubtasks();
        List<Task> loadedHistory = loadedHttpTaskManager.getHistory();
        Task loadedTask = loadedHttpTaskManager.getTaskById(1);
        Epic loadedEpic = loadedHttpTaskManager.getEpicById(3);
        Subtask loadedSubtask = loadedHttpTaskManager.getSubtaskById(5);

        assertEquals(tasks, loadedTasks);
        assertEquals(epics, loadedEpics);
        assertEquals(subtasks, loadedSubtasks);
        assertEquals(history, loadedHistory);
        assertEquals(task, loadedTask);
        assertEquals(epic, loadedEpic);
        assertEquals(subtask, loadedSubtask);
    }
}