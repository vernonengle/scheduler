package com.vernonengle;

import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ProjectScheduleGraph {

    private Project project;
    private DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);

    public ProjectScheduleGraph(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> getProjectGraph() {
        return projectGraph;
    }

    public void setProjectGraph(DefaultDirectedWeightedGraph<Vertex, DefaultWeightedEdge> projectGraph) {
        this.projectGraph = projectGraph;
    }
}
