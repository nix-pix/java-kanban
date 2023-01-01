package ru.yandex.practicum.tasktracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

class FileBackedTasksManagerTest extends TaskManagerTest {

    private static Path path;
    private static TaskManager fileTaskManager;
    private static int firstId;

    @BeforeEach
    void beforeEach() {
        path = Paths.get("." + File.separator + "resources" + File.separator + "testFile.csv");
        fileTaskManager = new FileBackedTasksManager(path);
        firstId = 1;
    }

    @Test
    void saveTest() {
    }
}