package com.vernonengle;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class App {


    public static void main(String[] args) throws IOException {
        JsonInputReader reader = new JsonInputReader();
        Project project = reader.getProjectDto(args[0]);
        ProjectScheduleGraphService service =  new ProjectScheduleGraphService();
        DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph = service.generateProjectScheduleGraph(project);
        List<Project> projectSchedules = service.getProjectSchedules(graph, project);
        AtomicInteger counter = new AtomicInteger(1);
        projectSchedules.stream()
                .forEach(projectSchedule -> {
                    int schedNumber = counter.getAndIncrement();
                    System.out.println("========Start of Schedule " + schedNumber + "==============");
                    projectSchedule.getTasks()
                            .stream()
                            .forEach(task -> System.out.println(task.toString()));
                    System.out.println("========End of Schedule " + schedNumber + "================");
                });
    }
}


