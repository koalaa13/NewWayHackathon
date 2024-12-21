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
        int unblockTime = -1;

        public void setBlocked(int unblockTime) {
            probBusy = 1.0;
            blocked = true;
            this.unblockTime = unblockTime;
        }

        public void addAnotherEvent(double prob) {
            if (blocked) return;
            probBusy = 1 - (1 - probBusy) * (1 - prob);
        }
    }

    private PlayerSnake humanSnake;
    private Map<Vec, CellInfo> cellInfos;
    private PreparedMapInfo savedMapInfo;
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

    private long pow3(long n) {
        if (n > 10) return pow3(10);
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
        if (!checkMinMax(point)) {
            return new Pair<>(distLimit, res);
        }
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
                if (!used.contains(cand) && cand.inRange(min, max) && !cellInfos.get(cand).blocked) {
                    queueP.add(cand);
                    queueD.add(d + 1);
                    used.add(cand);
                }
            }
        }
        return new Pair<>(distLimit, res);
    }

    private boolean checkMinMax(Vec v) {
        return v.inRange(min, max);
    }

    private void setBlockBySnake(Snake snake) {
        for (int idx = 0; idx < snake.body.size(); idx++) {
            var c = snake.body.get(idx);
            if (checkMinMax(c)) {
                cellInfos.get(c).setBlocked(snake.body.size() - idx - 1);
            }
        }
    }

    public WorldCoverage(PlayerSnake snake, PreparedMapInfo mapInfo) {
        var head = snake.Head();
        min = head.prevBlockStart();
        max = head.nextBlockEnd(mapInfo.mapSize);
        humanSnake = snake;
        savedMapInfo = mapInfo;
        cellInfos = new HashMap<>();
        for (long x = min.x; x <= max.x; x++) {
            for (long y = min.y; y <= max.y; y++) {
                for (long z = min.z; z <= max.z; z++) {
                    cellInfos.put(new Vec(x, y, z), new CellInfo());
                }
            }
        }
        mapInfo.food.forEach(f -> {
            if (checkMinMax(f.c)) {
                cellInfos.get(f.c).food = f;
            }
        });
        mapInfo.fences.forEach(f -> {
            if (checkMinMax(f)) {
                cellInfos.get(f).setBlocked(10000000);
            }
        });
        setBlockBySnake(snake);
        mapInfo.enemies.forEach(this::setBlockBySnake);
        mapInfo.enemies.forEach(s -> fillWithProb(s.body.getFirst()));
        mapInfo.enemies.forEach(s -> {
            var h = s.body.getFirst();
            var res = getBfsNearestFood(h);
            var foods = res.getValue();
            var dst = res.getKey();
            if (!foods.isEmpty()) {
//                foods.sort(Comparator.comparingInt(o -> o.points));
                foods.forEach(f -> cellInfos.get(f.c).enemyDists.add(dst));
            }
        });
    }

    public Pair<Vec, Pair<Integer, Double>> getPathProps(CellInfo cell, Path path) {
        long cntBefore = cell.enemyDists.stream().filter(d -> d <= path.dist).count();
        double prior = 1.0 / pow3(Math.min(cntBefore, 4L));
        return new Pair<>(path.firstDirection, new Pair<>((int) path.dist, prior));
    }

    public Vec moveToVec(Vec p, boolean notNear) {
        Vec diff = p.diff(humanSnake.Head());
        if (notNear) {
            long dst = p.dist(humanSnake.Head());
            if (dst < 80 + new Random().nextInt(20)) {
                return null;
            }
        }
        List<Vec> cands = new ArrayList<>();
        if (diff.x > 0) cands.add(VecUtil.XP); else if (diff.x < 0) cands.add(VecUtil.XN);
        if (diff.y > 0) cands.add(VecUtil.YP); else if (diff.y < 0) cands.add(VecUtil.YN);
        if (diff.z > 0) cands.add(VecUtil.ZP); else if (diff.z < 0) cands.add(VecUtil.ZN);
        for (var cand : cands) {
            var sp = humanSnake.Head().shift(cand);
            var v = cellInfos.get(sp);
            if (v != null && !v.blocked) {
                System.out.println("Move to the p " + p);
                return cand;
            }
        }
        return null;
    }

    public Vec getDirection() {
        Vec moveToC = moveToVec(savedMapInfo.mapCenter(), true);
        if (moveToC != null) {
            return moveToC;
        }
        List<CellInfo> allFood = new ArrayList<>();
        for (var cell : cellInfos.values()) {
            if (cell.food != null && !cell.blocked) {
                allFood.add(cell);
            }
        }
        var moveMaker = new MoveMaker(v -> {
            var t = cellInfos.get(v);
            if (t != null) {
                return 1L + (long) (cellInfos.get(v).probBusy * 4L);
            } else {
                return 100L;
            }
        });
        var foodCoords = allFood.stream().map(c -> c.food.c).toList();
        var paths = moveMaker.makeMoveTowardsTo(humanSnake, min, max, foodCoords, savedMapInfo);
        CellInfo best = null;
        double bestReward = 0.0;
        Pair<Vec, Pair<Integer, Double>> bestDir = null;
        if (paths.size() <= 1) {
            System.out.println("Paths found for " + paths.size() + "/" + allFood.size() + " foods");
        }
        for (var cell : allFood) {
            if (!paths.containsKey(cell.food.c)) continue;
            var res = getPathProps(cell, paths.get(cell.food.c));
            double reward = (cell.food.points + 5 - res.getValue().getKey()) * res.getValue().getValue();
            if (best == null || reward > bestReward) {
                best = cell;
                bestDir = res;
                bestReward = reward;
            }
        }
        if (best == null) {
            System.out.println("Best direction is not defined. Choose any safe");
            var mn = savedMapInfo.food.stream().min((o1, o2) ->
                    Long.compare(o1.c.dist(humanSnake.Head()), o2.c.dist(humanSnake.Head())));
            if (mn.isPresent()) {
                var emp = mn.get();
                var re = moveToVec(emp.c, false);
                if (re != null) {
                    System.out.println("MOVE TO ANY FOOD");
                    return re;
                }
            }
            for (var turn : VecUtil.turns) {
                var sp = humanSnake.Head().shift(turn);
                var v = cellInfos.get(sp);
                if (v != null && !v.blocked) {
                    return turn;
                }
            }
            System.out.println("ASSERT. NO MOVES. JUST SKIP");
            return null;
        } else {
//            System.out.println("Selected food: dist=" + bestDir.getValue().getKey());
            {
                var sp = humanSnake.Head().shift(bestDir.getKey());
                var v = cellInfos.get(sp);
                if (v != null && v.blocked) {
                    System.out.println("ASSERT. GO TO WALL");
                }
            }
            return bestDir.getKey();
        }
    }
}
