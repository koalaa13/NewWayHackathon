package movement;

import model.Vec;

import java.util.List;

public class Path {
    public boolean exist;
    public long dist;
    public List<Vec> steps;
    public List<Vec> cells;

    public Path() {

    }

    public Path(boolean exist, long dist, List<Vec> steps, List<Vec> cells) {
        this.exist = exist;
        this.dist = dist;
        this.steps = steps;
        this.cells = cells;
    }
}
