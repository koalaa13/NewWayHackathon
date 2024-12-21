package model;

import java.util.List;
import java.util.Objects;

public class Vec {
    public long x;
    public long y;
    public long z;

    public Vec(long x, long y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec(Vec v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vec(Point p) {
        this.x = p.coors.get(0);
        this.y = p.coors.get(1);
        this.z = p.coors.get(2);
    }

    public Vec(List<Integer> coords) {
        this.x = coords.get(0);
        this.y = coords.get(1);
        this.z = coords.get(2);
    }

    public Vec shift(Vec v) {
        return new Vec(x + v.x, y + v.y, z + v.z);
    }

    public long dist(Vec v) {
        return Math.abs(x - v.x) + Math.abs(y - v.y) + Math.abs(z - v.z);
    }

    public Vec diff(Vec v) {
        return new Vec(x - v.x, y - v.y, z - v.z);
    }

    public Point toPoint() {
        return new Point(List.of((int) x, (int) y, (int) z));
    }

    static int BC = 10;

    public Vec prevBlockStart() {
        return new Vec(
                Math.max(x / BC * BC - BC, 0),
                Math.max(y / BC * BC - BC, 0),
                Math.max(z / BC * BC - BC, 0));
    }

    public Vec nextBlockEnd(Vec mapSize) {
        return new Vec(
                Math.min(x / BC * BC + 2L * BC, mapSize.x) - 1,
                Math.min(y / BC * BC + 2L * BC, mapSize.y) - 1,
                Math.min(z / BC * BC + 2L * BC, mapSize.z) - 1);
    }

    public boolean inRange(Vec min, Vec max) {
        return min.x <= x && x <= max.x && min.y <= y && y <= max.y && min.z <= z && z <= max.z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec vec = (Vec) o;
        return x == vec.x && y == vec.y && z == vec.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Vec{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
