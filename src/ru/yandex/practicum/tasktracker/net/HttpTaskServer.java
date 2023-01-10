package ru.yandex.practicum.tasktracker.net;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.service.Managers;
import ru.yandex.practicum.tasktracker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new Gson();
    private HttpServer httpServer;

    TaskManager fileBackedTasksManager = Managers.getFileBacked();

    public HttpTaskServer(Path path) throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
//        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/tasks", this::handleTasks);
    }

//    public HttpTaskServer(Path path, HttpServer httpServer, TaskManager fileBackedTasksManager) {
//        super(path);
//        this.httpServer = httpServer;
//        this.fileBackedTasksManager = fileBackedTasksManager;
//    }

//    static class TasksHandler implements HttpHandler {
//        @Override
//        public void handle(HttpExchange exchange) throws IOException {
//
//        }
//    }

    private void handleTasks(HttpExchange httpExchange) {
        try {
            String path = httpExchange.getRequestURI().getPath();
            String requestMethod = httpExchange.getRequestMethod();
            switch (requestMethod) {
                case "GET": {

                    break;
                }
                case "POST": {

                    break;
                }
                case "DELETE": {

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

    private int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }
}
