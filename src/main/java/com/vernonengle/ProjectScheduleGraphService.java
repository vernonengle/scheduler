package com.vernonengle;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.traverse.DepthFirstIterator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectScheduleGraphService {
    public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> generateProjectScheduleGraph(Project project) {
        Set<Integer> remainingTasks = project.getTasks()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
        Vertex topVertex = new Vertex();
        topVertex.setRemainingTasks(remainingTasks);

        Map<Integer, Task> taskMap = project.getTasks()
                .stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));
        topVertex.setTaskMap(taskMap);
        topVertex.setMaxPointsPerUnitTime(project.getCapacityPerUnitTime());
        LocalDate startDate = LocalDate.parse(project.getScheduleStartString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        topVertex.setCurrentDate(startDate);

        return buildProjectGraph(topVertex);
    }

    public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> buildProjectGraph(Vertex topVertex) {
        DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        GraphBuilder<Vertex, DefaultWeightedEdge, DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge>> graphBuilder = new GraphBuilder<>(projectGraph);
        Stack<Vertex> vertexStack = new Stack<>();
        vertexStack.push(topVertex);
        while (!vertexStack.isEmpty()) {
            Vertex currentVertex = vertexStack.pop();
            Vertex previousVertex = currentVertex.getPreviousVertex();
            if (previousVertex == null) {
                graphBuilder.addVertex(currentVertex);
            } else {
                graphBuilder.addEdge(previousVertex, currentVertex, currentVertex.getEdgeWeight());
            }

            if (currentVertex.hasRemainingTasks()) {
                Vertex nextVertex = getNextStartableTasksSingleNode(currentVertex);
                if (nextVertex == null) {
                    Vertex timeProgressVertex = createTimeProgressVertex(currentVertex);
                    timeProgressVertex.setPreviousVertex(currentVertex);
                    vertexStack.push(timeProgressVertex);
                } else {
                    nextVertex.setPreviousVertex(currentVertex);
                    vertexStack.push(nextVertex);
                }
            }
        }

        return graphBuilder.build();
    }

    private Vertex getNextStartableTasksSingleNode(Vertex currentVertex) {
        List<Task> tasksToStart = currentVertex.getRemainingTasks()
                .stream()
                .map(taskId -> currentVertex.taskMap.get(taskId))
                .filter(task -> currentVertex.finishedTasks.containsAll(task.getDependencies()))
                .collect(Collectors.toList());
        if (tasksToStart.isEmpty()) {
            return null;
        }
        return new NewTaskVertex(tasksToStart, currentVertex);
    }

    public TimeProgressVertex createTimeProgressVertex(Vertex currentVertex) {
        return currentVertex.endDateForTask
                .entrySet()
                .stream()
                .filter(endDateEntrySet ->
                        endDateEntrySet.getValue()
                                .stream()
                                .anyMatch(id -> currentVertex.getActiveTasks().contains(id))
                )
                .map(Map.Entry::getKey)
                .min(LocalDate::compareTo)
                .map(endDate -> new TimeProgressVertex(currentVertex, endDate))
                .orElse(new TimeProgressVertex(currentVertex, 1));
    }

    public List<Project> getProjectSchedules(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph, Project project) {
        List<Project> projectSchedules = new ArrayList<>();
        DepthFirstIterator<Vertex, DefaultWeightedEdge> depthFirstIterator = new DepthFirstIterator<>(projectGraph);
        Project scheduledProject = new Project();
        LocalDate startDate = LocalDate.parse(project.getScheduleStartString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        scheduledProject.setScheduleStartDate(startDate);
        Map<Integer, Task> taskMap = project.getTasks()
                .stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));
        while (depthFirstIterator.hasNext()) {
            Vertex vertex = depthFirstIterator.next();
            if (vertex.isEdge()) {
                scheduledProject.setTasks(new ArrayList<>(taskMap.values()));
                projectSchedules.add(project);
                scheduledProject = new Project();
                scheduledProject.setScheduleStartDate(startDate);
                taskMap = project.getTasks()
                        .stream()
                        .collect(Collectors.toMap(Task::getId, Function.identity()));
            }
        }
        return projectSchedules;
    }

}
