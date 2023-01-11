package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.net.HttpTaskServer;

import java.io.IOException;

public class MainForTZ8 {

    public static void main(String[] args) throws IOException {

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-3");
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
        httpTaskServer.fileBackedTasksManager.getTaskById(2);
        httpTaskServer.fileBackedTasksManager.getSubtaskById(4);
        //System.out.println(httpTaskServer.fileBackedTasksManager.getAllTasks());
        //httpTaskServer.stop();
    }
}