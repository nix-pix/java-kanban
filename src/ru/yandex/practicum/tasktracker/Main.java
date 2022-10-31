package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.model.TaskStatus;
import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        taskManager.createTask("Первая задача", "Что-то там...1", TaskStatus.NEW);
        taskManager.createTask("Вторая задача", "Что-то там...2", TaskStatus.NEW);
        taskManager.createEpic("Первый эпик", "Что-то там...3", TaskStatus.NEW);
        taskManager.createSubtask("Первая подзадача в первом эпике", "Что-то там...4",
                TaskStatus.NEW, 3);
        taskManager.createSubtask("Вторая подзадача в первом эпике", "Что-то там...5",
                TaskStatus.NEW, 3);
        taskManager.createEpic("Второй эпик", "Что-то там...6", TaskStatus.NEW);
        taskManager.createSubtask("Первая подзадача во втором эпике", "Что-то там...7",
                TaskStatus.NEW, 6);

        System.out.println("\nСоздал 2 задачи, один эпик с 2 подзадачами, а другой эпик с 1 подзадачей:");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.updateTask(1, "Выполнил первую задачу", "Готово...1", TaskStatus.DONE);
        taskManager.updateSubtask(5, "Выполнил вторую подзадачу в первом эпике", "Готово...5",
                TaskStatus.DONE, 3);
        taskManager.updateSubtask(7, "Выполнил первую подзадачу во втором эпике", "Готово...7",
                TaskStatus.DONE, 6);

        System.out.println("\nЗавершил первую задачу, вторую подзадачу в первом эпике и первую " +
                "подзадачу во втором эпике:");
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(7));
        System.out.println(taskManager.getEpicById(6));

        System.out.println("\nИстория просмотров задач:");
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(1);
        taskManager.deleteSubtaskById(5, 3);
        taskManager.deleteEpicById(6);

        System.out.println("\nУдалил первую задачу, вторую подзадачу в первом эпике и второй эпик. Остались:");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}