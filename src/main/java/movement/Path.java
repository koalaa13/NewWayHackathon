package movement;

import model.Vec;

import java.util.List;

public class Path {
    public boolean exist;
    public long dist;
    public Vec firstDirection;

    public Path() {

    }

    public Path(boolean exist, long dist, Vec firstDirection) {
        this.exist = exist;
        this.dist = dist;
        this.firstDirection = firstDirection;
    }
}
