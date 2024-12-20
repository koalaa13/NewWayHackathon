package controller;

import java.util.ArrayList;
import java.util.List;

import model.Point;
import model.dto.EnemySnakeDTO;
import model.dto.MapInfoDTO;
import model.dto.MineSnakeDTO;
import model.dto.request.RequestDTO;

public class ControllerStub implements IController {
    @Override
    public MapInfoDTO getMapInfo(RequestDTO requestDTO) {
        var res = new MapInfoDTO();

        res.fences = new ArrayList<>();
        res.fences.add(new Point(List.of(0,0,0)));
        res.fences.add(new Point(List.of(0,0,1)));
        res.fences.add(new Point(List.of(0,0,2)));
        res.fences.add(new Point(List.of(0,0,3)));

        res.snakes = new ArrayList<>();
        var mineSnake = new MineSnakeDTO();
        mineSnake.geometry = new ArrayList<>();
        mineSnake.status = "alive";
        mineSnake.geometry.add(new Point(List.of(5, 5, 5)));
        res.snakes.add(mineSnake);

        res.enemies = new ArrayList<>();
        var enemySnake = new EnemySnakeDTO();
        enemySnake.geometry = new ArrayList<>();
        enemySnake.geometry.add(new Point(List.of(7, 7, 7)));
        res.enemies.add(enemySnake);

        return res;
    }
}
