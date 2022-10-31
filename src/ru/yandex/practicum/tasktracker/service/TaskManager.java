package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;

public interface TaskManager {
    public int generateId();
    public ArrayList<Task> getAllTasks();
    public ArrayList<Epic> getAllEpics();
    public ArrayList<Subtask> getAllSubtasks();
    public void deleteAllTasks();
    public void deleteAllEpics();
    public void deleteAllSubtasks();
    public Task getTaskById(int id);
    public Epic getEpicById(int id);
    public Subtask getSubtaskById(int id);
    public void createTask(String name, String description, TaskStatus status);
    public void createEpic(String name, String description, TaskStatus status);
    public void createSubtask(String name, String description, TaskStatus status, int epicId);
    public void updateTask(int id, String name, String description, TaskStatus status);
    public void updateEpic(int id, String name, String description);
    public void updateSubtask(int id, String name, String description, TaskStatus status, int epicId);
    public void deleteTaskById(int id);
    public void deleteEpicById(int id);
    public void deleteSubtaskById(Integer id, int epicId);
    public void getAllSubtasksInEpic(int id);
    public void getHistory();
}