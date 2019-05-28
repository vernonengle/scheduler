package com.vernonengle;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectTaskScheduler {


    public ProjectTaskScheduleMap scheduleTasksSequentiallyOnMap(Project project) {
        int totalDays = project.getTasks()
                .stream()
                .mapToInt(Task::getDuration)
                .sum();
        Map<Integer, Task> taskMap = project.getTasks()
                .stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));
        return new ProjectTaskScheduleMap(project.getCapacityPerUnitTime(), totalDays, taskMap);
    }
}
