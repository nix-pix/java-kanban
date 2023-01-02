package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.time.LocalDateTime;
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
    void copiesInHistoryTest() {
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
    void deleteBeginningFromHistoryTest() {
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
    void deleteMiddleFromHistoryTest() {
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
    void deleteEndingFromHistoryTest() {
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

    @Test
    void setTimeAndDurationForTaskTest() {
        taskManager.createTask("Задача 1", "id - 1");
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 11, 12, 0);
        long durationInMinutes = 30;
        taskManager.setTaskStartTimeAndDuration(firstGeneratedId, dateTime, durationInMinutes);
        Task task = taskManager.getTaskById(firstGeneratedId);
        LocalDateTime savedDateTime = task.getStartTime();
        long savedDuration = task.getDuration();

        assertEquals(dateTime, savedDateTime);
        assertEquals(durationInMinutes, savedDuration);
    }

    @Test
    void setTimeAndDurationForTaskWithWrongIdTest() {
        taskManager.createTask("Задача 1", "id - 1");
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 11, 12, 0);
        long durationInMinutes = 30;
        taskManager.setTaskStartTimeAndDuration(2, dateTime, durationInMinutes);
        Task task = taskManager.getTaskById(firstGeneratedId);
        LocalDateTime savedDateTime = task.getStartTime();
        long savedDuration = task.getDuration();

        assertNotEquals(dateTime, savedDateTime);
        assertNotEquals(durationInMinutes, savedDuration);
    }

    @Test
    void setTimeAndDurationForTaskIntersectionErrorTest() {
        taskManager.createTask("Задача 1", "id - 1");
        taskManager.createTask("Задача 2", "id - 2");
        LocalDateTime dateTime1 = LocalDateTime.of(2022, 12, 11, 12, 0);
        LocalDateTime dateTime2 = LocalDateTime.of(2022, 12, 11, 12, 20);
        long durationInMinutes = 30;
        taskManager.setTaskStartTimeAndDuration(firstGeneratedId, dateTime1, durationInMinutes);
        taskManager.setTaskStartTimeAndDuration(2, dateTime2, durationInMinutes);
        Task task1 = taskManager.getTaskById(firstGeneratedId);
        LocalDateTime savedDateTime1 = task1.getStartTime();
        long savedDuration1 = task1.getDuration();
        Task task2 = taskManager.getTaskById(2);
        LocalDateTime savedDateTime2 = task1.getStartTime();
        long savedDuration2 = task2.getDuration();

        assertEquals(dateTime1, savedDateTime1);
        assertEquals(durationInMinutes, savedDuration1);
        assertNotEquals(dateTime2, savedDateTime2);
        assertNotEquals(durationInMinutes, savedDuration2);
    }

    @Test
    void setTimeAndDurationForSubtaskTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        taskManager.createSubtask("Подзадача 1", "id-2", firstGeneratedId);
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 11, 12, 0);
        long durationInMinutes = 30;
        taskManager.setSubtaskStartTimeAndDuration(2, dateTime, durationInMinutes);
        Subtask subtask = taskManager.getSubtaskById(2);
        Epic epic = taskManager.getEpicById(firstGeneratedId);
        LocalDateTime savedDateTime = subtask.getStartTime();
        LocalDateTime savedEpicDateTime = epic.getEpicStartTime();
        long savedDuration = subtask.getDuration();

        assertEquals(dateTime, savedDateTime);
        assertEquals(durationInMinutes, savedDuration);
        assertEquals(savedDateTime, savedEpicDateTime);
    }

    @Test
    void setTimeAndDurationForSubtaskWithWrongIdTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        taskManager.createSubtask("Подзадача 1", "id-2", firstGeneratedId);
        LocalDateTime dateTime = LocalDateTime.of(2022, 12, 11, 12, 0);
        long durationInMinutes = 30;
        taskManager.setSubtaskStartTimeAndDuration(3, dateTime, durationInMinutes);
        Subtask subtask = taskManager.getSubtaskById(2);
        LocalDateTime savedDateTime = subtask.getStartTime();
        long savedDuration = subtask.getDuration();

        assertNotEquals(dateTime, savedDateTime);
        assertNotEquals(durationInMinutes, savedDuration);
    }

    @Test
    void updateTaskTest() {
        taskManager.createTask("Задача 1", "id - 1");
        String newName = "Новая задача 1";
        taskManager.updateTask(firstGeneratedId, newName, "id-1", TaskStatus.IN_PROGRESS);
        Task updatedTask = taskManager.getTaskById(firstGeneratedId);
        String savedName = updatedTask.getName();
        TaskStatus savedStatus = updatedTask.getStatus();

        assertEquals(newName, savedName);
        assertEquals(TaskStatus.IN_PROGRESS, savedStatus);
    }

    @Test
    void updateEpicTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        String newName = "Новый эпик 1";
        taskManager.updateEpic(firstGeneratedId, newName, "id-1");
        Epic updatedEpic = taskManager.getEpicById(firstGeneratedId);
        String savedName = updatedEpic.getName();

        assertEquals(newName, savedName);
    }

    @Test
    void getTaskWithWrongIdTest() {
        taskManager.createTask("Задача 1", "id - 1");
        Task task = taskManager.getTaskById(2);

        assertNull(task);
//        InputException thrown = assertThrows(InputException.class, () -> {
//            taskManager.getTaskById(3);
//        });
//        assertEquals("Не верно указан id", thrown.getMessage());
    }

    @Test
    void getAllSubtasksInEpicTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        taskManager.createSubtask("Подзадача 1", "id-2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 1", "id-3", firstGeneratedId);
        List<Subtask> subtasksInEpic = taskManager.getAllSubtasksInEpic(firstGeneratedId);
        Subtask subtask1 = taskManager.getSubtaskById(2);
        Subtask subtask2 = taskManager.getSubtaskById(3);

        assertEquals(2, subtasksInEpic.size());
        assertNotNull(subtasksInEpic);
        assertTrue(subtasksInEpic.contains(subtask1));
        assertTrue(subtasksInEpic.contains(subtask2));
    }

    @Test
    void deleteEpicByIdTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        taskManager.createSubtask("Подзадача 1", "id-2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 1", "id-3", firstGeneratedId);
        taskManager.createEpic("Эпик 2", "id-4");
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasksInEpic = taskManager.getAllSubtasksInEpic(firstGeneratedId);
        assertEquals(2, epics.size());
        assertEquals(2, subtasksInEpic.size());

        taskManager.deleteEpicById(firstGeneratedId);
        List<Epic> epicsAfterDelete = taskManager.getAllEpics();
        List<Subtask> subtasksInEpicAfterDelete = taskManager.getAllSubtasksInEpic(firstGeneratedId);
        assertEquals(1, epicsAfterDelete.size());
        assertEquals(null, subtasksInEpicAfterDelete);

    }

    @Test
    void deleteSubtaskByIdTest() {
        taskManager.createEpic("Эпик 1", "id-1");
        taskManager.createSubtask("Подзадача 1", "id-2", firstGeneratedId);
        taskManager.createSubtask("Подзадача 1", "id-3", firstGeneratedId);
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size());

        taskManager.deleteSubtaskById(3, 1);
        List<Subtask> subtasksAfterDelete = taskManager.getAllSubtasks();
        assertEquals(1, subtasksAfterDelete.size());
    }
}