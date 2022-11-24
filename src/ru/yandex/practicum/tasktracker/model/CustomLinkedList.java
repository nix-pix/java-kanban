package ru.yandex.practicum.tasktracker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private final HashMap<Integer, Node> history = new HashMap<>();

    public Node head;
    public Node tail;
    private int size = 0;

    public int getSize() {
        return size;
    }

    public void linkLast(Task task) {
        int id = task.getId();
        if (history.containsKey(id)) {
            removeNode(id);
        }

        final Node t = tail;
        final Node newNode = new Node(t, task, null);
        tail = newNode;

        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        size++;
        history.put(id, newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        for (Node x = head; x != null; x = x.next) {
            tasks.add(x.task);
        }
        return tasks;
    }

    public void removeNode(int id) {
        Node x = history.get(id);
        Node prev = x.prev;
        Node next = x.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }

        x.task = null;
        size--;
        history.remove(id);
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