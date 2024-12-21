package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.Controller;
import controller.IController;
import controller.StaticFileController;
import model.PreparedMapInfo;
import model.dto.MapInfoDTO;
import model.dto.request.RequestDTO;
import model.dto.request.RequestItemDTO;
import movement.WorldCoverage;

public class Main {
    public static MapInfoDTO currentMapInfo = null;

    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException {
        IController controller = new Controller(true);
        while (true) {
            currentMapInfo = controller.getMapInfo();
            long startTime = System.currentTimeMillis();
            var preparedMapInfo = new PreparedMapInfo(currentMapInfo);
            var request = new RequestDTO();
            var snakesDirections = Collections.synchronizedList(new ArrayList<RequestItemDTO>());
            System.out.println(preparedMapInfo.snakes.size() + " alive snakes");
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            for (var snake : preparedMapInfo.snakes) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    var worldCoverage = new WorldCoverage(snake, preparedMapInfo);
                    var direction = worldCoverage.getDirection();
                    if (direction != null) {
                        var requestItem = new RequestItemDTO();
                        requestItem.id = snake.id;
                        requestItem.direction = direction.toPoint();
                        snakesDirections.add(requestItem);
                    }
                }, executorService);
                futures.add(future);
            }
            for (var future : futures) future.join();
            request.snakes = snakesDirections;
            System.out.println("Total time: " + (System.currentTimeMillis() - startTime));
            var mapAfterRequest = controller.getMapInfo(request);
            if (!mapAfterRequest.errors.isEmpty()) {
                System.out.println("Errors: " + String.join(" | ", mapAfterRequest.errors));
            }
            Thread.sleep(mapAfterRequest.tickRemainMs - 20);
        }
    }
}
