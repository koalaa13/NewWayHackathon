package model.dto;

import java.util.List;

import model.Point;

public class MapInfoDTO {
    public List<Integer> mapSize;
    public int points;
    public List<Point> fences;
    public List<MineSnakeDTO> snakes;
    public List<EnemySnakeDTO> enemies;
    public List<FoodDTO> food;
    public SpecialFoodDTO specialFood;
    public int turn;
    public int reviveTimeoutSec;
    public int tickRemainMs;
}
