package com.vernonengle;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class ProjectScheduleGraphServiceTest {


    @Test
    public void testVisualize() throws IOException, InterruptedException {
        ProjectScheduleGraphService projectScheduleGraphService = new ProjectScheduleGraphService();
        JsonInputReader jsonInputReader = new JsonInputReader();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("TaskList.json");
        Project project = jsonInputReader.getProjectDto(resource.getPath());
        Graph<Vertex, DefaultWeightedEdge> graph = projectScheduleGraphService.generateProjectScheduleGraph(project);
        JGraphXAdapter<Vertex, DefaultWeightedEdge> graphAdapter =
                new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File("src/test/resources/graph.png");
        ImageIO.write(image, "PNG", imgFile);

        assertTrue(imgFile.exists());
    }

    @Test
    public void testAssignSchedule() throws IOException, InterruptedException {
        ProjectScheduleGraphService projectScheduleGraphService = new ProjectScheduleGraphService();
        JsonInputReader jsonInputReader = new JsonInputReader();
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("TaskList.json");
        Project project = jsonInputReader.getProjectDto(resource.getPath());
        DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> graph = projectScheduleGraphService.generateProjectScheduleGraph(project);
        projectScheduleGraphService.assignSchedule(graph, project);
    }
}