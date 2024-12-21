import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.Controller;
import controller.StaticFileController;
import model.PlayerSnake;
import model.Point;
import model.PreparedMapInfo;
import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;
import model.dto.request.RequestItemDTO;
import movement.WorldCoverage;

public class Main {
    public static MapInfoDTO currentMapInfo = null;

    public static void main(String[] args) throws InterruptedException {
        StaticFileController controller = new StaticFileController();
        while (true) {
            currentMapInfo = controller.getMapInfo();
            long startTime = System.currentTimeMillis();
            var preparedMapInfo = new PreparedMapInfo(currentMapInfo);
            var request = new RequestDTO();
            var snakesDirections = new ArrayList<RequestItemDTO>();
            System.out.println(preparedMapInfo.snakes.size() + " alive snakes");
            System.out.println("Got map");
            for (var snake : preparedMapInfo.snakes) {
                var worldCoverage = new WorldCoverage(snake, preparedMapInfo);
                System.out.println("Got world of " + snake.id);
                var requestItem = new RequestItemDTO();
                requestItem.id = snake.id;
                requestItem.direction = worldCoverage.getDirection().toPoint();
                System.out.println("Got direction of " + snake.id);
                snakesDirections.add(requestItem);
            }
            request.snakes = snakesDirections;
            System.out.println("Total time: " + (System.currentTimeMillis() - startTime));
            var mapAfterRequest = controller.getMapInfo(request);
            System.out.println("Errors: " + String.join(" | ", mapAfterRequest.errors));
            Thread.sleep(mapAfterRequest.tickRemainMs);
        }
    }
}
