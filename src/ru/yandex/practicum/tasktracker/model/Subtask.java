package ru.yandex.practicum.tasktracker.model;

public class Subtask extends Task {
    public int epicId; // уникальный номер эпика, в который входит эта подзадача

    public Subtask(int id, String name, String description, TaskStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }
}