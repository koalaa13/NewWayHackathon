package movement;

import model.PlayerSnake;
import model.Snake;
import model.Vec;
import util.VecUtil;

import java.util.*;
import java.util.function.Function;

public class Pathfinder {
    public static Map<Vec, Path> findPath(final PlayerSnake initialSnake, Set<Vec> destinations, Vec mapMin, Vec mapMax,
                                Set<Vec> obstacles, List<Snake> snakes, Function<Vec, Long> cellWeightCalculator) {
        PathState start = new PathState(initialSnake.Head());
        Map<Vec, Path> paths = new HashMap<>();
        Map<Vec, PathState> best = new HashMap<>();
        Queue<PathState> queue = new ArrayDeque<>();
        queue.add(start);
        best.put(initialSnake.Head(), start);
        while (!queue.isEmpty()) {
            PathState curState = queue.poll();
            if (destinations.contains(curState.snake)) {
                paths.put(curState.snake, curState.recover());
            }
            if (paths.size() >= destinations.size()) {
                break;
            }
            for (Vec possibleDirection : VecUtil.turns) {
                Vec possibleMove = curState.snake.shift(possibleDirection);
                if (isDestinationOccupied(possibleMove, curState, obstacles, snakes, mapMin, mapMax)) {
                    continue;
                }
                long edgeDist = 1L;
                if (!best.containsKey(possibleMove) || (best.containsKey(possibleMove) && best.get(possibleMove).dist > curState.dist + edgeDist)) {
                    PathState newState = new PathState(
                            possibleMove,
                            curState.dist + edgeDist,
                            curState.firstDirection == null ? possibleDirection : curState.firstDirection,
                            curState.stepsMade + 1);
                    queue.add(newState);
                    best.put(possibleMove, newState);
                }

            }
        }
        /*for (Vec dst : destinations) {
            if (!paths.containsKey(dst)) {
//                paths.put(dst, new Path(false, -1L, null, null));
            }
        }*/
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
