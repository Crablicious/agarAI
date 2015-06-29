package model;

/**
 * Created by Adam on 2015-06-26.
 */
public class Input {
    public enum Dir { WEST, EAST, NORTH, SOUTH };
    final public Dir dir;

    public Input(Dir dir) {
        this.dir = dir;
    }

    public boolean equals(Object o) {
        if (o instanceof Input) {
            Input input = (Input) o;
            if (this.dir == input.dir) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return dir != null ? dir.hashCode() : 0;
    }
}
