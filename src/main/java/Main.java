import controller.Controller;
import model.dto.request.RequestDTO;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(true);
        var request = new RequestDTO();

        var mapInfo = controller.getMapInfo(request);
        System.out.println(mapInfo);
    }
}
