package ru.yandex.practicum.tasktracker.model;

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
                '}';
    }
}