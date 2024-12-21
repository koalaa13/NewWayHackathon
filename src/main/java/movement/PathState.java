package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PathState {
    public PlayerSnake snake;
    public long dist;
    public PathState parent;
    public Vec lastMove;
    public Vec lastDirection;
    public int stepsMade;

    public PathState(PlayerSnake snake) {
        this.snake = snake;
        this.dist = 0L;
        this.parent = null;
        this.lastMove = null;
        this.lastDirection = null;
        this.stepsMade = 0;
    }

    public PathState(PlayerSnake snake, long dist, PathState parent, Vec lastMove, Vec lastDirection, int stepsMade) {
        this.snake = snake;
        this.dist = dist;
        this.parent = parent;
        this.lastMove = lastMove;
        this.lastDirection = lastDirection;
        this.stepsMade = stepsMade;
    }

    public Path recover() {
        List<Vec> steps = new ArrayList<>();
        List<Vec> cells = new ArrayList<>();
        PathState state = this;
        while (state.parent != null) {
            steps.add(state.lastMove);
            cells.add(state.lastDirection);
            state = state.parent;
        }
        Collections.reverse(steps);
        Collections.reverse(cells);
        return new Path(true, steps, cells);
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
