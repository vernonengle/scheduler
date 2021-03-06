package com.vernonengle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewTaskVertex extends Vertex {

    private void initializeFields(Task task) {
        this.remainingTasks.remove(task.getId());
        this.getActiveTasks().add(task.getId());
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

    public NewTaskVertex(List<Task> tasksToStart, Vertex currentVertex) {
        super(currentVertex);
        tasksToStart
                .forEach(this::initializeFields);
    }
}
