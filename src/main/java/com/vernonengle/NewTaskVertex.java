package com.vernonengle;

import java.util.HashSet;
import java.util.Set;

public class NewTaskVertex extends Vertex {

    public NewTaskVertex(Task task, Vertex currentVertex) {
        super(currentVertex);
        this.remainingTasks.remove(task.getId());
        this.startableTasks.remove(task.getId());
        this.getActiveTasks().add(task.getId());
        this.timeRemainingForTask.put(task.getId(), task.getDuration());
        this.currentTaskPoints += task.getPoints();
        task.setStartDate(this.currentDate);
        task.setEndDate(this.currentDate.plusDays(task.getDuration()));
        Set<Integer> taskIds;
        if (endDateForTask.containsKey(task.getEndDate())) {
            taskIds = endDateForTask.get(task.getEndDate());
        } else {
            taskIds = new HashSet<>();
        }
        taskIds.add(task.getId());
        endDateForTask.put(task.getEndDate(), taskIds);
    }
}
