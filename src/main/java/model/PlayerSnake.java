package model;

import model.dto.MineSnakeDTO;
import util.SnakeUtil;

import java.util.*;

public class PlayerSnake extends Snake {
    public String id;
    public Vec direction;

    public ArrayDeque<Vec> moves;

    public PlayerSnake() {
        super();
        moves = new ArrayDeque<>();
    }

    public PlayerSnake(MineSnakeDTO source) {
        super(source);
        id = source.id;
        direction = new Vec(source.direction);
        moves = new ArrayDeque<>();
    }

    public PlayerSnake Clone() {
        PlayerSnake res = new PlayerSnake();
        res.id = id;
        res.direction = new Vec(direction);

        res.body = new ArrayList<>();
        for (Vec bodyPart : body) {
            res.body.add(new Vec(bodyPart));
        }

        res.moves = new ArrayDeque<>();
        for (Vec move : moves) {
            res.moves.add(new Vec(move));
        }

        return res;
    }

    public void Move(Vec newDirection) {
        direction = newDirection;
        moves.addFirst(direction);
        while (moves.size() > body.size()) {
            moves.pollLast();
        }
        Iterator<Vec> iterator = moves.iterator();
        body.replaceAll(vec -> vec.shift(iterator.next()));
    }

    /*public List<Vec> HeadPossibleMoves() {
        List<Vec> possibleMoves = SnakeUtil.GenerateDirectionalMoves(this);
        possibleMoves.replaceAll(vec -> Head().shift(vec));
        return possibleMoves;
    }*/

    public List<Vec> HeadPossibleDirections() {
        return SnakeUtil.GenerateDirectionalMoves(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerSnake that = (PlayerSnake) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(direction, that.direction) &&
                moves.containsAll(that.moves) &&
                that.moves.containsAll(moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, direction, moves);
    }

    // TODO: eat mandarin
}
