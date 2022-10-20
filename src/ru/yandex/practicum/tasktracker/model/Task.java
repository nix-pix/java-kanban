package ru.yandex.practicum.tasktracker.model;

public class Task {
    public int id;
    public String name;
    public String description;
    public TaskStatus status;

    public Task(int id, String name, String description, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }
}