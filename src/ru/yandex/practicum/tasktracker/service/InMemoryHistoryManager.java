package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.CustomLinkedList;
import ru.yandex.practicum.tasktracker.model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList customLinkedList = new CustomLinkedList();

    @Override
    public void add(Task task) {
        customLinkedList.linkLast(task);
    }

    @Override
    public void remove(int id) {
        customLinkedList.removeNode(id);
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}