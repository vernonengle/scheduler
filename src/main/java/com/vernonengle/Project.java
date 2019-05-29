package com.vernonengle;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Project {

    @JsonProperty("scheduleStart")
    private String scheduleStartString;
    private LocalDate scheduleStartDate;
    private List<Task> tasks = new ArrayList<>();
    private Integer capacityPerUnitTime = Integer.MAX_VALUE;

    public String getScheduleStartString() {
        return scheduleStartString;
    }

    public void setScheduleStartString(String scheduleStartString) {
        this.scheduleStartString = scheduleStartString;
    }

    public LocalDate getScheduleStartDate() {
        return scheduleStartDate;
    }

    public void setScheduleStartDate(LocalDate scheduleStartDate) {
        this.scheduleStartDate = scheduleStartDate;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Integer getCapacityPerUnitTime() {
        return capacityPerUnitTime;
    }

    public void setCapacityPerUnitTime(Integer capacityPerUnitTime) {
        this.capacityPerUnitTime = capacityPerUnitTime;
    }
}
