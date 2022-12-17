package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() {
        ArrayList<Task> tasks = getAllTasks();
        ArrayList<Epic> epics = getAllEpics();
        ArrayList<Subtask> subtasks = getAllSubtasks();

        try (FileWriter writer = new FileWriter(path.toFile())) {
            writer.write("id,type,name,description,status,epicId\n");
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
                throw new RuntimeException(ex);
            }
        }
    }

    public String toString(Task task) {
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask subtask = (Subtask) task;
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + ","
                    + task.getStatus() + "," + subtask.getEpicId();
        } else {
            return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getDescription() + ","
                    + task.getStatus() + ",";
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
                return new Task(id, type, name, description, status);
            case EPIC:
                return new Epic(id, type, name, description, status, new ArrayList<>());
            case SUBTASK:
                int epicId = Integer.parseInt(taskData[5]);
                return new Subtask(id, type, name, description, status, epicId);
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

    public static FileBackedTasksManager loadFromFile(Path path) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);
        String fileData;

        try {
            fileData = Files.readString(path);
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            throw new RuntimeException(e);
        }

        String[] lines = fileData.split("\r?\n");
        int i = 1;
        for (; !lines[i].isBlank() && i < lines.length; i++) {
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
        List<Integer> historyIds = historyFromString(lines[i]);
        for (Integer id : historyIds) {

        }
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