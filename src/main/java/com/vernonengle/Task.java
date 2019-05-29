package com.vernonengle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private Integer id;
    private String name;
    private Integer duration = 0;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> dependencies = new ArrayList<>();
    private Integer points = 0;
    private Integer currentStartSlot = 0;
    private Integer currentEndSlot = 0;
    private Integer progress = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Integer> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Integer> dependencies) {
        this.dependencies = dependencies;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getCurrentStartSlot() {
        return currentStartSlot;
    }

    public void setCurrentStartSlot(Integer currentStartSlot) {
        this.currentStartSlot = currentStartSlot;
    }

    public Integer getCurrentEndSlot() {
        return currentEndSlot;
    }

    public void setCurrentEndSlot(Integer currentEndSlot) {
        this.currentEndSlot = currentEndSlot;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer progressTask(int timeOffset) {
        this.progress += timeOffset;
        return progress < duration ? progress : 0;
    }
}
