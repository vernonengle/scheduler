package com.vernonengle;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

public class TimeProgressVertex extends Vertex {

    private Integer timeOffset;

    public TimeProgressVertex(Vertex sourceVertex, int timeOffset) {
        super(sourceVertex);
        this.timeOffset = timeOffset;
        int newTimeSlot = currentTimeUnit + timeOffset;
        this.currentTimeUnit = newTimeSlot;

        List<Integer> endedTasks = this.activeTasks
                .stream()
                .filter(taskId -> taskMap.get(taskId).progressTask(timeOffset) == 0)
                .collect(Collectors.toList());
        activeTasks.removeAll(endedTasks);
        remainingTasks.removeAll(endedTasks);
        finishedTasks.addAll(endedTasks);
        endedTasks.stream()
                .forEach(endedTask -> timeRemainingForTask.remove(endedTask));
        activeTasks.stream()
                .forEach(activeTask -> {
                    timeRemainingForTask.put(activeTask, taskMap.get(activeTask).getProgress());
                });
        remainingTasks.stream()
                .map(taskId -> taskMap.get(taskId))
                .filter(task -> !task.getDependencies().containsAll(finishedTasks))
                .forEach(task -> startableTasks.add(task.getId()));
    }

    @Override
    public double getEdgeWeight() {
        return timeOffset;
    }
}
