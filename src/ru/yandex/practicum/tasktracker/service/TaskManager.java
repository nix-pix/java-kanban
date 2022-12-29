package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void createTask(String name, String description);

    void createEpic(String name, String description);

    void createSubtask(String name, String description, int epicId);

    void setTaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration);

    void setSubtaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration);

    void updateTask(int id, String name, String description, TaskStatus status);

    void updateEpic(int id, String name, String description);

    void updateSubtask(int id, String name, String description, TaskStatus status, int epicId);

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Subtask> getAllSubtasksInEpic(int id);

    List<Task> getPrioritizedTasksAndSubtasks();

    void deleteTaskById(int id);

    void deleteEpicById(int id);

    void deleteSubtaskById(Integer id, int epicId);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    List<Task> getHistory();
}