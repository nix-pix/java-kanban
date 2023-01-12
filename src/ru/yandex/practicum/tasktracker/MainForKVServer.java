package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.net.KVServer;
import ru.yandex.practicum.tasktracker.net.KVTaskClient;

import java.io.IOException;

public class MainForKVServer {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
        KVTaskClient kvTaskClient = new KVTaskClient("http://localhost:8078/");
        kvTaskClient.put("TestKEY_1", "testJSON_1");
        kvTaskClient.put("TestKEY_2", "testJSON_2");
        kvTaskClient.put("TestKEY_3", "testJSON_3");
        System.out.println(kvTaskClient.load("TestKEY_1"));
        System.out.println(kvTaskClient.load("TestKEY_3"));
        System.out.println(kvTaskClient.load("TestKEY_2"));
        kvTaskClient.put("TestKEY_2", "ЧТО-ТО НОВЕНЬКОЕ");
        System.out.println(kvTaskClient.load("TestKEY_2"));
    }
}