package ru.yandex.practicum.tasktracker.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.net.KVTaskClient;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskManager extends FileBackedTaskManager {
    String url; // http://localhost:8078/
    private final KVTaskClient kvTaskClient;
    private final Gson gson = new Gson();

    public HttpTaskManager(String url) {
        super(Paths.get("." + File.separator + "resources" + File.separator + "httpMemoryCopyFile.csv"));
        this.url = url;
        kvTaskClient = new KVTaskClient(url);
    }

    public void putOnServer(Key key) {
        ArrayList<Task> tasks = getAllTasks();
        ArrayList<Epic> epics = getAllEpics();
        ArrayList<Subtask> subtasks = getAllSubtasks();
        List<Task> history = getHistory();

        switch (key) {
            case TASKS:
                String tasksJson = gson.toJson(tasks);
                kvTaskClient.put(String.valueOf(Key.TASKS), tasksJson);
                break;
            case EPICS:
                String epicsJson = gson.toJson(epics);
                kvTaskClient.put(String.valueOf(Key.EPICS), epicsJson);
                break;
            case SUBTASKS:
                String subtasksJson = gson.toJson(subtasks);
                kvTaskClient.put(String.valueOf(Key.SUBTASKS), subtasksJson);
                break;
            case HISTORY:
                String historyJson = gson.toJson(history);
                kvTaskClient.put(String.valueOf(Key.HISTORY), historyJson);
                break;
        }
    }

    public HttpTaskManager loadFromServer(String url) {
        HttpTaskManager httpTaskManager = new HttpTaskManager(url);
        String tasksJson = kvTaskClient.load("TASKS");
        String epicsJson = kvTaskClient.load("EPICS");
        String subtasksJson = kvTaskClient.load("SUBTASKS");
        String historyJson = kvTaskClient.load("HISTORY");
        Type taskType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        ArrayList<Task> tasks = gson.fromJson(tasksJson, taskType);
        Type epicType = new TypeToken<ArrayList<Epic>>() {
        }.getType();
        ArrayList<Epic> epics = gson.fromJson(epicsJson, epicType);
        Type subtaskType = new TypeToken<ArrayList<Subtask>>() {
        }.getType();
        ArrayList<Subtask> subtasks = gson.fromJson(subtasksJson, subtaskType);
        List<Task> history = gson.fromJson(historyJson, taskType);
        for (Task task : tasks) {
            httpTaskManager.tasks.put(task.getId(), task);
        }
        for (Epic epic : epics) {
            httpTaskManager.epics.put(epic.getId(), epic);
        }
        for (Subtask subtask : subtasks) {
            httpTaskManager.subtasks.put(subtask.getId(), subtask);
        }
        httpTaskManager.allTasks.putAll(httpTaskManager.tasks);
        httpTaskManager.allTasks.putAll(httpTaskManager.epics);
        httpTaskManager.allTasks.putAll(httpTaskManager.subtasks);
        httpTaskManager.prioritizedTasksAndSubtasks.addAll(httpTaskManager.tasks.values());
        httpTaskManager.prioritizedTasksAndSubtasks.addAll(httpTaskManager.subtasks.values());

        for (Task task : history) {
            httpTaskManager.historyManager.add(task);
        }

        int maxId = 0;
        for (Integer id : httpTaskManager.allTasks.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        httpTaskManager.setIdSequence(maxId);

        return httpTaskManager;
    }

    @Override
    public void createTask(String name, String description) {
        super.createTask(name, description);
        putOnServer(Key.TASKS);
    }

    @Override
    public void createEpic(String name, String description) {
        super.createEpic(name, description);
        putOnServer(Key.EPICS);
    }

    @Override
    public void createSubtask(String name, String description, int epicId) {
        super.createSubtask(name, description, epicId);
        putOnServer(Key.SUBTASKS);
    }

    @Override
    public void setTaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        super.setTaskStartTimeAndDuration(id, startTime, duration);
        putOnServer(Key.TASKS);
    }

    @Override
    public void setSubtaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        super.setSubtaskStartTimeAndDuration(id, startTime, duration);
        putOnServer(Key.SUBTASKS);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        putOnServer(Key.TASKS);
        putOnServer(Key.HISTORY);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        putOnServer(Key.EPICS);
        putOnServer(Key.HISTORY);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        putOnServer(Key.SUBTASKS);
        putOnServer(Key.HISTORY);
        return subtask;
    }

    enum Key {
        TASKS, EPICS, SUBTASKS, HISTORY
    }
}