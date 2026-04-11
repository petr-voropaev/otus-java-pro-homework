package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        var mapper = new ObjectMapper();
        try (var inputStreamFile = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStreamFile == null) {
                throw new FileProcessException("File not found: " + fileName);
            }
            var type = mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class);
            return mapper.readValue(inputStreamFile, type);
        } catch (IOException ex) {
            throw new FileProcessException(ex);
        }
    }
}
