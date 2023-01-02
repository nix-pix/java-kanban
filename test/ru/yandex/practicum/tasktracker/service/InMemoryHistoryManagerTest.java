package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;
import ru.yandex.practicum.tasktracker.model.TaskType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private static HistoryManager historyManager;
    private static Task task;

    @BeforeEach
    void beforeEach() {
        historyManager = Managers.getDefaultHistory();
        task = new Task(1, TaskType.TASK, "Тестовая задача", "Тест", TaskStatus.NEW);
    }

    @Test
    void addTest() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История пустая");
    }

    @Test
    void removeTest() {
        historyManager.add(task);
        int taskId = task.getId();
        historyManager.remove(taskId);
        final List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty(), "История не пустая");
        assertEquals(0, history.size(), "История не пустая");
    }

    @Test
    void getHistoryTest() {
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История пустая");
        assertEquals(1, history.size(), "История пустая");
    }
}