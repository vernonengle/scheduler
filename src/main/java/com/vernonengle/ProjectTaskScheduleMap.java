package com.vernonengle;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public class ProjectTaskScheduleMap {

    private Integer capacityPerUnitTime = Integer.MAX_VALUE;
//    private Map<Integer, List<Integer>> endingTasksOnSlot = new HashMap<>();
//    private Map<Integer, List<Integer>> startingTasksOnSlot = new HashMap<>();
    private Map<Integer, Set<Integer>> activeTasksOnSlot = new HashMap<>();
    private Map<Integer, Task> taskMap = new HashMap<>();
    private Integer totalDays = 0;

    public ProjectTaskScheduleMap(Integer capacityPerUnitTime, Integer totalDays, Map<Integer, Task> taskMap) {
        this.capacityPerUnitTime = capacityPerUnitTime;
        this.taskMap = taskMap;
        this.totalDays = totalDays;
        IntStream.rangeClosed(1, totalDays)
                .boxed()
                .forEach(slot -> {
                    activeTasksOnSlot.put(slot, new HashSet<>());
                });

        //schedule tasks sequentially during init
        int currentStartingSlot = 1;
        for (Task task : taskMap.values()) {
            //startingTasksOnSlot.get(currentStartingSlot).add(task.getId());
            task.setCurrentStartSlot(currentStartingSlot);
            int currentEndingSlot = currentStartingSlot + task.getDuration() - 1;
            IntStream.rangeClosed(currentStartingSlot, currentEndingSlot)
                    .boxed()
                    .forEach(slot -> {
                        activeTasksOnSlot.get(slot).add(task.getId());
                    });
            //endingTasksOnSlot.get(currentEndingSlot).add(task.getId());
            task.setCurrentEndSlot(currentEndingSlot);
            currentStartingSlot = currentEndingSlot + 1;
        }
    }

    public Integer getCapacityPerUnitTime() {
        return capacityPerUnitTime;
    }

    public void setCapacityPerUnitTime(Integer capacityPerUnitTime) {
        this.capacityPerUnitTime = capacityPerUnitTime;
    }

    public void showScheduledTasks() {
        System.out.println("========================");
        Integer slotDigitLength = totalDays.toString().length();
        activeTasksOnSlot.keySet()
                .stream()
                .forEach(slot -> {
                    String slotString = "%0" + slotDigitLength + "d";
                    System.out.format("Slot " + slotString + ": ", slot);
                    System.out.println(activeTasksOnSlot.get(slot));
                });

    }

    public Task getTask(Integer taskId) {
        return taskMap.get(taskId);
    }

    public boolean canPushTaskEarlier(Integer taskToPush) {
        Task task = taskMap.get(taskToPush);
        int nextEarlierSlot = task.getCurrentStartSlot() - 1;
        if (nextEarlierSlot < 1) {
            return false;
        }
        if (slotCapacityExceedsLimit(nextEarlierSlot, task.getPoints())) {
            return false;
        }
        Integer slotOfLatestDependency = task.getDependencies()
                .stream()
                .map(taskId -> taskMap.get(taskId).getCurrentEndSlot())
                .max(Comparator.comparingInt(o -> o))
                .orElse(0);
        if (slotOfLatestDependency >= nextEarlierSlot) {
            return false;
        }
        return true;
    }

    private boolean slotCapacityExceedsLimit(int nextEarlierSlot, int addedWeight) {
        int currentWeight = activeTasksOnSlot.get(nextEarlierSlot)
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
        return currentWeight + addedWeight > capacityPerUnitTime;
    }

    public void pushTaskEarlier(Integer taskToPush) {
        Task task = taskMap.get(taskToPush);
        Integer currentEndSlot = task.getCurrentEndSlot();
        Integer currentStartSlot = task.getCurrentStartSlot();
        activeTasksOnSlot.get(currentEndSlot).remove(task.getId());
        activeTasksOnSlot.get(currentStartSlot - 1).add(task.getId());
        task.setCurrentStartSlot(currentStartSlot - 1);
        task.setCurrentEndSlot(currentEndSlot - 1);
        taskMap.put(task.getId(), task);
    }
}
