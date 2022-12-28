package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private final HashMap<Integer, Node> history = new HashMap<>();
    private Node head;
    private Node tail;
    int size = 0;

    public void linkLast(Task task) {
        int id = task.getId();
        if (history.containsKey(id)) {
            removeNode(id);
        }

        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        size++;
        history.put(id, newNode);
    }

    public void removeNode(int id) {
        Node toRemove = history.get(id);
        Node prev = toRemove.prev;
        Node next = toRemove.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            toRemove.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            toRemove.next = null;
        }

        toRemove.task = null;
        size--;
        history.remove(id);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node i = head; i != null; i = i.next) {
            tasks.add(i.task);
        }
        return tasks;
    }

    static class Node {
        Node prev;
        Task task;
        Node next;

        Node(Node prev, Task task, Node next) {
            this.prev = prev;
            this.task = task;
            this.next = next;
        }
    }
}