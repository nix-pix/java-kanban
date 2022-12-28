package ru.yandex.practicum.tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private final int id;
    private final TaskType type;
    private String name;
    private String description;
    private TaskStatus status;
    private LocalDateTime startTime;
    private long duration;

    public Task(int id, TaskType type, String name, String description, TaskStatus status) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        LocalDateTime endTime = null;
        try {
            endTime = startTime.plus(Duration.ofMinutes(duration));
        } catch (NullPointerException e) {
        }
        return endTime;
    }

    @Override
    public String toString() {
        return "TASK{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration(min)=" + duration +
                ", endTime=" + getEndTime() +
                '}';
    }
}