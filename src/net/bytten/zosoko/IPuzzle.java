package net.bytten.zosoko;

import java.util.List;

import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public interface IPuzzle {
    
    enum Tile {
        FLOOR, WALL, GOAL
    }
    
    public List<Coords> getBoxStartPositions();
    public Coords getPlayerStartPosition();
    
    public Bounds getSize();
    public Tile get(int x, int y);
    
}
