package model.dto;

import model.Point;

public class MineSnakeDTO extends SnakeDTO {
    public String id;
    public Point direction;
    public Point oldDirection;
    public int deathCount;
    public int reviveRemainMs;
}
