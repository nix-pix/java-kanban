package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

public class MainForTZ5 {

    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefault();

        System.out.println("\nСоздал 2 задачи, 1 эпик с 3 подзадачами и 2 эпик с 1 подзадачей");
        taskManager.createTask("Задача 1", "Задача с id-1");
        taskManager.createTask("Задача 2", "Задача с id-2");
        taskManager.createEpic("Эпик 1", "Эпик с id-3");
        taskManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-4", 3);
        taskManager.createSubtask("Подзадача 2 в эпике 1", "Подзадача с id-5", 3);
        taskManager.createSubtask("Подзадача 3 в эпике 1", "Подзадача с id-6", 3);
        taskManager.createEpic("Эпик 2", "Эпик с id-7");
        taskManager.createSubtask("Подзадача 1 в эпике 2", "Подзадача с id-8", 7);

        System.out.println("\nВызвал задачи с id: 1, 1, 2");
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getTaskById(2));
        System.out.println("ИСТОРИЯ задач:");
        System.out.println(taskManager.getHistory());

        System.out.println("\nВызвал эпики с id: 3, 3, 7");
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getEpicById(7));
        System.out.println("ИСТОРИЯ задач и эпиков:");
        System.out.println(taskManager.getHistory());

        System.out.println("\nВызвал подзадачи с id: 6, 4, 5, 6, 8");
        System.out.println(taskManager.getSubtaskById(6));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtaskById(6));
        System.out.println(taskManager.getSubtaskById(8));
        String historyText = "ИСТОРИЯ задач, эпиков и подзадач:";
        System.out.println(historyText);
        System.out.println(taskManager.getHistory());

        System.out.println("\nУдалил задачу с id: 1, в том числе из истории");
        taskManager.deleteTaskById(1);
        System.out.println(historyText);
        System.out.println(taskManager.getHistory());

        System.out.println("\nУдалил эпик с id: 3 и должны удалиться подзадачи с id: 4, 5, 6, в том числе из истории");
        taskManager.deleteEpicById(3);
        System.out.println(historyText);
        System.out.println(taskManager.getHistory());

        System.out.println("\nУдалил подзадачу с id: 8, в том числе из истории и список подзадач эпика должен обновиться");
        taskManager.deleteSubtaskById(8, 7); //проверка на удаление подзадачи
        System.out.println(historyText);
        System.out.println(taskManager.getHistory());
    }
}