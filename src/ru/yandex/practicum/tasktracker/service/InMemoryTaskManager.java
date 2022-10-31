package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private static HashMap<Integer, Epic> epics = new HashMap<>();
    private static int idSequence = 0;

    HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        idSequence = idSequence + 1;
        return idSequence;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>(tasks.values());
        return tasksList;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>(epics.values());
        return epicsList;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>(subtasks.values());
        return subtasksList;
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void createTask(String name, String description, TaskStatus status) {
        int id = generateId();
        tasks.put(id, new Task(id, name, description, status));
    }

    @Override
    public void createEpic(String name, String description, TaskStatus status) {
        int id = generateId();
        epics.put(id, new Epic(id, name, description, status, new ArrayList<>()));
    }

    @Override
    public void createSubtask(String name, String description, TaskStatus status, int epicId) {
        int id = generateId();
        subtasks.put(id, new Subtask(id, name, description, status, epicId));
        Epic epic = epics.get(epicId);
        epic.subtaskIds.add(id);
    }

    @Override
    public void updateTask(int id, String name, String description, TaskStatus status) {
        tasks.put(id, new Task(id, name, description, status));
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        Epic epic = epics.get(id);
        epic.id = id;
        epic.name = name;
        epic.description = description;
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, String name, String description, TaskStatus status, int epicId) {
        subtasks.put(id, new Subtask(id, name, description, status, epicId));
        Epic epic = epics.get(epicId);
        if (status.equals(TaskStatus.DONE)) {
            epic.status = TaskStatus.IN_PROGRESS;
        }
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        boolean epicIsDone = true;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (!subtask.status.equals(TaskStatus.DONE)) {
                epicIsDone = false;
                break;
            }
        }
        if (epicIsDone) {
            epic.status = TaskStatus.DONE;
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id, int epicId) {
        subtasks.remove(id);
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        subtaskIds.remove(id);
    }

    @Override
    public void getAllSubtasksInEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        for (Integer subtaskId : subtaskIds) {
            subtasks.get(subtaskId);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }
}