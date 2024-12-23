package util;

import model.Vec;

import java.util.List;

public class VecUtil {
    public static final Vec XP = new Vec(1, 0, 0);
    public static final Vec XN = new Vec(-1, 0, 0);
    public static final Vec YP = new Vec(0, 1, 0);
    public static final Vec YN = new Vec(0, -1, 0);
    public static final Vec ZP = new Vec(0, 0, 1);
    public static final Vec ZN = new Vec(0, 0, -1);

    public static List<Vec> turns = List.of(XP, XN, YP, YN, ZP, ZN);
}
