package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public int idSequence = 0;

    public int generateId() {
        idSequence = idSequence + 1;
        return idSequence;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.equals(task)) {
                tasksList.add(task);
            }
        }
        return tasksList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            if (epic.equals(epic)) {
                epicsList.add(epic);
            }
        }
        return epicsList;
    }

    public ArrayList<Subtask> getAllSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (subtask.equals(subtask)) {
                subtasksList.add(subtask);
            }
        }
        return subtasksList;
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        return epic;
    }

    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        return subtask;
    }

    public void createTask(String name, String description, TaskStatus status) {
        int id = generateId();
        tasks.put(id, new Task(id, name, description, status));
    }

    public void createEpic(String name, String description, TaskStatus status) {
        int id = generateId();
        epics.put(id, new Epic(id, name, description, status, new ArrayList<>()));
    }

    public void createSubtask(String name, String description, TaskStatus status, int epicId) {
        int id = generateId();
        subtasks.put(id, new Subtask(id, name, description, status, epicId));
        Epic epic = epics.get(epicId);
        epic.subtaskIds.add(id);
    }

    public void updateTask(int id, String name, String description, TaskStatus status) {
        tasks.put(id, new Task(id, name, description, status));
    }

    public void updateEpic(int id, String name, String description) {
        Epic epic = epics.get(id);
        epic.id = id;
        epic.name = name;
        epic.description = description;
        epics.put(id, epic);
    }

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

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(Integer id, int epicId) {
        subtasks.remove(id);
        Epic epic = epics.get(epicId);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        subtaskIds.remove(id);
    }

    public void getAllSubtasksInEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtaskIds = epic.subtaskIds;
        for (Integer subtaskId : subtaskIds) {
            subtasks.get(subtaskId);
        }
    }
}