package movement;

import model.PlayerSnake;
import model.PreparedMapInfo;
import model.Snake;
import model.Vec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MoveMaker {
    private Function<Vec, Long> cellWeightCalculator;

    public MoveMaker(Function<Vec, Long> cellWeightCalculator) {
        this.cellWeightCalculator = cellWeightCalculator;
    }

    public Path makeMoveTowardsTo(PlayerSnake snake, Vec min, Vec max, List<Vec> destination, PreparedMapInfo mapInfo) {
        if (snake.Head().equals(destination)) {
            return null;
        }
        var allSnakes = new ArrayList<Snake>();
        allSnakes.addAll(mapInfo.snakes);
        allSnakes.addAll(mapInfo.enemies);
        Map<Vec, Path> paths = Pathfinder.findPath(
                snake,
                new HashSet<Vec>(destination),
                min,
                max,
                new HashSet<>(mapInfo.fences),
                allSnakes,
                cellWeightCalculator);
        /*if (!path.exist) {
            return null;
        }
//        snake.Move(path.steps.getFirst());
        return path;*/
        return null;
    }

    public static boolean makeRandomPossibleMove(PlayerSnake snake) {
        for (Vec possibleDirection : snake.HeadPossibleDirections()) {
            Vec possibleMove = snake.Head().shift(possibleDirection);
            if (isPossibleToMove(possibleMove, snake)) {
                snake.Move(possibleDirection);
                return true;
            }
        }
        return false;
    }

    private static boolean isPossibleToMove(Vec cell, PlayerSnake snake) {
        if (!snake.bodyOrder.containsKey(cell)) {
            // add check map objects
            return true;
        } else {
            int ordNum = snake.bodyOrder.get(cell);
            if (ordNum + 1 == snake.body.size()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
