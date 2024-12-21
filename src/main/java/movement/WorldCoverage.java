package movement;

import javafx.util.Pair;
import model.*;
import util.VecUtil;

import java.util.*;

public class WorldCoverage {
    private static final double MAX_ETEN_PROB = 0.8;

    public static class CellInfo {
        double probBusy = 0.0;
        boolean blocked = false;
        Food food = null;
        List<Integer> enemyDists = new ArrayList<>();

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

    private Pair<Integer, List<Food>> getBfsNearestFood(Vec point) {
        int distLimit = -1;
        List<Food> res = new ArrayList<>();

        int pos = 0;
        Set<Vec> used = new HashSet<>();
        List<Vec> queueP = new ArrayList<>();
        List<Integer> queueD = new ArrayList<>();
        used.add(point);
        queueD.add(0);
        queueP.add(point);
        while (pos < queueP.size()) {
            var d = queueD.get(pos);
            var e = queueP.get(pos);
            if (distLimit >= 0 && d > distLimit) break;
            var f = cellInfos.get(e).food;
            if (f != null) {
                distLimit = d;
                res.add(f);
            }
            pos++;
            for (var shift : VecUtil.turns) {
                var cand = e.shift(shift);
                if (!used.contains(cand) && e.inRange(min, max) && !cellInfos.get(cand).blocked) {
                    queueP.add(cand);
                    queueD.add(d + 1);
                }
            }
        }
        return new Pair<>(distLimit, res);
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
        mapInfo.food.forEach(f -> {
            if (f.c.inRange(min, max)) {
                cellInfos.get(f.c).food = f;
            }
        });
        mapInfo.fences.forEach(v -> cellInfos.get(v).setBlocked());
        mapInfo.snakes.forEach(s -> s.body.forEach(v -> cellInfos.get(v).setBlocked()));
        mapInfo.enemies.forEach(s -> s.body.forEach(v -> cellInfos.get(v).setBlocked()));
        mapInfo.enemies.forEach(s -> fillWithProb(s.body.getFirst()));
        mapInfo.enemies.forEach(s -> {
            var h = s.body.getFirst();
            var res = getBfsNearestFood(h);
            var foods = res.getValue();
            var dst = res.getKey();
            if (!foods.isEmpty()) {
                foods.sort(Comparator.comparingInt(o -> o.points));
                foods.forEach(f -> cellInfos.get(f.c).enemyDists.add(dst));
            }
        });
    }

    public Vec getDirection() {
        return new Vec(1, 0, 0);
    }
}
