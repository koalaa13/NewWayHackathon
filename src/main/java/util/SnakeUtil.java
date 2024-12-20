package util;

import model.PlayerSnake;
import model.Vec;

import java.util.ArrayList;
import java.util.List;

public class SnakeUtil {
    public static List<Vec> GenerateDirectionalMoves(PlayerSnake snake) {
        List<Vec> res = new ArrayList<>();
        res.add(snake.direction);
        if (snake.direction.x != 0) {
            res.addAll(VecUtil.turnsAlongX());
            if (snake.body.size() == 1) {
                res.add(VecUtil.XN);
            }
        }
        if (snake.direction.y != 0) {
            res.addAll(VecUtil.turnsAlongY());
            if (snake.body.size() == 1) {
                res.add(VecUtil.YN);
            }
        }
        if (snake.direction.z != 0) {
            res.addAll(VecUtil.turnsAlongZ());
            if (snake.body.size() == 1) {
                res.add(VecUtil.ZN);
            }
        }
        if (res.size() <= 1 || res.size() >= 7) {
            throw new RuntimeException(snake.id + ": Snake's direction is spoiled");
        }
        return res;
    }
}
