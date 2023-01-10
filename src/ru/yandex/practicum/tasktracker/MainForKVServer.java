package ru.yandex.practicum.tasktracker;

import ru.yandex.practicum.tasktracker.net.KVServer;

import java.io.IOException;

public class MainForKVServer {

    public static void main(String[] args) throws IOException {
        new KVServer().start();
    }
}