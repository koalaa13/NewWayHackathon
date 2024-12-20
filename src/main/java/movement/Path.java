package movement;

import model.Vec;

import java.util.List;

public class Path {
    public boolean exist;
    public List<Vec> steps;
    public List<Vec> cells;

    public Path() {

    }

    public Path(boolean exist, List<Vec> steps, List<Vec> cells) {
        this.exist = exist;
        this.steps = steps;
        this.cells = cells;
    }
}
