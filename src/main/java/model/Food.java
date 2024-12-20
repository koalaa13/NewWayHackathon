package model;

import model.dto.FoodDTO;

public class Food {
    public Vec c;
    public int points;

    public Food(FoodDTO foodDTO) {
        c = new Vec(foodDTO.c);
        points = foodDTO.points;
    }

    public boolean suspicious() {
        return points == 0;
    }
}
