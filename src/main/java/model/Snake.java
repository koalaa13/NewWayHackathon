package model;

import model.dto.SnakeDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Snake {
    public List<Vec> body;
    public Map<Vec, Integer> bodyOrder;

    public Snake() {
        body = new ArrayList<>();
        bodyOrder = new HashMap<>();
    }

    public Snake(SnakeDTO source) {
        body = new ArrayList<>();
        int orderNum = 0;
        for (Point p : source.geometry) {
            body.add(new Vec(p));
            bodyOrder.put(body.getLast(), orderNum);
            orderNum++;
        }
    }

    public Vec Head() {
        return body.getFirst();
    }
}
