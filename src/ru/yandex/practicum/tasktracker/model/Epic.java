package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Integer> subtaskIds; // номера подзадач в этом эпике

    public Epic(int id, String name, String description, TaskStatus status, ArrayList<Integer> subtaskIds) {
        super(id, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}