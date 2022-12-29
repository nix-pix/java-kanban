package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.time.LocalDateTime;

public class MainForTZ7 {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

//        System.out.println("Проверка новых полей startTime, duration, endTime:");
//
//        taskManager.createTask("Задача 1", "Задача с id-1");
//        taskManager.setTaskStartTimeAndDuration(
//                1,
//                LocalDateTime.of(2022, 12, 10, 15, 0),
//                30);
//        System.out.println(taskManager.getTaskById(1));
//
//        taskManager.createEpic("Эпик 1", "Эпик с id-2");
//        taskManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-3", 2);
//        taskManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-4", 2);
//        taskManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 2);
//        taskManager.setSubtaskStartTimeAndDuration(
//                3,
//                LocalDateTime.of(2022, 12, 10, 16, 0),
//                45);
//        taskManager.setSubtaskStartTimeAndDuration(
//                4,
//                LocalDateTime.of(2022, 12, 9, 15, 0),
//                45);
//        taskManager.setSubtaskStartTimeAndDuration(
//                5,
//                LocalDateTime.of(2022, 12, 11, 14, 0),
//                10);
//        System.out.println(taskManager.getEpicById(2));
//        System.out.println(taskManager.getSubtaskById(3));
//        System.out.println(taskManager.getSubtaskById(4));
//        System.out.println(taskManager.getSubtaskById(5));

        System.out.println("Проверка сортировки по startTime:");

        taskManager.createTask("Задача 1", "Задача с id-1");
        taskManager.setTaskStartTimeAndDuration(
                1,
                LocalDateTime.of(2022, 12, 11, 9, 0),
                30);
        taskManager.createTask("Задача 2", "Задача с id-2");
        taskManager.setTaskStartTimeAndDuration(
                2,
                LocalDateTime.of(2022, 12, 11, 8, 0),
                30);
        taskManager.createTask("Задача 3", "Задача с id-3");
        taskManager.createTask("Задача 4", "Задача с id-4");
        taskManager.setTaskStartTimeAndDuration(
                4,
                LocalDateTime.of(2022, 12, 11, 7, 0),
                30);
        taskManager.createTask("Задача 5", "Задача с id-5");
        taskManager.setTaskStartTimeAndDuration(
                5,
                LocalDateTime.of(2022, 12, 11, 6, 0),
                30);
        System.out.println(taskManager.getPrioritizedTasksAndSubtasks());
    }
}