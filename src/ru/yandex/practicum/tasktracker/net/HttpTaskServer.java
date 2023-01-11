package ru.yandex.practicum.tasktracker.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    public TaskManager fileBackedTasksManager;
    private HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        this.fileBackedTasksManager = Managers.getFileBacked();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String requestMethod = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String query = httpExchange.getRequestURI().getQuery();
            switch (requestMethod) {
                case "GET": {
                    if (Pattern.matches("^/tasks/$", path)) {
                        String response = gson.toJson(fileBackedTasksManager.getPrioritizedTasksAndSubtasks());
                        sendText(httpExchange, response);
                        break;
                    }
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (query == null) {
                            String response = gson.toJson(fileBackedTasksManager.getAllTasks());
                            sendText(httpExchange, response);
                        } else {
                            String[] splitQuery = query.split("=");
                            int id = parseId(splitQuery[1]);
                            if (id != -1) {
                                String response = gson.toJson(fileBackedTasksManager.getTaskById(id));
                                sendText(httpExchange, response);
                            } else {
                                System.out.println("Получен некорректный id = " + splitQuery[1]);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        if (query == null) {
                            String response = gson.toJson(fileBackedTasksManager.getAllEpics());
                            sendText(httpExchange, response);
                        } else {
                            String[] splitQuery = query.split("=");
                            int id = parseId(splitQuery[1]);
                            if (id != -1) {
                                String response = gson.toJson(fileBackedTasksManager.getEpicById(id));
                                sendText(httpExchange, response);
                            } else {
                                System.out.println("Получен некорректный id = " + splitQuery[1]);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        if (query == null) {
                            String response = gson.toJson(fileBackedTasksManager.getAllSubtasks());
                            sendText(httpExchange, response);
                        } else {
                            String[] splitQuery = query.split("=");
                            int id = parseId(splitQuery[1]);
                            if (id != -1) {
                                String response = gson.toJson(fileBackedTasksManager.getSubtaskById(id));
                                sendText(httpExchange, response);
                            } else {
                                System.out.println("Получен некорректный id = " + splitQuery[1]);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/epic/$", path)) {
                        String[] splitQuery = query.split("=");
                        int id = parseId(splitQuery[1]);
                        if (id != -1) {
                            String response = gson.toJson(fileBackedTasksManager.getAllSubtasksInEpic(id));
                            sendText(httpExchange, response);
                        } else {
                            System.out.println("Получен некорректный id = " + splitQuery[1]);
                            httpExchange.sendResponseHeaders(405, 0);
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/history/$", path)) {
                        String response = gson.toJson(fileBackedTasksManager.getHistory());
                        sendText(httpExchange, response);
                        break;
                    }
                    break;
                }
                case "POST": {
                    InputStream body = httpExchange.getRequestBody();
                    byte[] allBytesOfBody = body.readAllBytes();
                    String bodyContent = new String(allBytesOfBody);
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        TaskFromJson taskFromJson = gson.fromJson(bodyContent, TaskFromJson.class);
                        String name = taskFromJson.getName();
                        String description = taskFromJson.getDescription();
                        fileBackedTasksManager.createTask(name, description);
                        sendText(httpExchange, "Задача_создана");
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        TaskFromJson epicFromJson = gson.fromJson(bodyContent, TaskFromJson.class);
                        String name = epicFromJson.getName();
                        String description = epicFromJson.getDescription();
                        fileBackedTasksManager.createEpic(name, description);
                        sendText(httpExchange, "Эпик_создан");
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        TaskFromJson subtaskFromJson = gson.fromJson(bodyContent, TaskFromJson.class);
                        String name = subtaskFromJson.getName();
                        String description = subtaskFromJson.getDescription();
                        int epicId = subtaskFromJson.getEpicId();
                        fileBackedTasksManager.createSubtask(name, description, epicId);
                        sendText(httpExchange, "Подзадача_создана");
                        break;
                    }
                    break;
                }
                case "DELETE": {
                    if (Pattern.matches("^/tasks/task/$", path)) {
                        if (query == null) {
                            fileBackedTasksManager.deleteAllTasks();
                            sendText(httpExchange, "Все_задачи_удалены");
                        } else {
                            String[] splitQuery = query.split("=");
                            int id = parseId(splitQuery[1]);
                            if (id != -1) {
                                fileBackedTasksManager.deleteTaskById(id);
                                sendText(httpExchange, "Задача_с_id_" + id + "_удалена");
                            } else {
                                System.out.println("Получен некорректный id = " + splitQuery[1]);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/epic/$", path)) {
                        if (query == null) {
                            fileBackedTasksManager.deleteAllEpics();
                            sendText(httpExchange, "Все_эпики_удалены");
                        } else {
                            String[] splitQuery = query.split("=");
                            int id = parseId(splitQuery[1]);
                            if (id != -1) {
                                fileBackedTasksManager.deleteEpicById(id);
                                sendText(httpExchange, "Эпик_с_id_" + id + "_удален");
                            } else {
                                System.out.println("Получен некорректный id = " + splitQuery[1]);
                                httpExchange.sendResponseHeaders(405, 0);
                            }
                        }
                        break;
                    }
                    if (Pattern.matches("^/tasks/subtask/$", path)) {
                        fileBackedTasksManager.deleteAllSubtasks();
                        sendText(httpExchange, "Все_подзадачи_удалены");
                        break;
                    }
                    break;
                }
                default: {
                    System.out.println(requestMethod + " - неверный запрос");
                    httpExchange.sendResponseHeaders(405, 0);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    public void start() {
        System.out.println("Сервер запущен на порту " + PORT);
        System.out.println("http://localhost:" + PORT + "/tasks");
        //для запуска пройти по ссылке: http://localhost:8080/tasks
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Сервер остановлен на порту " + PORT);
    }

    private String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
    }

    private void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] response = text.getBytes(DEFAULT_CHARSET);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        httpExchange.sendResponseHeaders(200, response.length);
        httpExchange.getResponseBody().write(response);
    }

    private int parseId(String query) {
        try {
            return Integer.parseInt(query);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    class TaskFromJson {
        private String name;
        private String description;
        private int epicId;

        public TaskFromJson(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public TaskFromJson(String name, String description, int epicId) {
            this.name = name;
            this.description = description;
            this.epicId = epicId;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getEpicId() {
            return epicId;
        }
    }
}