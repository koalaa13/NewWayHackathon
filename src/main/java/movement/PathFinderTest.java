package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.ArrayList;
import java.util.Set;

public class PathFinderTest {
    public static void main(String[] args) {
        PlayerSnake snake = new PlayerSnake();
        snake.id = "1";
        snake.body = new ArrayList<>();
        snake.body.add(new Vec(0, 0, 0));
        snake.direction = new Vec(1, 0, 0);
        Path path = Pathfinder.findPath(snake, new Vec(3, 2, 1), Set.of(new Vec(3, 1, 1)), (Vec v) -> 1L);
        int kek = 0;
    }
}
