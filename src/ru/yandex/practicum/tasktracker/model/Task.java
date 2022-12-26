package ru.yandex.practicum.tasktracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
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

    public Task(int id, TaskType type, String name, String description, TaskStatus status, LocalDateTime startTime, long duration) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(Duration.ofMinutes(duration));
    }

    @Override
    public String toString() {
        return "TASK{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}