package model;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(coors.get(0), point.coors.get(0)) &&
                Objects.equals(coors.get(1), point.coors.get(1)) &&
                Objects.equals(coors.get(2), point.coors.get(2));
    }

    @Override
    public int hashCode() {
        return Objects.hash(coors.get(0), coors.get(1), coors.get(2));
    }
}
