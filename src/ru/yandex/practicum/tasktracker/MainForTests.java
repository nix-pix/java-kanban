package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.util.ArrayList;

public class MainForTests {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask("Задача 1", "id-1");
        Task task = taskManager.getTaskById(2);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getPrioritizedTasksAndSubtasks());
        taskManager.deleteTaskById(1);

        System.out.println("");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getPrioritizedTasksAndSubtasks());

        System.out.println("");
        ArrayList<Subtask> subtaskArrayList = taskManager.getAllSubtasksInEpic(1);
    }
}