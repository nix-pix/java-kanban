package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.FileBackedTaskManager;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class MainForTZ6 {
    public static void main(String[] args) {
        Path path = Paths.get("." + File.separator + "resources" + File.separator + "memoryFile.csv");

        TaskManager firstFileBackedTasksManager = new FileBackedTaskManager(path);

        System.out.println("\nПервый вызов менеджера задач (файл должен быть пустым):");
        System.out.println("ИСТОРИЯ ПРОСМОТРА задач, эпиков и подзадач из файла в начале программы:");
        System.out.println(firstFileBackedTasksManager.getHistory());
        System.out.println("СПИСОК всех задач, эпиков и подзадач из файла в начале программы:");
        System.out.println(firstFileBackedTasksManager.getAllTasks());
        System.out.println(firstFileBackedTasksManager.getAllEpics());
        System.out.println(firstFileBackedTasksManager.getAllSubtasks());

        System.out.println("\nСоздал 2 задачи и 1 эпик с 2 подзадачами");
        firstFileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        firstFileBackedTasksManager.setTaskStartTimeAndDuration(
                1,
                LocalDateTime.of(2022, 12, 10, 16, 0),
                45);
        firstFileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        firstFileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-3");
        firstFileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        firstFileBackedTasksManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
        firstFileBackedTasksManager.setSubtaskStartTimeAndDuration(
                5,
                LocalDateTime.of(2022, 12, 11, 14, 0),
                10);
        System.out.println("И вызвал задачу с id: 1, эпик с id: 3 и подзадачу с id: 5");
        System.out.println(firstFileBackedTasksManager.getTaskById(1));
        System.out.println(firstFileBackedTasksManager.getEpicById(3));
        System.out.println(firstFileBackedTasksManager.getSubtaskById(5));

        TaskManager secondFileBackedTasksManager = FileBackedTaskManager.loadFromFile(path);

        System.out.println("\nВторой вызов менеджера задач (файл должен быть заполнен):");
        System.out.println("ИСТОРИЯ ПРОСМОТРА задач, эпиков и подзадач из файла:");
        System.out.println(secondFileBackedTasksManager.getHistory());
        System.out.println("СПИСОК всех задач, эпиков и подзадач из файла:");
        System.out.println(secondFileBackedTasksManager.getAllTasks());
        System.out.println(secondFileBackedTasksManager.getAllEpics());
        System.out.println(secondFileBackedTasksManager.getAllSubtasks());
        System.out.println(secondFileBackedTasksManager.getPrioritizedTasksAndSubtasks());
    }
}