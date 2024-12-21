package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PathFinderTest {
    public static void main(String[] args) {
        PlayerSnake snake = new PlayerSnake();
        snake.id = "1";

        snake.body = new ArrayList<>();
        snake.body.add(new Vec(-2, 0, 0));
        //snake.body.add(new Vec(-2, 0, -1));

        snake.bodyOrder = new HashMap<>();
        snake.bodyOrder.put(snake.body.get(0), 0);
        //snake.bodyOrder.put(snake.body.get(1), 1);

        snake.direction = new Vec(-1, 0, 0);
        Path path = Pathfinder.findPath(snake, new Vec(3, 2, 1), Set.of(new Vec(3, 1, 1)), (Vec v) -> 1L);
        int kek = 0;
    }
}
