package net.bytten.zosoko.player;

import java.util.ArrayList;
import java.util.List;

import net.bytten.zosoko.IPuzzle;
import net.bytten.zosoko.util.Bounds;
import net.bytten.zosoko.util.Coords;

public class PlayingPuzzle implements IPuzzle {

    IPuzzle puzzle;
    Coords playerPosition;
    List<Coords> boxPositions;
    
    public PlayingPuzzle(IPuzzle puzzle) {
        this.puzzle = puzzle;
        this.playerPosition = getPlayerStartPosition();
        this.boxPositions = new ArrayList<Coords>(getBoxStartPositions());
    }

    @Override
    public List<Coords> getBoxStartPositions() {
        return puzzle.getBoxStartPositions();
    }

    @Override
    public Coords getPlayerStartPosition() {
        return puzzle.getPlayerStartPosition();
    }

    @Override
    public Bounds getSize() {
        return puzzle.getSize();
    }

    @Override
    public Tile get(int x, int y) {
        return puzzle.get(x, y);
    }

    public Coords getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Coords playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<Coords> getBoxPositions() {
        return boxPositions;
    }

    @Override
    public boolean isBounded() {
        return puzzle.isBounded();
    }

}