package model;

import model.dto.MineSnakeDTO;
import util.SnakeUtil;
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

    public PlayerSnake Clone() {
        PlayerSnake res = new PlayerSnake();
        res.id = originId + "_Clone_" + UUID.randomUUID();
        res.direction = new Vec(direction);

        res.body = new ArrayList<>();
        for (Vec bodyPart : body) {
            res.body.add(new Vec(bodyPart));
        }

        res.bodyOrder = new HashMap<>();
        for (Map.Entry<Vec, Integer> bodyPart : bodyOrder.entrySet()) {
            res.bodyOrder.put(new Vec(bodyPart.getKey()), bodyPart.getValue());
        }

        res.moves = new ArrayDeque<>();
        for (Vec move : moves) {
            res.moves.add(new Vec(move));
        }

        res.croppedGhost = croppedGhost;

        return res;
    }

    public PlayerSnake HeadSnake() {
        PlayerSnake res = new PlayerSnake();
        res.id = originId + "_Head_" + UUID.randomUUID();
        res.direction = new Vec(direction);
        res.body = new ArrayList<>();
        res.body.add(body.getFirst());
        res.bodyOrder = new HashMap<>();
        res.bodyOrder.put(body.getFirst(), 0);
        res.moves = new ArrayDeque<>();
        if (!moves.isEmpty()) {
            res.moves.add(moves.getFirst());
        }
        res.croppedGhost = body.size() > 1;
        return res;
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
