package ru.yandex.practicum.tasktracker.model;

import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds; // номера подзадач в этом эпике

    public Epic(int id, TaskType type, String name, String description, TaskStatus status, List<Integer> subtaskIds) {
        super(id, type, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public String toString() {
        return "EPIC{" +
                "id=" + super.getId() +
                ", subtaskIds=" + subtaskIds +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                '}';
    }
}