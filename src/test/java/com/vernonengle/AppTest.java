package com.vernonengle;

import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void shouldAnswerWithTrue() throws IOException, InterruptedException {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource("TaskList.json");
        App.main(new String[]{resource.getPath()});
    }
}
