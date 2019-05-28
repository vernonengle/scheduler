package com.vernonengle;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewTaskVertexTest {

    @Test
    public void constructorTest() {
        Vertex vertex = new Vertex();
        vertex.getRemainingTasks().add(1);
        vertex.setCurrentTaskPoints(3);
        Task task = new Task();
        NewTaskVertex vertex1 = new NewTaskVertex(task, vertex);
        vertex1.getCurrentTaskPoints();
    }
}