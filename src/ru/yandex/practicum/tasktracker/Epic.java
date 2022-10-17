package ru.yandex.practicum.tasktracker;

import java.util.HashMap;

public class Epic extends Task { // большая задача с подзадачами
    HashMap<Integer, HashMap<Integer, Content>> epic = new HashMap<>();
}