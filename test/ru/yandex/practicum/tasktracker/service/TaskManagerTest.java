package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    private static TaskManager taskManager;
    private static int firstGeneratedId;

    @BeforeEach
    void beforeEach() {
        taskManager = Managers.getDefault();
        firstGeneratedId = 1;
    }

    @Test
    void epicWithEmptySubtasksListStatusTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        Epic epic = taskManager.getEpicById(firstGeneratedId);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус не соответствует");
    }

    @Test
    void epicWithAllNewSubtasksStatusTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        taskManager.createSubtask("Подзадача 1", "id - 2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 2", "id - 3", firstGeneratedId);
        taskManager.createSubtask("Подзадача 3", "id - 4", firstGeneratedId);
        Epic epic = taskManager.getEpicById(firstGeneratedId);

        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус не соответствует");
    }

    @Test
    void epicWithAllDoneSubtasksStatusTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        taskManager.createSubtask("Подзадача 1", "id - 2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 2", "id - 3", firstGeneratedId);
        taskManager.createSubtask("Подзадача 3", "id - 4", firstGeneratedId);
        taskManager.updateSubtask(2, "Подзадача 1 выполнена", "id - 2", TaskStatus.DONE, firstGeneratedId);
        taskManager.updateSubtask(3, "Подзадача 2 выполнена", "id - 3", TaskStatus.DONE, firstGeneratedId);
        taskManager.updateSubtask(4, "Подзадача 3 выполнена", "id - 4", TaskStatus.DONE, firstGeneratedId);
        Epic epic = taskManager.getEpicById(firstGeneratedId);

        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус не соответствует");
    }

    @Test
    void epicWithNewAndDoneSubtasksStatusTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        taskManager.createSubtask("Подзадача 1", "id - 2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 2", "id - 3", firstGeneratedId);
        taskManager.createSubtask("Подзадача 3", "id - 4", firstGeneratedId);
        taskManager.updateSubtask(3, "Подзадача 2 выполнена", "id - 3", TaskStatus.DONE, firstGeneratedId);
        taskManager.updateSubtask(4, "Подзадача 3 выполнена", "id - 4", TaskStatus.DONE, firstGeneratedId);
        Epic epic = taskManager.getEpicById(1);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус не соответствует");
    }

    @Test
    void epicWithAllInProgressSubtasksStatusTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        taskManager.createSubtask("Подзадача 1", "id - 2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 2", "id - 3", firstGeneratedId);
        taskManager.createSubtask("Подзадача 3", "id - 4", firstGeneratedId);
        taskManager.updateSubtask(2, "Подзадача 1 выполнена", "id - 2", TaskStatus.IN_PROGRESS, firstGeneratedId);
        taskManager.updateSubtask(3, "Подзадача 2 выполнена", "id - 3", TaskStatus.IN_PROGRESS, firstGeneratedId);
        taskManager.updateSubtask(4, "Подзадача 3 выполнена", "id - 4", TaskStatus.IN_PROGRESS, firstGeneratedId);
        Epic epic = taskManager.getEpicById(1);

        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус не соответствует");
    }

    @Test
    void createSubtaskWithoutEpicTest() {
        taskManager.createSubtask("Подзадача 1", "id - 1", 0);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        List<Task> prioritizedSubtasks = taskManager.getPrioritizedTasksAndSubtasks();

        assertTrue(subtasks.isEmpty());
        assertTrue(prioritizedSubtasks.isEmpty());
        assertEquals(0, subtasks.size(), "Неверное количество задач");
        assertEquals(0, prioritizedSubtasks.size(), "Неверное количество задач");
    }

    @Test
    void createTaskTest() {
        taskManager.createTask("Задача 1", "id - 1");
        Task savedTask = taskManager.getTaskById(firstGeneratedId);
        List<Task> tasks = taskManager.getAllTasks();
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasksAndSubtasks();

        assertNotNull(savedTask, "Задача не найдена");
        assertNotNull(tasks, "Задачи на возвращаются");
        assertNotNull(prioritizedTasks, "Задачи на возвращаются");
        assertEquals(1, tasks.size(), "Неверное количество задач");
        assertEquals(1, prioritizedTasks.size(), "Неверное количество задач");
        assertEquals(savedTask, tasks.get(0), "Задачи не совпадают");
        assertEquals(savedTask, prioritizedTasks.get(0), "Задачи не совпадают");
    }

    @Test
    void createEpicTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        Epic savedEpic = taskManager.getEpicById(firstGeneratedId);
        List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(savedEpic, "Задача не найдена");
        assertNotNull(epics, "Задачи на возвращаются");
        assertEquals(1, epics.size(), "Неверное количество задач");
        assertEquals(savedEpic, epics.get(0), "Задачи не совпадают");
    }

    @Test
    void createSubtaskTest() {
        taskManager.createEpic("Эпик 1", "id - 1");
        taskManager.createSubtask("Подзадача 1", "id - 2", firstGeneratedId);
        int secondGeneratedId = 2;
        Subtask savedSubtask = taskManager.getSubtaskById(secondGeneratedId);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        List<Task> prioritizedSubtasks = taskManager.getPrioritizedTasksAndSubtasks();

        assertNotNull(savedSubtask, "Задача не найдена");
        assertNotNull(subtasks, "Задачи на возвращаются");
        assertNotNull(prioritizedSubtasks, "Задачи на возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество задач");
        assertEquals(1, prioritizedSubtasks.size(), "Неверное количество задач");
        assertEquals(savedSubtask, subtasks.get(0), "Задачи не совпадают");
        assertEquals(savedSubtask, prioritizedSubtasks.get(0), "Задачи не совпадают");
    }

    @Test
    void CopiesInHistoryTest() {
        taskManager.createTask("Задача 1", "id - 1");
        taskManager.createTask("Задача 2", "id - 2");
        taskManager.createTask("Задача 3", "id - 3");
        Task task1 = taskManager.getTaskById(firstGeneratedId);
        Task task2 = taskManager.getTaskById(2);
        Task task1copy = taskManager.getTaskById(firstGeneratedId);
        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task1copy, history.get(1));
    }

    @Test
    void DeleteBeginingFromHistoryTest() {
        taskManager.createTask("Задача 1", "id - 1");
        taskManager.createTask("Задача 2", "id - 2");
        taskManager.createTask("Задача 3", "id - 3");
        Task task1 = taskManager.getTaskById(firstGeneratedId);
        Task task2 = taskManager.getTaskById(2);
        Task task3 = taskManager.getTaskById(3);
        taskManager.deleteTaskById(firstGeneratedId);
        List<Task> history = taskManager.getHistory();

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void DeleteMiddleFromHistoryTest() {
        taskManager.createTask("Задача 1", "id - 1");
        taskManager.createTask("Задача 2", "id - 2");
        taskManager.createTask("Задача 3", "id - 3");
        Task task1 = taskManager.getTaskById(firstGeneratedId);
        Task task2 = taskManager.getTaskById(2);
        Task task3 = taskManager.getTaskById(3);
        taskManager.deleteTaskById(2);
        List<Task> history = taskManager.getHistory();

        assertEquals(task1, history.get(0));
    }

    @Test
    void DeleteEndingFromHistoryTest() {
        taskManager.createTask("Задача 1", "id - 1");
        taskManager.createTask("Задача 2", "id - 2");
        taskManager.createTask("Задача 3", "id - 3");
        Task task1 = taskManager.getTaskById(firstGeneratedId);
        Task task2 = taskManager.getTaskById(2);
        Task task3 = taskManager.getTaskById(3);
        taskManager.deleteTaskById(3);
        List<Task> history = taskManager.getHistory();

        assertEquals(task2, history.get(1));
    }
}