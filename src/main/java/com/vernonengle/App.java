package com.vernonengle;

import java.io.IOException;

public class App {


    public static void main(String[] args) throws IOException, InterruptedException {
        JsonInputReader reader = new JsonInputReader();
        Project project = reader.getProjectDto(args[0]);
        ProjectScheduleGraph projectScheduleGraph = new ProjectScheduleGraph(project);
        ProjectScheduleGraphService projectScheduleGraphService = new ProjectScheduleGraphService();
        projectScheduleGraph.setProjectGraph(projectScheduleGraphService.generateProjectScheduleGraph(project));
    }
}


