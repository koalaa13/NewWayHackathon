package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;

public class StaticFileController implements IController {
    private ObjectMapper objectMapper;

    public StaticFileController() {
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private MapInfoDTO readFromStaticFile() {
        try (var reader = new BufferedReader(new FileReader("example_response.json"))) {
            JsonNode node = objectMapper.readTree(reader);
            return objectMapper.treeToValue(node, MapInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public MapInfoDTO getMapInfo(RequestDTO requestDTO) {
        return readFromStaticFile();
    }
}
