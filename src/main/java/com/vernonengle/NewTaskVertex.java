package com.vernonengle;

public class NewTaskVertex extends Vertex {

    public NewTaskVertex(Task task, Vertex currentVertex) {
        super(currentVertex);
        this.remainingTasks.remove(task.getId());
        this.startableTasks.remove(task.getId());
        this.activeTasks.add(task.getId());
        this.timeRemainingForTask.put(task.getId(), task.getDuration());
        this.currentTaskPoints += task.getPoints();
    }
}
