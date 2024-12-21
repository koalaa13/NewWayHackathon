package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.Set;
import java.util.function.Function;

public class MoveMaker {
    private Function<Vec, Long> cellWeightCalculator;

    public MoveMaker(Function<Vec, Long> cellWeightCalculator) {
        this.cellWeightCalculator = cellWeightCalculator;
    }

    public boolean makeMoveTowardsTo(PlayerSnake snake, Vec destination) {
        if (snake.Head().equals(destination)) {
            return false;
        }
        Path path = Pathfinder.findPath(snake, destination, Set.of(), cellWeightCalculator);
        if (!path.exist) {
            return false;
        }
        snake.Move(path.steps.getFirst());
        return true;
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
