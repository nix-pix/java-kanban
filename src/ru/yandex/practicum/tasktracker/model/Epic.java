package ru.yandex.practicum.tasktracker.model;

import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIds; // номера подзадач в этом эпике
    private LocalDateTime epicStartTime;
    private long epicDuration;
    private LocalDateTime epicEndTime;

    public Epic(int id, TaskType type, String name, String description, TaskStatus status, List<Integer> subtaskIds) {
        super(id, type, name, description, status);
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public LocalDateTime getEpicStartTime() {
        return epicStartTime;
    }

    public void setEpicStartTime(LocalDateTime epicStartTime) {
        this.epicStartTime = epicStartTime;
    }

    public long getEpicDuration() {
        return epicDuration;
    }

    public void setEpicDuration(long epicDuration) {
        this.epicDuration = epicDuration;
    }

    public LocalDateTime getEpicEndTime() {
        return epicEndTime;
    }

    public void setEpicEndTime(LocalDateTime epicEndTime) {
        this.epicEndTime = epicEndTime;
    }

    @Override
    public String toString() {
        return "EPIC{" +
                "id=" + super.getId() +
                ", subtaskIds=" + subtaskIds +
                ", name='" + super.getName() + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", startTime=" + epicStartTime +
                ", duration(min)=" + epicDuration +
                ", endTime=" + epicEndTime +
                '}';
    }
}