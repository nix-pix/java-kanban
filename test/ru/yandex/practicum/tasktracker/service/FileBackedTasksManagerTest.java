package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private static Path path;
    private static TaskManager fileTaskManager;
    private static int firstId;

    @BeforeEach
    void beforeEachInFTM() {
        path = Paths.get("." + File.separator + "resources" + File.separator + "testFile.csv");
        fileTaskManager = new FileBackedTasksManager(path);
        firstId = 1;
    }

    @Test
    void taskAndHistoryIntoFileTest() throws IOException {
        fileTaskManager.createTask("Задача 1", "id-1");
        fileTaskManager.createEpic("Эпик 1", "id-2");
        fileTaskManager.createSubtask("Подзадача 1", "id-3", 2);
        Task task = fileTaskManager.getTaskById(firstId);
        Epic epic = fileTaskManager.getEpicById(2);
        Subtask subtask = fileTaskManager.getSubtaskById(3);
        String fileData = Files.readString(path);
        String[] lines = fileData.split("\r?\n");
        String taskLine = lines[1];
        String epicLine = lines[2];
        String subtaskLine = lines[3];
        String historyLine = lines[5];

        assertEquals("1,TASK,Задача 1,id-1,NEW,null,0,null,", taskLine);
        assertEquals("2,EPIC,Эпик 1,id-2,NEW,null,0,null,", epicLine);
        assertEquals("3,SUBTASK,Подзадача 1,id-3,NEW,null,0,null,2", subtaskLine);
        assertEquals("1,2,3", historyLine);
    }

    @Test
    void taskIntoOnlyReadFileTest() {
        path = Paths.get("." + File.separator + "resources" + File.separator + "testOnlyReadFile.csv");
        fileTaskManager = new FileBackedTasksManager(path);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            fileTaskManager.createTask("Задача 1", "id-1");
        });
//        assertEquals("Ошибка сохранения файла", thrown.getMessage());
    }

    @Test
    void taskAndHistoryFromFileTest() {
        path = Paths.get("." + File.separator + "resources" + File.separator + "testLoadFile.csv");
        TaskManager loadedFileTaskManager = FileBackedTasksManager.loadFromFile(path);
        List<Task> tasks = loadedFileTaskManager.getAllTasks();
        List<Epic> epics = loadedFileTaskManager.getAllEpics();
        List<Subtask> subtasks = loadedFileTaskManager.getAllSubtasks();
        List<Task> loadedHistory = loadedFileTaskManager.getHistory();
        assertNotNull(tasks);
        assertNotNull(epics);
        assertNotNull(subtasks);
        assertNotNull(loadedHistory);
    }

    @Test
    void taskAndHistoryFromEmptyFileTest() {
        path = Paths.get("." + File.separator + "resources" + File.separator + "testLoadEmptyFile.csv");
        TaskManager loadedFileTaskManager = FileBackedTasksManager.loadFromFile(path);
        List<Task> tasks = loadedFileTaskManager.getAllTasks();
        List<Epic> epics = loadedFileTaskManager.getAllEpics();
        List<Subtask> subtasks = loadedFileTaskManager.getAllSubtasks();
        List<Task> loadedHistory = loadedFileTaskManager.getHistory();
        assertTrue(tasks.isEmpty());
        assertTrue(epics.isEmpty());
        assertTrue(subtasks.isEmpty());
        assertTrue(loadedHistory.isEmpty());
    }

    @Test
    void EpicWithoutSubtaskAndEmptyHistoryFileTest() throws IOException {
        fileTaskManager.createTask("Задача 1", "id-1");
        fileTaskManager.createEpic("Эпик 1", "id-2");

        TaskManager loadedFileTaskManager = FileBackedTasksManager.loadFromFile(path);
        List<Epic> loadedEpics = loadedFileTaskManager.getAllEpics();
        Epic loadedEpic = loadedEpics.get(0);
        List<Integer> subtasksInEpic = loadedEpic.getSubtaskIds();
        List<Task> loadedHistory = loadedFileTaskManager.getHistory();

        assertTrue(subtasksInEpic.isEmpty());
        assertTrue(loadedHistory.isEmpty());
    }
}