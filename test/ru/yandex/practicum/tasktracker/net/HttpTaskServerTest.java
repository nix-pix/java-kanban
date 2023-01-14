package ru.yandex.practicum.tasktracker.net;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.model.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    private static HttpTaskServer httpTaskServer;
    private final String url = "http://localhost:8080/tasks/";
    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void beforeEachInHTS() throws IOException {
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
    }

    @AfterEach
    void afterEachInHTS() {
        httpTaskServer.stop();
    }

    @Test
    void getPrioritizedTasksAndSubtasksTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        List<Task> taskList = httpTaskServer.fileBackedTasksManager.getPrioritizedTasksAndSubtasks();

        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> loadedTaskList = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(taskList, loadedTaskList);
    }

    @Test
    void getTaskByIdAndAllTasksTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        Task task = httpTaskServer.fileBackedTasksManager.getTaskById(1);
        List<Task> taskList = httpTaskServer.fileBackedTasksManager.getAllTasks();

        URI uri = URI.create(url + "task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task loadedTask = gson.fromJson(response.body(), Task.class);
        URI uri2 = URI.create(url + "task/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Task> loadedTaskList = gson.fromJson(response2.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(task, loadedTask);
        assertEquals(taskList, loadedTaskList);
    }

    @Test
    void getEpicByIdAndAllEpicsTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-1");
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 2", "Эпик с id-2");
        Epic epic = httpTaskServer.fileBackedTasksManager.getEpicById(1);
        List<Epic> epicList = httpTaskServer.fileBackedTasksManager.getAllEpics();

        URI uri = URI.create(url + "epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic loadedEpic = gson.fromJson(response.body(), Epic.class);
        URI uri2 = URI.create(url + "epic/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Epic> loadedEpicList = gson.fromJson(response2.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());

        assertEquals(epic, loadedEpic);
        assertEquals(epicList, loadedEpicList);
    }

    @Test
    void getSubtaskByIdAndAllSubtasksTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-1");
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 2", "Эпик с id-2");
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-3", 1);
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 2", "Подзадача с id-4", 2);
        Subtask subtask = httpTaskServer.fileBackedTasksManager.getSubtaskById(3);
        List<Subtask> subtaskList = httpTaskServer.fileBackedTasksManager.getAllSubtasks();

        URI uri = URI.create(url + "subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask loadedSubtask = gson.fromJson(response.body(), Subtask.class);
        URI uri2 = URI.create(url + "subtask/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Subtask> loadedSubtaskList = gson.fromJson(response2.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(subtask, loadedSubtask);
        assertEquals(subtaskList, loadedSubtaskList);
    }

    @Test
    void getSubtaskInEpicByIdTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-1");
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 2", "Эпик с id-2");
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-3", 1);
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 2", "Подзадача с id-4", 2);
        List<Subtask> subtaskList = httpTaskServer.fileBackedTasksManager.getAllSubtasksInEpic(1);
        List<Subtask> subtaskList2 = httpTaskServer.fileBackedTasksManager.getAllSubtasksInEpic(3);

        URI uri = URI.create(url + "subtask/epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> loadedSubtaskList = gson.fromJson(response.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());
        URI uri2 = URI.create(url + "subtask/epic/?id=n");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Subtask> loadedSubtaskList2 = gson.fromJson(response2.body(), new TypeToken<ArrayList<Subtask>>() {
        }.getType());

        assertEquals(subtaskList, loadedSubtaskList);
//        assertEquals(subtaskList2, loadedSubtaskList2);
    }

    @Test
    void getHistoryTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        Task task = httpTaskServer.fileBackedTasksManager.getTaskById(1);
        List<Task> history = httpTaskServer.fileBackedTasksManager.getHistory();

        URI uri = URI.create(url + "history/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> loadedHistory = gson.fromJson(response.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(history, loadedHistory);
    }

    @Test
    void deleteTaskByIdAndAllTasksTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createTask("Задача 1", "Задача с id-1");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 2", "Задача с id-2");
        httpTaskServer.fileBackedTasksManager.createTask("Задача 3", "Задача с id-3");
        List<Task> taskList0 = httpTaskServer.fileBackedTasksManager.getAllTasks();
        assertEquals(3, taskList0.size());

        URI uri = URI.create(url + "task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList = httpTaskServer.fileBackedTasksManager.getAllTasks();
        assertEquals(2, taskList.size());

        URI uri2 = URI.create(url + "task/?id=n");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).DELETE().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList2 = httpTaskServer.fileBackedTasksManager.getAllTasks();
        assertEquals(2, taskList2.size());

        URI uri3 = URI.create(url + "task/");
        HttpRequest request3 = HttpRequest.newBuilder().uri(uri3).DELETE().build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());
        List<Task> taskList3 = httpTaskServer.fileBackedTasksManager.getAllTasks();
        assertEquals(0, taskList3.size());
    }

    @Test
    void deleteEpicByIdAndAllEpicsTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-1");
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 2", "Эпик с id-2");
        List<Epic> epicList0 = httpTaskServer.fileBackedTasksManager.getAllEpics();
        assertEquals(2, epicList0.size());

        URI uri = URI.create(url + "epic/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicList = httpTaskServer.fileBackedTasksManager.getAllEpics();
        assertEquals(1, epicList.size());

        URI uri2 = URI.create(url + "epic/");
        HttpRequest request2 = HttpRequest.newBuilder().uri(uri2).DELETE().build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicList2 = httpTaskServer.fileBackedTasksManager.getAllEpics();
        assertEquals(0, epicList2.size());
    }

    @Test
    void deleteAllSubtasksTest() throws IOException, InterruptedException {
        httpTaskServer.fileBackedTasksManager.createEpic("Эпик 1", "Эпик с id-1");
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-2", 1);
        httpTaskServer.fileBackedTasksManager.createSubtask("Подзадача 1 в эпике 1", "Подзадача с id-2", 1);
        List<Subtask> subtaskList0 = httpTaskServer.fileBackedTasksManager.getAllSubtasks();
        assertEquals(2, subtaskList0.size());

        URI uri = URI.create(url + "subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).DELETE().build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> subtaskList = httpTaskServer.fileBackedTasksManager.getAllSubtasks();
        assertEquals(0, subtaskList.size());
    }

    @Test
    void postTaskTest() throws IOException, InterruptedException {
        Task task = new Task(1, TaskType.TASK, "Задача 1", "Задача с id-1", TaskStatus.NEW);
        URI uri = URI.create(url + "task/");
        String taskJson = gson.toJson(task);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskToCheck = httpTaskServer.fileBackedTasksManager.getTaskById(1);
        assertEquals(task, taskToCheck);
    }

    @Test
    void postEpicAndSubtaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic(1,TaskType.EPIC, "Эпик 1", "Эпик с id-1", TaskStatus.NEW, new ArrayList<>());
        Subtask subtask = new Subtask(2, TaskType.SUBTASK, "Подзадача 1 в эпике 1", "Подзадача с id-2", TaskStatus.NEW, 1);
        epic.getSubtaskIds().add(2);

        URI uri = URI.create(url + "epic/");
        String epicJson = gson.toJson(epic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI uri2 = URI.create(url + "subtask/");
        String subtaskJson = gson.toJson(subtask);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(uri2)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());

        Epic epicToCheck = httpTaskServer.fileBackedTasksManager.getEpicById(1);
        Subtask subtaskToCheck = httpTaskServer.fileBackedTasksManager.getSubtaskById(2);
        assertEquals(epic, epicToCheck);
        assertEquals(subtask, subtaskToCheck);
    }
}