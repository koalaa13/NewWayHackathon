package controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;
import model.dto.request.RequestItemDTO;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Controller implements IController {
    private static final String TOKEN = "dbc1fb7f-bc4e-438d-8a8d-f3ec945483c9";
    private static final String API_AUTH_HEADER = "X-Auth-Token";

    private boolean isTest;
    private ObjectMapper objectMapper;
    private boolean fromStaticFile;

    public Controller(boolean isTest) {
        this(isTest, false);
    }

    public Controller(boolean isTest, boolean fromStaticFile) {
        this.isTest = isTest;
        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.fromStaticFile = fromStaticFile;
    }

    private String getUrl() {
        return isTest ?
                "https://games-test.datsteam.dev" :
                "https://games.datsteam.dev";
    }

    private MapInfoDTO readFromStaticFile() {
        try (var reader = new BufferedReader(new FileReader("example_response.json"))) {
            JsonNode node = objectMapper.readTree(reader);
            return objectMapper.treeToValue(node, MapInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MapInfoDTO getMapInfo(RequestDTO requestDTO) {
        if (fromStaticFile) {
            return readFromStaticFile();
        }
        final String url = getUrl();
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            final String json = objectMapper.writeValueAsString(requestDTO);
            final StringEntity entity = new StringEntity(json);

            HttpPost request = new HttpPost(url + "/play/snake3d/player/move");
            request.setHeader(API_AUTH_HEADER, TOKEN);
            request.setEntity(entity);

            MapInfoDTO info = client.execute(request, response -> {
                var content = response.getEntity().getContent();
                JsonNode node = objectMapper.readTree(content);
                return objectMapper.treeToValue(node, MapInfoDTO.class);
            });
            return info;
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }

    public MapInfoDTO getMapInfo() {
        return getMapInfo(new RequestDTO());
    }
}
