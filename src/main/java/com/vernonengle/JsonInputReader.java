package com.vernonengle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

public class JsonInputReader {
    public Project getProjectDto(String path) throws IOException {
        File jsonFile = new File(path);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper.readValue(jsonFile, Project.class);
    }
}
