package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.*;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
//    protected final HashMap<Integer, Task> tasksAndSubtasks = new HashMap<>();
    public Set<Task> prioritizedTasks = new TreeSet<>(Comparator);
    private int idSequence = 0;

    HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    public void setIdSequence(int idSequence) {
        this.idSequence = idSequence;
    }

//    public HashMap<Integer, Task> getPrioritizedTasks() {
//        return tasksAndSubtasks;
//    }

    public void saveToPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) {
            prioritizedTasks.add(task);
        } else {
            prioritizedTasks.a
        }
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public void createTask(String name, String description) {
        int id = generateId();
        tasks.put(id, new Task(id, TaskType.TASK, name, description, TaskStatus.NEW));
    }

    @Override
    public void createEpic(String name, String description) {
        int id = generateId();
        epics.put(id, new Epic(id, TaskType.EPIC, name, description, TaskStatus.NEW, new ArrayList<>()));
    }

    @Override
    public void createSubtask(String name, String description, int epicId) {
        int id = generateId();
        subtasks.put(id, new Subtask(id, TaskType.SUBTASK, name, description, TaskStatus.NEW, epicId));
        Epic epic = epics.get(epicId);
        epic.getSubtaskIds().add(id);
    }

    @Override
    public void updateTask(int id, String name, String description, TaskStatus status) {
        tasks.put(id, new Task(id, TaskType.TASK, name, description, status));
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        Epic epic = epics.get(id);
        epic.setName(name);
        epic.setDescription(description);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, String name, String description, TaskStatus status, int epicId) {
        subtasks.put(id, new Subtask(id, TaskType.SUBTASK, name, description, status, epicId));
        Epic epic = epics.get(epicId);
        if (status.equals(TaskStatus.DONE)) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        List<Integer> subtaskIds = epic.getSubtaskIds();
        boolean epicIsDone = true;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (!subtask.getStatus().equals(TaskStatus.DONE)) {
                epicIsDone = false;
                break;
            }
        }
        if (epicIsDone) {
            epic.setStatus(TaskStatus.DONE);
        }
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
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        epics.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtaskById(Integer id, int epicId) {
        subtasks.remove(id);
        historyManager.remove(id);
        Epic epic = epics.get(epicId);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        subtaskIds.remove(id);
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksInEpic(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        for (Integer subtaskId : subtaskIds) {
            subtasksInEpic.add(subtasks.get(subtaskId));
        }
        return subtasksInEpic;
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
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}