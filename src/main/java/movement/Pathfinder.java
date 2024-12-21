package movement;

import com.google.common.collect.MinMaxPriorityQueue;

import model.PlayerSnake;
import model.Snake;
import model.Vec;

import java.util.*;
import java.util.function.Function;

public class Pathfinder {
    private static final int MAX_QUEUE_SIZE = 1000;

    public static Path findPath(final PlayerSnake initialSnake, Vec finish, Set<Vec> obstacles, List<Snake> snakes, Function<Vec, Long> cellWeightCalculator) {
        PathState start = new PathState(initialSnake.HeadSnake());
        MinMaxPriorityQueue<PathState> queue = MinMaxPriorityQueue
                .orderedBy(Comparator.comparingLong((PathState a) -> a.dist).thenComparing((PathState a) -> finish.dist(a.snake.Head())))
                .maximumSize(MAX_QUEUE_SIZE)
                .create();
        queue.add(start);
        while (!queue.isEmpty()) {
            PathState curState = queue.poll();
            if (curState.snake.Head().equals(finish)) {
                return curState.recover();
            }
            for (Vec possibleDirection : curState.snake.HeadPossibleDirections()) {
                Vec possibleMove = curState.snake.Head().shift(possibleDirection);
                if (isDestinationOccupied(possibleMove, curState, obstacles, snakes)) {
                    continue;
                }
                long edgeDist = cellWeightCalculator.apply(possibleMove);
                PlayerSnake newSnake = curState.snake.HeadSnake();
                newSnake.Move(possibleDirection);
                PathState newState = new PathState(newSnake, curState.dist + edgeDist, curState, possibleDirection, possibleMove, curState.stepsMade + 1);
                queue.add(newState);
            }
        }
        return new Path(false, null, null);
    }

    private static boolean isDestinationOccupied(Vec dst, PathState state, Set<Vec> obstacles, List<Snake> snakes) {
        if (obstacles.contains(dst)) {
            return true;
        }
        boolean occupied = false;
        for (var snake : snakes) {
            if (snake.bodyOrder.containsKey(dst)) {
                int ordNum = snake.bodyOrder.get(dst);
                int shiftStep = snake.body.size() - ordNum + 1;
                occupied |= state.stepsMade < shiftStep;
            }
        }
        return occupied;
    }
}
