package movement;

import model.PlayerSnake;
import model.Point;
import model.PreparedMapInfo;
import model.Vec;

import java.util.Map;

public class WorldCoverage {
    private Map<Point, Double> probBusy;
    private Point min;
    private Point max;

    public WorldCoverage(PlayerSnake snake, PreparedMapInfo mapInfo) {

    }

    public Vec getDirection() {
        return new Vec(1, 0, 0);
    }
}
