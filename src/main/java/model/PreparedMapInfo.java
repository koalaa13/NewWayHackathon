package model;

import model.dto.*;

import java.util.List;
import java.util.stream.Collectors;

public class PreparedMapInfo {
    public Vec mapSize;
    public List<Vec> fences;
    public List<PlayerSnake> snakes;
    public List<Snake> enemies;
    public List<Food> food;

    public PreparedMapInfo(MapInfoDTO mapInfoDTO) {
        mapSize = new Vec(mapInfoDTO.mapSize);
        fences = mapInfoDTO.fences.stream().map(Vec::new).toList();
        snakes = mapInfoDTO.snakes.stream().map(PlayerSnake::new).toList();
        enemies = mapInfoDTO.enemies.stream().map(Snake::new).toList();
        food = mapInfoDTO.food.stream().map(Food::new).toList();
    }
}
