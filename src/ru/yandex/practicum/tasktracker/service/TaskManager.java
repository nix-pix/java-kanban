package ru.yandex.practicum.tasktracker.service;

import ru.yandex.practicum.tasktracker.model.Epic;
import ru.yandex.practicum.tasktracker.model.Subtask;
import ru.yandex.practicum.tasktracker.model.Task;
import ru.yandex.practicum.tasktracker.model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager { // Этот класс будет запускаться на старте программы и управлять всеми задачами
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, Subtask> subtasks = new HashMap<>();
    public HashMap<Integer, Epic> epics = new HashMap<>();
    public int idSequence = 0;

    public int generateId() {
        idSequence = idSequence + 1;
        return idSequence;
    }

    public HashMap getAllTasks() {
        return tasks;//, subtasks, epics;
    }

    public void deleteAllTasks() {
    }

    public void getTaskById() {
    }

    public void createTask(String name, String description) {
        idSequence = generateId();
        tasks.put(idSequence, new Task(idSequence, name, description, TaskStatus.NEW));
    }

    public void createEpic(String name, String description) {
        idSequence = generateId();
        epics.put(idSequence, new Epic(idSequence, name, description, TaskStatus.NEW, new ArrayList<>()));
    }

    public void createSubtask(String name, String description, int epicId) {
        idSequence = generateId();
        subtasks.put(idSequence, new Subtask(idSequence, name, description, TaskStatus.NEW, epicId));
        Epic epic = epics.get(epicId);
        epic.subtaskIds.add(idSequence);
    }

    public void updateTask() {
    }

    public void deleteTaskById() {
    }

    public void getAllSubtasksInEpic() {
    }

    public void controlStatus() {
    }
}

/* 2.Методы для каждого из типа задач(Задача/Эпик/Подзадача):
- Получение списка всех задач.
- Удаление всех задач.
- Получение по идентификатору.
- Создание. Сам объект должен передаваться в качестве параметра.
- Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
- Удаление по идентификатору.
3.Дополнительные методы:
- Получение списка всех подзадач определённого эпика.
4.Управление статусами осуществляется по следующему правилу:
- Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
  По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
  (это означает, что не существует отдельного метода, который занимался бы только обновлением статуса задачи.
  Вместо этого статус задачи обновляется вместе с полным обновлением задачи.)
- Для эпиков: (Пользователь не должен иметь возможности поменять статус эпика самостоятельно.)
  если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
  если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
  во всех остальных случаях статус должен быть IN_PROGRESS.
  (Когда меняется статус любой подзадачи в эпике, вам необходимо проверить,
  что статус эпика изменится соответствующим образом. При этом изменение статуса эпика может и не произойти,
  если в нём, к примеру, всё ещё есть незакрытые задачи.) */