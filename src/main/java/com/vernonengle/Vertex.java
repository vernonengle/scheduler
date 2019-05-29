package com.vernonengle;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Vertex {

    private Set<Integer> activeTasks = new HashSet<>();
    protected Set<Integer> remainingTasks = new HashSet<>();
    protected Set<Integer> startableTasks = new HashSet<>();
    protected Set<Integer> finishedTasks = new HashSet<>();
    protected Map<Integer, Integer> timeRemainingForTask = new HashMap<>();
    protected Map<LocalDate, Set<Integer>> endDateForTask = new HashMap<>();
    protected Map<Integer, Task> taskMap = new HashMap<>();
    protected Integer currentTaskPoints = 0;
    protected Integer currentTimeUnit = 0;
    protected Integer maxPointsPerUnitTime = Integer.MAX_VALUE;
    protected Vertex previousVertex;
    protected LocalDate currentDate;

    public Vertex(Vertex sourceVertex) {
        this.getActiveTasks().addAll(sourceVertex.getActiveTasks());
        this.remainingTasks.addAll(sourceVertex.remainingTasks);
        this.startableTasks.addAll(sourceVertex.startableTasks);
        this.finishedTasks.addAll(sourceVertex.finishedTasks);

        sourceVertex.taskMap
                .entrySet()
                .stream()
                .forEach( entrySet -> this.taskMap.put(entrySet.getKey(), entrySet.getValue()));
        sourceVertex.timeRemainingForTask
                .entrySet()
                .stream()
                .forEach( entrySet -> this.timeRemainingForTask.put(entrySet.getKey(), entrySet.getValue()));
        sourceVertex.endDateForTask
                .entrySet()
                .stream()
                .forEach( entrySet -> this.endDateForTask.put(entrySet.getKey(), entrySet.getValue()));

        this.currentTaskPoints = sourceVertex.currentTaskPoints;
        this.currentTimeUnit = sourceVertex.currentTimeUnit;
        this.currentDate = sourceVertex.currentDate;
        this.setMaxPointsPerUnitTime(sourceVertex.getMaxPointsPerUnitTime());
    }

    public Vertex() {

    }

    public Set<Integer> getRemainingTasks() {
        return remainingTasks;
    }

    public void setRemainingTasks(Set<Integer> remainingTasks) {
        this.remainingTasks = remainingTasks;
    }

    public void setStartableTasks(Set<Integer> startableTasks) {
        this.startableTasks = startableTasks;
    }

    public boolean hasRemainingTasks() {
        return !remainingTasks.isEmpty() || !activeTasks.isEmpty();
    }

    public Integer getCurrentTaskPoints() {
        return currentTaskPoints;
    }

    public void setCurrentTaskPoints(Integer currentTaskPoints) {
        this.currentTaskPoints = currentTaskPoints;
    }

    public Integer getMaxPointsPerUnitTime() {
        return maxPointsPerUnitTime;
    }

    public void setMaxPointsPerUnitTime(Integer maxPointsPerUnitTime) {
        this.maxPointsPerUnitTime = maxPointsPerUnitTime;
    }

    public Set<Integer> getStartableTasks() {
        return startableTasks;
    }

    public Map<Integer, Task> getTaskMap() {
        return taskMap;
    }

    public void setTaskMap(Map<Integer, Task> taskMap) {
        this.taskMap = taskMap;
    }

    public Map<Integer, Integer> getTimeRemainingForTask() {
        return timeRemainingForTask;
    }

    public double getEdgeWeight() {
        return 0;
    }

    public void setPreviousVertex(Vertex vertex) {
        this.previousVertex = vertex;
    }

    public Vertex getPreviousVertex() {
        return previousVertex;
    }

    @Override
    public String toString() {
        String remainingTasksString = "Remaining Tasks: " + remainingTasks + "\n";
        String finishedTasksString = "FinishedTasks Tasks: " + finishedTasks + "\n";
        String activeTasksString = "Active Tasks: " + getActiveTasks() + "\n";
        String capacityString = "Current Capacity: " + currentTaskPoints + "\n";
        String currentTimeSlotString = "Current timeSlot: " + currentTimeUnit + "\n";
        return remainingTasksString
                + finishedTasksString
                + activeTasksString
                + capacityString;
                //+ currentTimeSlotString;
    }

    public Set<Integer> getActiveTasks() {
        return activeTasks;
    }


    public boolean isEdge() {
        return activeTasks.isEmpty() && remainingTasks.isEmpty();
    }


    public void setCurrentDate(LocalDate currentDate) {
        this.currentDate = currentDate;
    }
}
