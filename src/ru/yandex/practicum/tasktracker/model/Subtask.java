package ru.yandex.practicum.tasktracker.model;

import java.util.Objects;

public class Subtask extends Task {
    private final int epicId; // номер эпика, в который входит эта подзадача

    public Subtask(int id, TaskType type, String name, String description, TaskStatus status, int epicId) {
        super(id, type, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "SUBTASK{" +
                "id=" + super.getId() +
                ", epicId=" + epicId +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", startTime=" + super.getStartTime() +
                ", duration(min)=" + super.getDuration() +
                ", endTime=" + super.getEndTime() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}