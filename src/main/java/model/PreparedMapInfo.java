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
        snakes = mapInfoDTO.snakes.stream()
                .filter(s -> s.status.equals("alive")).map(PlayerSnake::new).toList();
        enemies = mapInfoDTO.enemies.stream()
                .filter(s -> s.status.equals("alive")).map(Snake::new).toList();
        food = mapInfoDTO.food.stream().map(Food::new).toList();
    }

    public Vec mapCenter() {
        return new Vec(mapSize.x / 2, mapSize.y / 2, mapSize.z / 2);
    }
}
