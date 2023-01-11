package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int idSequence = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final Set<Task> prioritizedTasksAndSubtasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() != null && o2.getStartTime() != null) {
            if (o1.getStartTime().isBefore(o2.getStartTime())) {
                return -1;
            } else if (o1.getStartTime().isAfter(o2.getStartTime())) {
                return 1;
            } else {
                return o1.getId() - o2.getId();
            }
        }
        return 1;
    });

    HistoryManager historyManager = Managers.getDefaultHistory();

    private int generateId() {
        idSequence++;
        return idSequence;
    }

    public void setIdSequence(int idSequence) {
        this.idSequence = idSequence;
    }

    boolean checkId(int id, TaskType taskType) {
        Set<Integer> idSet = new HashSet<>();
        switch (taskType) {
            case TASK:
                idSet = tasks.keySet();
                break;
            case EPIC:
                idSet = epics.keySet();
                break;
            case SUBTASK:
                idSet = subtasks.keySet();
                break;
        }
        return !idSet.contains(id);
    }

    @Override
    public void createTask(String name, String description) {
        int id = generateId();
        Task task = new Task(id, TaskType.TASK, name, description, TaskStatus.NEW);
        tasks.put(id, task);
        prioritizedTasksAndSubtasks.add(task);
    }

    @Override
    public void createEpic(String name, String description) {
        int id = generateId();
        epics.put(id, new Epic(id, TaskType.EPIC, name, description, TaskStatus.NEW, new ArrayList<>()));
    }

    @Override
    public void createSubtask(String name, String description, int epicId) {
        if (epics.get(epicId) == null) {
            System.out.println("Создать подзадачу без эпика невозможно");
            return;
        }
        int id = generateId();
        Subtask subtask = new Subtask(id, TaskType.SUBTASK, name, description, TaskStatus.NEW, epicId);
        subtasks.put(id, subtask);
        Epic epic = epics.get(epicId);
        epic.getSubtaskIds().add(id);
        prioritizedTasksAndSubtasks.add(subtask);
    }

    @Override //не используется
    public void setStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        if (checkId(id, TaskType.TASK)) {
            System.out.println("Не верно указан id. Время не может быть установлено");
            return;
        }
        Task task = tasks.get(id);
        LocalDateTime taskEndTime = startTime.plus(Duration.ofMinutes(duration));
        for (Task taskToCheck : prioritizedTasksAndSubtasks) {
            if (taskToCheck.getStartTime() != null
                    && startTime.isBefore(taskToCheck.getEndTime()) && taskEndTime.isAfter(taskToCheck.getStartTime())) {
                System.out.println("Неудачная попытка добавить время выполнения задачи. Период выполнения пересекается " +
                        " с периодами из других задач. Выберете другое время.");
                return;
            }
        }
        task.setStartTime(startTime);
        task.setDuration(duration);
        prioritizedTasksAndSubtasks.removeIf(taskToDelete -> taskToDelete.getId() == id);
        prioritizedTasksAndSubtasks.add(task);
    }

    @Override
    public void setTaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        if (checkId(id, TaskType.TASK)) {
            System.out.println("Не верно указан id. Время не может быть установлено");
            return;
        }
        Task task = tasks.get(id);
        LocalDateTime taskEndTime = startTime.plus(Duration.ofMinutes(duration));
        for (Task taskToCheck : prioritizedTasksAndSubtasks) {
            if (taskToCheck.getStartTime() != null
                    && startTime.isBefore(taskToCheck.getEndTime()) && taskEndTime.isAfter(taskToCheck.getStartTime())) {
                System.out.println("Неудачная попытка добавить время выполнения задачи. Период выполнения пересекается " +
                        " с периодами из других задач. Выберете другое время.");
                return;
            }
        }
        task.setStartTime(startTime);
        task.setDuration(duration);
        prioritizedTasksAndSubtasks.removeIf(taskToDelete -> taskToDelete.getId() == id);
        prioritizedTasksAndSubtasks.add(task);
    }

    @Override
    public void setSubtaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        if (checkId(id, TaskType.SUBTASK)) {
            System.out.println("Не верно указан id. Время не может быть установлено");
            return;
        }
        Subtask subtask = subtasks.get(id);
        LocalDateTime subtaskEndTime = startTime.plus(Duration.ofMinutes(duration));
        for (Task taskToCheck : prioritizedTasksAndSubtasks) {
            if (taskToCheck.getStartTime() != null
                    && startTime.isBefore(taskToCheck.getEndTime()) && subtaskEndTime.isAfter(taskToCheck.getStartTime())) {
                System.out.println("Неудачная попытка добавить время выполнения задачи. Период выполнения пересекается " +
                        " с периодами из других задач. Выберете другое время.");
                return;
            }
        }
        subtask.setStartTime(startTime);
        subtask.setDuration(duration);
        prioritizedTasksAndSubtasks.removeIf(subtaskToDelete -> subtaskToDelete.getId() == id);
        prioritizedTasksAndSubtasks.add(subtask);

        Epic epic = epics.get(subtask.getEpicId());
        if (epic.getEpicStartTime() == null) {
            epic.setEpicStartTime(subtask.getStartTime());
            epic.setEpicDuration(subtask.getDuration());
            epic.setEpicEndTime(subtask.getEndTime());
        } else {
            epic.setEpicDuration(subtask.getDuration() + epic.getEpicDuration());
            if (subtask.getStartTime().isBefore(epic.getEpicStartTime())) {
                epic.setEpicStartTime(subtask.getStartTime());
            }
            if (subtask.getEndTime().isAfter(epic.getEpicEndTime())) {
                epic.setEpicEndTime(subtask.getEndTime());
            }
        }
    }

    @Override
    public void updateTask(int id, String name, String description, TaskStatus status) {
        if (checkId(id, TaskType.TASK)) {
            System.out.println("Не верно указан id. Задача не обновлена");
            return;
        }
        Task task = new Task(id, TaskType.TASK, name, description, status);
        tasks.put(id, task);
        prioritizedTasksAndSubtasks.removeIf(taskToDelete -> taskToDelete.getId() == id);
        prioritizedTasksAndSubtasks.add(task);
    }

    @Override
    public void updateEpic(int id, String name, String description) {
        if (checkId(id, TaskType.EPIC)) {
            System.out.println("Не верно указан id. Эпик не обновлен");
            return;
        }
        Epic epic = epics.get(id);
        epic.setName(name);
        epic.setDescription(description);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, String name, String description, TaskStatus status, int epicId) {
        if (checkId(id, TaskType.SUBTASK)) {
            System.out.println("Не верно указан id подзадачи. Подзадача не обновлена");
            return;
        }
        if (epics.get(epicId) == null) {
            System.out.println("Обновить подзадачу без эпика невозможно");
            return;
        }
        Subtask subtask = new Subtask(id, TaskType.SUBTASK, name, description, status, epicId);
        subtasks.put(id, subtask);
        prioritizedTasksAndSubtasks.removeIf(subtaskToDelete -> subtaskToDelete.getId() == id);
        prioritizedTasksAndSubtasks.add(subtask);
        Epic epic = epics.get(epicId);
        if (status.equals(TaskStatus.DONE) || status.equals(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
        List<Integer> subtaskIds = epic.getSubtaskIds();
        boolean epicIsDone = true;
        for (Integer subtaskId : subtaskIds) {
            Subtask subtaskToCheck = subtasks.get(subtaskId);
            if (!subtaskToCheck.getStatus().equals(TaskStatus.DONE)) {
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
        if (checkId(id, TaskType.TASK)) {
            try {
                throw new InputException("Не верно указан id");
            } catch (InputException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        Task task = tasks.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        if (checkId(id, TaskType.EPIC)) {
            try {
                throw new InputException("Не верно указан id");
            } catch (InputException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        Epic epic = epics.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (checkId(id, TaskType.SUBTASK)) {
            try {
                throw new InputException("Не верно указан id");
            } catch (InputException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        Subtask subtask = subtasks.get(id);
        historyManager.add(subtask);
        return subtask;
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
        if (epics.get(id) == null) {
            System.out.println("Такого эпика не существует");
            return null;
        }
        Epic epic = epics.get(id);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        for (Integer subtaskId : subtaskIds) {
            subtasksInEpic.add(subtasks.get(subtaskId));
        }
        return subtasksInEpic;
    }

    @Override
    public List<Task> getPrioritizedTasksAndSubtasks() {
        return new ArrayList<>(prioritizedTasksAndSubtasks);
    }

    @Override
    public void deleteTaskById(int id) {
        if (checkId(id, TaskType.TASK)) {
            System.out.println("Не верно указан id. Задача не удалена");
            return;
        }
        tasks.remove(id);
        historyManager.remove(id);
        prioritizedTasksAndSubtasks.removeIf(taskToDelete -> taskToDelete.getId() == id);
    }

    @Override
    public void deleteEpicById(int id) {
        if (checkId(id, TaskType.EPIC)) {
            System.out.println("Не верно указан id. Эпик не удален");
            return;
        }
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
        if (checkId(id, TaskType.SUBTASK)) {
            System.out.println("Не верно указан id. Подзадача не удалена");
            return;
        }
        if (epics.get(epicId) == null) {
            System.out.println("Удалить подзадачу без эпика невозможно");
            return;
        }
        Epic epic = epics.get(epicId);
        Subtask deleteSubtask = subtasks.get(id);
        if (epic.getEpicDuration() != 0) {
            epic.setEpicDuration(epic.getEpicDuration() - deleteSubtask.getDuration());
        }
        subtasks.remove(id);
        historyManager.remove(id);
        prioritizedTasksAndSubtasks.removeIf(subtaskToDelete -> subtaskToDelete.getId() == id);
        List<Integer> subtaskIds = epic.getSubtaskIds();
        subtaskIds.remove(id);
        epic.setStatus(TaskStatus.IN_PROGRESS);
    }

    @Override
    public void deleteAllTasks() {
        Set<Integer> ids = tasks.keySet();
        for (int id : ids) {
            prioritizedTasksAndSubtasks.removeIf(taskToDelete -> taskToDelete.getId() == id);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        Set<Integer> ids = subtasks.keySet();
        for (int id : ids) {
            prioritizedTasksAndSubtasks.removeIf(subtaskToDelete -> subtaskToDelete.getId() == id);
        }
        subtasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}