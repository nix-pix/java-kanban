package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskIds; // уникальные номера подзадач в этом эпике

    public Epic(int id, String name, String description, TaskStatus status, ArrayList<Integer> subtaskIds) {
        super(id, name, description, status);
        this.subtaskIds = subtaskIds;
    }
}