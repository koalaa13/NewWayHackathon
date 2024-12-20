import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import controller.Controller;
import model.PlayerSnake;
import model.Point;
import model.PreparedMapInfo;
import model.dto.request.RequestDTO;
import model.dto.request.RequestItemDTO;
import movement.WorldCoverage;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller(true, true);
        while (true) {
            var mapInfo = controller.getMapInfo();
            var preparedMapInfo = new PreparedMapInfo(mapInfo);
            var request = new RequestDTO();
            var snakesDirections = new ArrayList<RequestItemDTO>();
            for (var snake : mapInfo.snakes) {
                var worldCoverage = new WorldCoverage(new PlayerSnake(snake), preparedMapInfo);
                if (snake.status.equals("dead")) {
                    System.out.println("Snake " + snake.id + " dead. Skipped");
                    continue;
                }
                var requestItem = new RequestItemDTO();
                requestItem.id = snake.id;
                requestItem.direction = worldCoverage.getDirection().toPoint();
                snakesDirections.add(requestItem);
            }
            request.snakes = snakesDirections;
            var mapAfterRequest = controller.getMapInfo(request);
            System.out.println("Errors: " + String.join(" | ", mapAfterRequest.errors));
            Thread.sleep(mapAfterRequest.tickRemainMs);
        }
    }
}
