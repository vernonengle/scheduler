package com.vernonengle;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class TimeProgressVertex extends Vertex {

    private Integer timeOffset;

    public TimeProgressVertex(Vertex sourceVertex, int timeOffset) {
        super(sourceVertex);
        initialize(timeOffset);
    }

    private void initialize(int timeOffset) {
        this.timeOffset = timeOffset;
        this.currentDate =  currentDate.plusDays(timeOffset);
        List<Integer> endedTasks = this.getActiveTasks()
                .stream()
                .filter(taskId -> taskMap.get(taskId).progressTask(timeOffset) == 0)
                .collect(Collectors.toList());
        getActiveTasks().removeAll(endedTasks);
        remainingTasks.removeAll(endedTasks);
        finishedTasks.addAll(endedTasks);
    }

    public TimeProgressVertex(Vertex currentVertex, LocalDate endDate) {
        super(currentVertex);
        Integer timeOffset = Math.toIntExact(currentVertex.currentDate.until(endDate, DAYS));
        initialize(timeOffset);
    }

    @Override
    public double getEdgeWeight() {
        return timeOffset;
    }
}
