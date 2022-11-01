package ru.yandex.practicum.tasktracker.model;

import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds; // номера подзадач в этом эпике

    public Epic(int id, String name, String description, TaskStatus status, List<Integer> subtaskIds) {
        super(id, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + super.getId() +
                ", subtaskIds=" + subtaskIds +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                '}';
    }
}