package movement;

import model.PlayerSnake;
import model.Vec;

import java.util.Objects;

public class PathState {
    public PlayerSnake snake;
    public long dist;
    public PathState parent;
    public Vec lastMove;

    public PathState(PlayerSnake snake, long dist, PathState parent, Vec lastMove) {
        this.snake = snake;
        this.dist = dist;
        this.parent = parent;
        this.lastMove = lastMove;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathState pathState = (PathState) o;
        return dist == pathState.dist &&
                Objects.equals(snake, pathState.snake) &&
                Objects.equals(parent, pathState.parent) &&
                Objects.equals(lastMove, pathState.lastMove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(snake, dist, parent, lastMove);
    }
}
