package net.bytten.zosoko.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.bytten.zosoko.Tile;
import net.bytten.zosoko.util.Coords;
import net.bytten.zosoko.util.Direction;

public class FarthestStateFinder {
    
    protected PuzzleMap map;
    protected boolean bounded;

    public FarthestStateFinder(boolean bounded) {
        this.bounded = bounded;
    }
    
    protected PuzzleState chooseBest(Set<PuzzleState> states) {
        int bestScore = 0;
        PuzzleState best = null;
        for (PuzzleState state: states) {
            if (state.getScore() > bestScore) {
                bestScore = state.getScore();
                best = state;
            }
        }
        return best;
    }
    
    protected Set<PuzzleState> makeStartSet(List<Coords> goals) {
        Set<PuzzleState> states = new TreeSet<PuzzleState>();
        // TODO put player into each available space partition. At the moment
        // it only places the player into one area
        states.add(new PuzzleState.Builder(map, goals)
            .setBoxes(goals)
            .setPath(null)
            .setPlayer(PuzzleMap.getAnyFloorTile(map))
            .build());
        return states;
    }
    
    protected PuzzleState derive(PuzzleState state, int box, Direction pull) {
        List<Coords> boxes = state.getBoxes(),
                newBoxes = new ArrayList<Coords>(boxes);
        Coords newBoxPos = boxes.get(box).add(pull.x, pull.y),
               playerPos = newBoxPos.add(pull.x, pull.y);
        newBoxes.set(box, newBoxPos);
        
        ActionPath action = new ActionPath(state.getPath(), box, pull);
        return new PuzzleState.Builder(state.getMap(), state.getGoals())
            .setPath(action)
            .setBoxes(newBoxes)
            .setPlayer(playerPos)
            .build();
    }
    
    protected Set<PuzzleState> expand(PuzzleState state) {
        Set<PuzzleState> states = new TreeSet<PuzzleState>();
        
        PlayerCloud player = state.getPlayer();
        for (int boxnum = 0; boxnum < state.getBoxes().size(); ++boxnum) {
            Coords box = state.getBoxes().get(boxnum);
            if (player.canReach(box)) {
                for (Direction d: Direction.values()) {
                    // Since we're working backwards from the goal, the player
                    // _pulls_ boxes
                    Coords nextBoxPos = box.add(d.x,d.y),
                           nextPlayerPos = nextBoxPos.add(d.x,d.y);
                    if (!player.canReach(nextBoxPos)) continue;
                    if (state.getBoxes().contains(nextPlayerPos) ||
                            state.getBoxes().contains(nextBoxPos) ||
                            !state.getMap().getBounds().contains(nextBoxPos) ||
                            !state.getMap().getBounds().contains(nextPlayerPos) ||
                            state.getMap().getTile(nextBoxPos.x, nextBoxPos.y)
                                    != Tile.FLOOR ||
                            state.getMap().getTile(nextPlayerPos.x, nextPlayerPos.y)
                                    == Tile.WALL) {
                        continue;
                    }
                    
                    states.add(derive(state, boxnum, d));
                }
            }
        }
        
        return states;
    }
    
    protected Set<PuzzleState> expand(Set<PuzzleState> states) {
        Set<PuzzleState> newStates = new TreeSet<PuzzleState>();
        for (PuzzleState state: states) {
            newStates.addAll(expand(state));
        }
        return newStates;
    }
    
    // Known as 'Try' in the paper...
    protected Set<PuzzleState> deepen(Set<PuzzleState> startSet,
            Set<PuzzleState> prevResults, int depth) {
        
        Set<PuzzleState> resultSet = expand(prevResults);
        Set<PuzzleState> tempSet = startSet;
        for (int i = 1; i < depth; ++i) {
            resultSet.removeAll(tempSet);
            tempSet = expand(tempSet);
        }
        return resultSet;
        
    }

    public PuzzleState go(PuzzleMap map, List<Coords> goals) {
        this.map = map;
        for (Coords goal: goals) {
            map.setTile(goal.x, goal.y, Tile.GOAL);
        }
        try {
            
            Set<PuzzleState> startSet = makeStartSet(goals);
            Set<PuzzleState> resultSet = startSet, prevSet = null;
            for (int depth = 1; resultSet.size() > 0; ++depth) {
                prevSet = resultSet;
                resultSet = deepen(startSet, resultSet, depth);
            }
            assert prevSet != null;
            return chooseBest(prevSet);
        } finally {
            // Clear up afterwards: remove the goals
            for (Coords goal: goals) {
                map.setTile(goal.x, goal.y, Tile.FLOOR);
            }
        }
    }

}
