package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        var mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File(fileName), data);
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
    }
}
