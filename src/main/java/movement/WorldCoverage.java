package movement;

import model.PlayerSnake;
import model.Point;
import model.PreparedMapInfo;
import model.Vec;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class WorldCoverage {
    public class CellInfo {
        double probBusy = 0.0;
        boolean blocked = false;

        public void setBlocked() {
            probBusy = 1.0;
            blocked = true;
        }

        public void addAnotherEvent(double prob) {
            if (blocked) return;
            probBusy = 1 - (1 - probBusy) * (1 - prob);
        }
    }

    private Map<Vec, CellInfo> cellInfos;
    private Vec min;
    private Vec max;

    private long fact(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= i;
        }
        return res;
    }

    private long C(int n, int k) {
        return fact(n) / (fact(k) * fact(n - k));
    }

    private long pow3(int n) {
        long res = 1;
        for (int i = 1; i <= n; i++) {
            res *= 3;
        }
        return res;
    }

    public void fillWithProb(Vec point) {
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -2; k <= 2; k++) {
                    if (i == 0 && j == 0 && k == 0) continue;
                    var totalIJ = Math.abs(i) + Math.abs(j);
                    var total = totalIJ + Math.abs(k);
                    var prob = (C(total, Math.abs(k)) * C(totalIJ, Math.abs(i)) * 1.0) / pow3(total);
                    if (prob < -0.01 || prob > 1.01) {
                        throw new RuntimeException("Bad probability: " + prob + " " + i + " " + j + " " + k);
                    }
                    var sp = point.shift(new Vec(i, j, k));
                    var v = cellInfos.get(sp);
                    if (v != null) v.addAnotherEvent(prob);
                }
            }
        }
    }

    public WorldCoverage(PlayerSnake snake, PreparedMapInfo mapInfo) {
        var head = snake.Head();
        min = head.prevBlockStart();
        max = head.nextBlockEnd(mapInfo.mapSize);
        cellInfos = new HashMap<>();
        for (long x = min.x; x <= max.x; x++) {
            for (long y = min.y; y <= max.y; y++) {
                for (long z = min.z; z <= max.z; z++) {
                    cellInfos.put(new Vec(x, y, z), new CellInfo());
                }
            }
        }
        mapInfo.fences.forEach(v -> cellInfos.get(v).setBlocked());
        mapInfo.snakes.forEach(s -> s.body.forEach(v -> cellInfos.get(v).setBlocked()));
        mapInfo.enemies.forEach(s -> s.body.forEach(v -> cellInfos.get(v).setBlocked()));
        mapInfo.enemies.forEach(s -> fillWithProb(s.body.getFirst()));
    }

    public Vec getDirection() {
        return new Vec(1, 0, 0);
    }
}
