package com.vernonengle;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.traverse.DepthFirstIterator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProjectScheduleGraphService {
    public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> generateProjectScheduleGraph(Project project) throws InterruptedException {
        Set<Integer> remainingTasks = project.getTasks()
                .stream()
                .map(Task::getId)
                .collect(Collectors.toSet());
        Vertex topVertex = new Vertex();
        topVertex.setRemainingTasks(remainingTasks);

        Set<Integer> independentTasks = project.getTasks()
                .stream()
                .filter(task -> task.getDependencies().isEmpty())
                .map(Task::getId)
                .collect(Collectors.toSet());
        topVertex.setStartableTasks(independentTasks);

        Map<Integer, Task> taskMap = project.getTasks()
                .stream()
                .collect(Collectors.toMap(Task::getId, Function.identity()));
        topVertex.setTaskMap(taskMap);
        topVertex.setMaxPointsPerUnitTime(project.getCapacityPerUnitTime());

        DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph = buildProjectGraph(topVertex);

        return projectGraph;
    }

    public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> buildProjectGraph(Vertex topVertex) throws InterruptedException {
        DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        GraphBuilder<Vertex, DefaultWeightedEdge, DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge>> graphBuilder = new GraphBuilder<>(projectGraph);
        LinkedBlockingQueue<Vertex> vertexQueue = new LinkedBlockingQueue<>();
        vertexQueue.put(topVertex);

        while (!vertexQueue.isEmpty()) {
            Vertex currentVertex = vertexQueue.poll();
            Vertex previousVertex = currentVertex.getPreviousVertex();
            if (previousVertex == null) {
                System.out.println("==========================================");
                System.out.println("Adding Vertex:");
                System.out.println(currentVertex.toString());
                System.out.println("==========================================");
                graphBuilder.addVertex(currentVertex);
            } else {
                System.out.println("==========================================");
                System.out.println("Adding Edge Source:");
                System.out.println(previousVertex.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println("Adding Edge Target:");
                System.out.println(currentVertex.toString());
                System.out.println("==========================================");
                graphBuilder.addEdge(previousVertex, currentVertex, currentVertex.getEdgeWeight());
            }

            if (currentVertex.hasRemainingTasks()) {
                List<NewTaskVertex> nextVertices = getNextStartableTasks(currentVertex);
                if (nextVertices.isEmpty()) {
                    Vertex timeProgressVertex = createTimeProgressVertex(currentVertex);
                    timeProgressVertex.setPreviousVertex(currentVertex);
                    vertexQueue.put(timeProgressVertex);
                } else {
                    nextVertices.stream()
                            .forEach(vertex -> {
                                try {
                                    vertex.setPreviousVertex(currentVertex);
                                    vertexQueue.put(vertex);
                                } catch (InterruptedException e) {
                                }
                            });
                }
            }
        }

        return graphBuilder.build();
    }

    public List<NewTaskVertex> getNextStartableTasks(Vertex currentVertex) {
        List<NewTaskVertex> startableTasks = currentVertex.getStartableTasks()
                .stream()
                .map(id -> currentVertex.getTaskMap().get(id))
                .filter(nextTask ->
                        currentVertex.getMaxPointsPerUnitTime() >= currentVertex.getCurrentTaskPoints() + nextTask.getPoints()
                )
                .map(task -> new NewTaskVertex(task, currentVertex))
                .collect(Collectors.toList());
        return startableTasks;
    }

    public TimeProgressVertex createTimeProgressVertex(Vertex currentVertex) {
        int timeOffset = currentVertex.getTimeRemainingForTask().values()
                .stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
        return new TimeProgressVertex(currentVertex, timeOffset);
    }

    public Project assignSchedule(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph, Project project) {
        Project scheduledProject = new Project();
        LocalDate localDate = LocalDate.parse(project.getScheduleStartString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        project.setScheduleStartDate(localDate);
        DepthFirstIterator<Vertex, DefaultWeightedEdge> depthFirstIterator = new DepthFirstIterator<>(projectGraph);
        while(depthFirstIterator.hasNext()) {
            depthFirstIterator.next().toString();
        }
        return scheduledProject;
    }
}
