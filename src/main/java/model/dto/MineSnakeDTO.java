package model.dto;

import java.util.List;

public class MineSnakeDTO extends SnakeDTO {
    public String id;
    public List<Integer> direction;
    public List<Integer> oldDirection;
    public int deathCount;
    public int reviveRemainMs;
}
