package model;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import controller.PointDeserializer;
import controller.PointSerializer;

@JsonDeserialize(using = PointDeserializer.class)
@JsonSerialize(using = PointSerializer.class)
public class Point {
    public List<Integer> coors;

    public Point(List<Integer> coors) {
        this.coors = coors;
    }

    @Override
    public String toString() {
        return coors.toString();
    }
}
