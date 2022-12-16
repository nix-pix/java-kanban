package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.FileBackedTasksManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainForTZ6 {
    public static void main(String[] args) {
        Path path = Paths.get("." + File.separator + "resources" + File.separator + "memoryFile.csv");

        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(path);

        System.out.println("\nПервый вызов менеджера задач (файл должен быть пустым):");
        System.out.println("ИСТОРИЯ ПРОСМОТРА задач, эпиков и подзадач из файла в начале программы:");
        System.out.println(fileBackedTasksManager.getHistory());
        System.out.println("СПИСОК всех задач, эпиков и подзадач из файла в начале программы:");
        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println(fileBackedTasksManager.getAllEpics());
        System.out.println(fileBackedTasksManager.getAllSubtasks());

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

        System.out.println("\nВторой вызов менеджера задач (файл должен быть заполнен):");
        System.out.println("ИСТОРИЯ ПРОСМОТРА задач, эпиков и подзадач из файла:");
        System.out.println(newFileBackedTasksManager.getHistory());
        System.out.println("СПИСОК всех задач, эпиков и подзадач из файла:");
        System.out.println(newFileBackedTasksManager.getAllTasks());
        System.out.println(newFileBackedTasksManager.getAllEpics());
        System.out.println(newFileBackedTasksManager.getAllSubtasks());
    }
}