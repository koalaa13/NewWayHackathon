package movement;

import com.google.common.collect.MinMaxPriorityQueue;

import model.PlayerSnake;
import model.Snake;
import model.Vec;

import java.util.*;
import java.util.function.Function;

public class Pathfinder {
    private static final int MAX_QUEUE_SIZE = 1000000;

    public static Map<Vec, Path> findPath(final PlayerSnake initialSnake, Set<Vec> destinations, Vec mapMin, Vec mapMax,
                                Set<Vec> obstacles, List<Snake> snakes, Function<Vec, Long> cellWeightCalculator) {
        PathState start = new PathState(initialSnake.HeadSnake());
        Map<Vec, Path> paths = new HashMap<>();
        Map<Vec, PathState> best = new HashMap<>();
        TreeSet<PathState> queue = new TreeSet<>(Comparator.comparingLong((PathState a) -> a.dist).thenComparing((PathState a) -> a.snake.id));
        /*MinMaxPriorityQueue<PathState> queue = MinMaxPriorityQueue
                .orderedBy(Comparator.comparingLong((PathState a) -> a.dist))
                .maximumSize(MAX_QUEUE_SIZE)
                .create();
        queue.add(start);*/
        queue.add(start);
        best.put(initialSnake.Head(), start);
        while (!queue.isEmpty()) {
            PathState curState = queue.removeFirst();
            //visited.add(curState.snake.Head());
            if (destinations.contains(curState.snake.Head())) {
                paths.put(curState.snake.Head(), curState.recover());
            }
            if (paths.size() >= destinations.size()) {
                break;
            }
            for (Vec possibleDirection : curState.snake.HeadPossibleDirections()) {
                Vec possibleMove = curState.snake.Head().shift(possibleDirection);
                if (isDestinationOccupied(possibleMove, curState, obstacles, snakes, mapMin, mapMax)) {
                    continue;
                }
                long edgeDist = cellWeightCalculator.apply(possibleMove);
                if (!best.containsKey(possibleMove) || (best.containsKey(possibleMove) && best.get(possibleMove).dist > curState.dist + edgeDist)) {
                    best.remove(possibleMove);
                    PlayerSnake newSnake = curState.snake.HeadSnake();
                    newSnake.Move(possibleDirection);
                    PathState newState = new PathState(newSnake, curState.dist + edgeDist, curState, possibleDirection, possibleMove, curState.stepsMade + 1);
                    queue.add(newState);
                    best.put(possibleMove, newState);
                }

            }
        }
        for (Vec dst : destinations) {
            if (!paths.containsKey(dst)) {
                paths.put(dst, new Path(false, -1L, null, null));
            }
        }
        return paths;
    }

    private static boolean isDestinationOccupied(Vec dst, PathState state, Set<Vec> obstacles,
                                                 List<Snake> snakes, Vec mapMin, Vec mapMax) {
        if (!dst.inRange(mapMin, mapMax)) {
            return true;
        }
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
