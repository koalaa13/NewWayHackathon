package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.*;

public class PathFinderTest {
    public static void main(String[] args) {
        PlayerSnake snake = new PlayerSnake();
        snake.id = "1";

        snake.body = new ArrayList<>();
        snake.body.add(new Vec(-7, -7, -7));
        //snake.body.add(new Vec(-2, 0, -1));

        snake.bodyOrder = new HashMap<>();
        snake.bodyOrder.put(snake.body.get(0), 0);
        //snake.bodyOrder.put(snake.body.get(1), 1);

        snake.direction = new Vec(-1, 0, 0);

        long start = System.currentTimeMillis();
        Map<Vec, Path> paths = Pathfinder.findPath(
                snake,
                Set.of(new Vec(8, 7, 8)),
                new Vec(-10, -10, -10),
                new Vec(10, 10, 10),
                Set.of(new Vec(3, 1, 1)),
                List.of(), (Vec v) -> 1L);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        int kek = 0;
    }
}
