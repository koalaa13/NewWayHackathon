package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.*;
import java.util.function.Function;

public class Pathfinder {
    public static Path findPath(PlayerSnake snake, Vec finish, Set<Vec> obstacles, Function<Vec, Long> cellWeightCalculator) {
        PathState start = new PathState(snake.Clone(), 0L, null, null, null);
        PriorityQueue<PathState> queue = new PriorityQueue<>(Comparator.comparingLong((PathState a) -> a.dist));
        queue.add(start);
        while (!queue.isEmpty()) {
            PathState curState = queue.poll();
            if (curState.snake.Head().equals(finish)) {
                return curState.recover();
            }
            for (Vec possibleDirection : curState.snake.HeadPossibleDirections()) {
                Vec possibleMove = curState.snake.Head().shift(possibleDirection);
                if (isDestinationOccupied(possibleMove, curState, obstacles)) {
                    continue;
                }
                long edgeDist = cellWeightCalculator.apply(possibleMove);
                PlayerSnake newSnake = curState.snake.Clone();
                newSnake.Move(possibleDirection);
                PathState newState = new PathState(newSnake, curState.dist + edgeDist, curState, possibleDirection, possibleMove);
                queue.add(newState);
            }
        }
        return new Path(false, null, null);
    }

    private static boolean isDestinationOccupied(Vec dst, PathState state, Set<Vec> obstacles) {
        if (obstacles.contains(dst)) {
            return true;
        }
        for (Vec snakeBodyPart : state.snake.body) {
            if (dst.equals(snakeBodyPart)) {
                return true;
            }
        }
        return false;
    }
}
