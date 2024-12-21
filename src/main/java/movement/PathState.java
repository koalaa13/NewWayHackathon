package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PathState {
    public Vec snake;
    public long dist;
    public Vec firstDirection;
    public int stepsMade;

    public PathState(Vec snake) {
        this.snake = snake;
        this.dist = 0L;
        this.firstDirection = null;
        this.stepsMade = 0;
    }

    public PathState(Vec snake, long dist, Vec firstDirection, int stepsMade) {
        this.snake = snake;
        this.dist = dist;
        this.firstDirection = firstDirection;
        this.stepsMade = stepsMade;
    }

    public Path recover() {
        return new Path(true, dist, firstDirection);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathState pathState = (PathState) o;
        return dist == pathState.dist &&
                Objects.equals(snake, pathState.snake);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snake, dist);
    }
}
