package model;

import model.dto.SnakeDTO;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public List<Vec> body;

    public Snake() {
        body = new ArrayList<>();
    }

    public Snake(SnakeDTO source) {
        body = new ArrayList<>();
        for (Point p : source.geometry) {
            body.add(new Vec(p));
        }
    }

    public Vec Head() {
        return body.getFirst();
    }
}
