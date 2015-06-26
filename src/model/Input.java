package model;

/**
 * Created by Adam on 2015-06-26.
 */
public class Input {
    public enum Dir { WEST, EAST, NORTH, SOUTH, NORTHWEST, NORTHEAST, SOUTHWEST, SOUTHEAST };
    final public Dir dir;

    public Input(Dir dir) {
        this.dir = dir;

    }

    public boolean contains(Dir dir){
        if (this.dir == dir) return true;
        return false;
    }

    public boolean equals(Object o) {
        if (o instanceof Input) {
            Input input = (Input) o;
            if (this.contains(input.dir)) return true;
        }
        return false;
    }
}
