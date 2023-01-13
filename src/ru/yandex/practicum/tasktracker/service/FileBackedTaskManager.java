package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path path;
    protected final HashMap<Integer, Task> allTasks = new HashMap<>();

    public FileBackedTaskManager(Path path) {
        this.path = path;
    }

    public void save() {
        ArrayList<Task> tasks = getAllTasks();
        ArrayList<Epic> epics = getAllEpics();
        ArrayList<Subtask> subtasks = getAllSubtasks();

        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write("id,type,name,description,status,start,duration,end,epicId\n");
            for (Task task : tasks) {
                writer.write(toString(task) + "\n");
            }
            for (Epic epic : epics) {
                writer.write(toString(epic) + "\n");
            }
            for (Subtask subtask : subtasks) {
                writer.write(toString(subtask) + "\n");
            }
            writer.write("\n");
            writer.write(historyToString(historyManager));
        } catch (IOException e) {
            try {
                throw new ManagerSaveException("Ошибка сохранения файла");
            } catch (ManagerSaveException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    public String toString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + ","
                    + task.getStatus() + "," + task.getStartTime() + "," + task.getDuration() + ","
                    + task.getEndTime() + "," + subtask.getEpicId();
        } else if (task.getType().equals(TaskType.EPIC)) {
            Epic epic = (Epic) task;
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + ","
                    + task.getStatus() + "," + epic.getEpicStartTime() + "," + epic.getEpicDuration() + ","
                    + epic.getEpicEndTime() + ",";
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + ","
                    + task.getStatus() + "," + task.getStartTime() + "," + task.getDuration() + ","
                    + task.getEndTime() + ",";
        }
    }

    public Task fromString(String value) {
        String[] taskData = value.split(",");
        int id = Integer.parseInt(taskData[0]);
        TaskType type = TaskType.valueOf(taskData[1]);
        String name = taskData[2];
        String description = taskData[3];
        TaskStatus status = TaskStatus.valueOf(taskData[4]);

        switch (type) {
            case TASK:
                Task task = new Task(id, type, name, description, status);
                if (!taskData[5].equals("null")) {
                    task.setStartTime(LocalDateTime.parse(taskData[5]));
                    Duration duration = Duration.between(LocalDateTime.parse(taskData[5]), LocalDateTime.parse(taskData[7]));
                    task.setDuration(duration.toMinutes());
                }
                return task;
            case EPIC:
                Epic epic = new Epic(id, type, name, description, status, new ArrayList<>());
                if (!taskData[5].equals("null")) {
                    epic.setEpicStartTime(LocalDateTime.parse(taskData[5]));
                    epic.setEpicDuration(Long.parseLong(taskData[6]));
                    epic.setEpicEndTime(LocalDateTime.parse(taskData[7]));
                }
                return epic;
            case SUBTASK:
                int epicId = Integer.parseInt(taskData[8]);
                Subtask subtask = new Subtask(id, type, name, description, status, epicId);
                if (!taskData[5].equals("null")) {
                    subtask.setStartTime(LocalDateTime.parse(taskData[5]));
                    Duration duration = Duration.between(LocalDateTime.parse(taskData[5]), LocalDateTime.parse(taskData[7]));
                    subtask.setDuration(duration.toMinutes());
                }
                return subtask;
        }
        return null;
    }

    public static String historyToString(HistoryManager manager) {
        List<String> list = new ArrayList<>();
        for (Task task : manager.getHistory()) {
            list.add(String.valueOf(task.getId()));
        }
        return String.join(",", list);
    }

    public static List<Integer> historyFromString(String value) {
        String[] idsString = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (String id : idsString) {
            historyIds.add(Integer.parseInt(id));
        }
        return historyIds;
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(path);
        String fileData;

        try {
            fileData = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            throw new RuntimeException(e);
        }

        if (fileData.isBlank()) {
            System.out.println("Файл пустой.");
            return fileBackedTasksManager;
        }

        String[] lines = fileData.split("\r?\n");
        int i = 1;
        for (; i < lines.length && !lines[i].isBlank(); i++) {
            String line = lines[i];
            Task task = fileBackedTasksManager.fromString(line);
            int taskId = task.getId();
            TaskType type = task.getType();

            if (type.equals(TaskType.TASK)) {
                fileBackedTasksManager.tasks.put(taskId, task);
            } else if (type.equals(TaskType.EPIC)) {
                fileBackedTasksManager.epics.put(taskId, (Epic) task);
            } else if (type.equals(TaskType.SUBTASK)) {
                fileBackedTasksManager.subtasks.put(taskId, (Subtask) task);
            }
        }

        fileBackedTasksManager.allTasks.putAll(fileBackedTasksManager.tasks);
        fileBackedTasksManager.allTasks.putAll(fileBackedTasksManager.epics);
        fileBackedTasksManager.allTasks.putAll(fileBackedTasksManager.subtasks);
        fileBackedTasksManager.prioritizedTasksAndSubtasks.addAll(fileBackedTasksManager.tasks.values());
        fileBackedTasksManager.prioritizedTasksAndSubtasks.addAll(fileBackedTasksManager.subtasks.values());

        if (i < lines.length) {
            List<Integer> historyIds = historyFromString(lines[i + 1]);
            for (Integer id : historyIds) {
                Task task = fileBackedTasksManager.allTasks.get(id);
                fileBackedTasksManager.historyManager.add(task);
            }
        }

        int maxId = 0;
        for (Integer id : fileBackedTasksManager.allTasks.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        fileBackedTasksManager.setIdSequence(maxId);
        return fileBackedTasksManager;
    }

    @Override
    public void createTask(String name, String description) {
        super.createTask(name, description);
        save();
    }

    @Override
    public void createEpic(String name, String description) {
        super.createEpic(name, description);
        save();
    }

    @Override
    public void createSubtask(String name, String description, int epicId) {
        super.createSubtask(name, description, epicId);
        save();
    }

    @Override
    public void setTaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        super.setTaskStartTimeAndDuration(id, startTime, duration);
        save();
    }

    @Override
    public void setSubtaskStartTimeAndDuration(int id, LocalDateTime startTime, long duration) {
        super.setSubtaskStartTimeAndDuration(id, startTime, duration);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }
}