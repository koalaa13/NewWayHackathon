package model;

import model.dto.MineSnakeDTO;
import util.VecUtil;

import java.util.*;

public class PlayerSnake extends Snake {
    public String id;
    public String originId;
    public Vec direction;
    public ArrayDeque<Vec> moves;
    public boolean croppedGhost;

    public PlayerSnake() {
        super();
        moves = new ArrayDeque<>();
        croppedGhost = false;
    }

    public PlayerSnake(MineSnakeDTO source) {
        super(source);
        id = source.id;
        originId = id;
        direction = new Vec(source.direction);
        moves = new ArrayDeque<>();
        croppedGhost = false;
    }

    public void Move(Vec newDirection) {
        direction = newDirection;
        moves.addFirst(direction);
        while (moves.size() > body.size()) {
            moves.pollLast();
        }
        bodyOrder.forEach((cell, ord) -> ord++);
        bodyOrder.remove(body.getLast());
        Iterator<Vec> iterator = moves.iterator();
        body.replaceAll(vec -> vec.shift(iterator.next()));
        bodyOrder.put(body.getFirst(), 0);
    }

    public List<Vec> HeadPossibleDirections() {
        return VecUtil.turns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerSnake that = (PlayerSnake) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
