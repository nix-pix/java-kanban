package ru.yandex.practicum.tasktracker;

import java.util.HashMap;

public class Subtask extends Task { // подзадача
    HashMap<Integer, Content> subtask = new HashMap<>();
}