package movement;

import model.Vec;

import java.util.List;

public class Path {
    public boolean exist;
    public List<Vec> steps;

    public Path() {

    }

    public Path(boolean exist, List<Vec> steps) {
        this.exist = exist;
        this.steps = steps;
    }
}
