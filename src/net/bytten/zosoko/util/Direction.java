package net.bytten.zosoko.util;

/**
 * Represents a compass direction.
 */
public enum Direction {

    N(0,    0,-1),
    E(1,    1, 0),
    S(2,    0, 1),
    W(3,    -1,0);
    
    public static final int NUM_DIRS = 4;
    
    /**
     * The integer representation of this direction.
     */
    public final int code;
    public final int x, y;
    
    private Direction(int code, int x, int y) {
        this.code = code;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the direction completely opposite to this direction.
     * 
     * @return  the opposite direction to this
     */
    public Direction opposite() {
        switch (this) {
        case N: return S;
        case E: return W;
        case S: return N;
        case W: return E;
        default:
            // Should not occur
            throw new RuntimeException("Unknown direction");
        }
    }
    
    /**
     * Gets the Direction for a given integer representation of a direction.
     * 
     * @return the Direction
     * @see #code
     */
    public static Direction fromCode(int code) {
        switch (code) {
        case 0: return N;
        case 1: return E;
        case 2: return S;
        case 3: return W;
        default: return null;
        }
    }
}
