package ru.yandex.practicum.tasktracker.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class TaskTest {

    private static Task task;

    @BeforeEach
    public void beforeEach() {
        task = new Task(1, TaskType.TASK, "Тестовая задача", "Тест", TaskStatus.NEW);
    }

    @Test
    void getEndTime() {
        LocalDateTime startTime = LocalDateTime.of(2022, 12, 10, 16, 0);
        long duration = 30;
        task.setStartTime(startTime);
        task.setDuration(duration);
        LocalDateTime taskEndTime = task.getEndTime();
        LocalDateTime expectedEndTime = startTime.plusMinutes(duration);
        Assertions.assertTrue(expectedEndTime.isEqual(taskEndTime));
    }
}