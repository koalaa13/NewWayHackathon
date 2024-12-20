import java.util.List;

import controller.Controller;
import model.Point;
import model.dto.request.RequestDTO;
import model.dto.request.RequestItemDTO;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Controller controller = new Controller(true);
        var request = new RequestDTO();
        var mapInfo = controller.getMapInfo(request);
        var requestItem = new RequestItemDTO();
        requestItem.id = mapInfo.snakes.get(0).id;
        requestItem.direction = new Point(List.of(1, 0, 0));
        request.snakes = List.of(requestItem);

        Thread.sleep(1000);
        mapInfo = controller.getMapInfo(request);
        System.out.println(mapInfo.snakes.get(0).id);
        System.out.println(mapInfo.snakes.get(0).geometry);
    }
}
