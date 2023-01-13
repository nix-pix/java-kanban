package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.net.KVServer;
import ru.yandex.practicum.tasktracker.service.HttpTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

public class MainForTZ8 {

    public static void main(String[] args) throws IOException {

//        System.out.println("Проверка HttpTaskServer:");
//        HttpTaskServer httpTaskServer = new HttpTaskServer();
//        httpTaskServer.start();
//        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
//        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
//        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-3");
//        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
//        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
//        httpTaskServer.fileBackedTasksManager.getTaskById(2);
//        httpTaskServer.fileBackedTasksManager.getSubtaskById(4);
//        //System.out.println(httpTaskServer.fileBackedTasksManager.getAllTasks());
//        //httpTaskServer.stop();

        new KVServer().start();
//        TaskManager httpTaskManager1 = Managers.getDefault();
        HttpTaskManager httpTaskManager1 = new HttpTaskManager("http://localhost:8078/");
        httpTaskManager1.createTask("Задача 1", "Задача с id-1");

        System.out.println(httpTaskManager1.getTaskById(1));
        httpTaskManager1.setTaskStartTimeAndDuration(
                1,
                LocalDateTime.of(2022, 12, 10, 16, 0),
                45);
        httpTaskManager1.createTask("Задача 2", "Задача с id-2");
        httpTaskManager1.createEpic("Эпик 1", "Эпик с id-3");
        httpTaskManager1.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        httpTaskManager1.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
        httpTaskManager1.setSubtaskStartTimeAndDuration(
                5,
                LocalDateTime.of(2022, 12, 11, 14, 0),
                10);

        System.out.println(httpTaskManager1.getTaskById(1));
        System.out.println(httpTaskManager1.getEpicById(3));
        System.out.println(httpTaskManager1.getSubtaskById(5));

        System.out.println(httpTaskManager1.getAllTasks());
        System.out.println(httpTaskManager1.getAllEpics());
        System.out.println(httpTaskManager1.getAllSubtasks());
        System.out.println(httpTaskManager1.getHistory());

//        HttpTaskManager httpTaskManager2 = httpTaskManager1.loadFromServer("http://localhost:8078/");
        HttpTaskManager httpTaskManager2 = httpTaskManager1.loadFromServer("http://localhost:8078/");

        System.out.println(httpTaskManager2.getAllTasks());
        System.out.println(httpTaskManager2.getAllEpics());
        System.out.println(httpTaskManager2.getAllSubtasks());
        System.out.println(httpTaskManager2.getHistory());
    }
}