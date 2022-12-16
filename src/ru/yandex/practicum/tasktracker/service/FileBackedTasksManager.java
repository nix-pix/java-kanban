package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager { //класс менеджера, который будет после каждой
    // операции автоматически сохранять все задачи и их состояние в файл
    private Path path;

    public FileBackedTasksManager(Path path) {
        this.path = path;
    }

    public void save() throws ManagerSaveException { //метод сохраняет текущее состояние менеджера в файл
/*id,type,name,description,status,epicId
1,TASK,Task1,NEW,Description task1,
2,EPIC,Epic2,DONE,Description epic2,
3,SUBTASK,Sub Task2,DONE,Description sub task3,2

2,3*/
        path = Paths.get("." + File.separator + "resources" + File.separator + "memoryFile.csv");
        FileWriter writer = null;
        try {
            writer = new FileWriter(path);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    public String toString(Task task) { //метод сохранения задачи в строку
    }

    public Task fromString(String value) { //метод создания задачи из строки
        //Files.readString(Path.of(path)); //способ чтения файлов
    }

    public static String historyToString(HistoryManager manager) { //метод для сохранения менеджера истории в файл
    }

    public static List<Integer> historyFromString(String value) { //метод для восстановления менеджера истории из файла
    }

    public static FileBackedTasksManager loadFromFile(Path path) { //метод восстанавливает данные менеджера из файла
        // при запуске программы
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager(path);
        try {

        }
    }

    void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(path);

        System.out.println("\nСоздал 2 задачи и 1 эпик с 2 подзадачами");
        fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-3");
        fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        fileBackedTasksManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);

        System.out.println("\nВызвал задачу с id: 2, эпик с id: 3 и подзадачу с id: 5");
        System.out.println(fileBackedTasksManager.getTaskById(2));
        System.out.println(fileBackedTasksManager.getEpicById(3));
        System.out.println(fileBackedTasksManager.getSubtaskById(5));

        FileBackedTasksManager newFileBackedTasksManager = new FileBackedTasksManager(path);
        System.out.println("\nИСТОРИЯ ПРОСМОТРА задач, эпиков и подзадач из файла:");
        System.out.println(newFileBackedTasksManager.getHistory());

        System.out.println("\nСПИСОК всех задач, эпиков и подзадач из файла:");
        System.out.println(newFileBackedTasksManager.getAllTasks());
        System.out.println(newFileBackedTasksManager.getAllEpics());
        System.out.println(newFileBackedTasksManager.getAllSubtasks());
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
        super.getTaskById(id);
        save();
    }

    @Override
    public Epic getEpicById(int id) {
        super.getEpicById(id);
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        super.getSubtaskById(id);
        save();
    }
}