package com.vernonengle;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class TimeProgressVertex extends Vertex {

    private Integer timeOffset;

    public TimeProgressVertex(Vertex sourceVertex, int timeOffset) {
        super(sourceVertex);
        this.timeOffset = timeOffset;
        int newTimeSlot = currentTimeUnit + timeOffset;
        this.currentTimeUnit = newTimeSlot;
        this.currentDate =  currentDate.plusDays(timeOffset);
        List<Integer> endedTasks = this.getActiveTasks()
                .stream()
                .filter(taskId -> taskMap.get(taskId).progressTask(timeOffset) == 0)
                .collect(Collectors.toList());
        getActiveTasks().removeAll(endedTasks);
        remainingTasks.removeAll(endedTasks);
        finishedTasks.addAll(endedTasks);
        endedTasks.stream()
                .map(endedTaskId -> taskMap.get(endedTaskId))
                .filter(task -> task.getEndDate() == null)
                .forEach(endedTask -> {
                    timeRemainingForTask.remove(endedTask.getId());
                    endedTask.setEndDate(this.currentDate);
                });
        getActiveTasks().
                stream()
                .forEach(activeTask -> {
                    timeRemainingForTask.put(activeTask, taskMap.get(activeTask).getDuration() - taskMap.get(activeTask).getProgress());
                });
        remainingTasks.stream()
                .map(taskId -> taskMap.get(taskId))
                .filter(task -> finishedTasks.containsAll(task.getDependencies()))
                .forEach(task -> startableTasks.add(task.getId()));
    }

    public TimeProgressVertex(Vertex currentVertex, LocalDate endDate) {
        super(currentVertex);
        Integer timeOffset = Math.toIntExact(currentVertex.currentDate.until(endDate, DAYS));
        this.timeOffset = timeOffset;
        List<Integer> endedTasks = this.getActiveTasks()
                .stream()
                .filter(taskId -> taskMap.get(taskId).progressTask(timeOffset) == 0)
                .collect(Collectors.toList());
        getActiveTasks().removeAll(endedTasks);
        remainingTasks.removeAll(endedTasks);
        finishedTasks.addAll(endedTasks);
        endedTasks.stream()
                .map(endedTaskId -> taskMap.get(endedTaskId))
                .filter(task -> task.getEndDate() == null)
                .forEach(endedTask -> {
                    timeRemainingForTask.remove(endedTask.getId());
                    endedTask.setEndDate(this.currentDate);
                });
        remainingTasks.stream()
                .map(taskId -> taskMap.get(taskId))
                .filter(task -> finishedTasks.containsAll(task.getDependencies()))
                .forEach(task -> startableTasks.add(task.getId()));
        this.currentDate = endDate;
    }

    @Override
    public double getEdgeWeight() {
        return timeOffset;
    }
}
