package ru.yandex.practicum.tasktracker;

import java.util.HashMap;

public class Task { // обычная задача
    HashMap<Integer, Content> task = new HashMap<>();
}

/* Для всех задач должны выполняться условия:
- Для каждой подзадачи известно, в рамках какого эпика она выполняется.
- Каждый эпик знает, какие подзадачи в него входят.
- Завершение всех подзадач эпика считается завершением эпика. */