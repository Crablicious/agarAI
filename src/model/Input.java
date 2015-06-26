package model;

/**
 * Created by Adam on 2015-06-26.
 */
public class Input {
    public enum Dir { WEST, EAST, NORTH, SOUTH };
    final public Dir dir1;
    final public Dir dir2;

    public Input(Dir dir1, Dir dir2) {
        this.dir1 = dir1;
        this.dir2 = dir2;
    }

    public boolean contains(Dir dir){
        if ((dir1 == dir) || (dir2 == dir)) return true;
        return false;
    }

    public boolean equals(Object o) {
        if (o instanceof Input) {
            Input input = (Input) o;
            if (this.contains(input.dir1) && this.contains(input.dir2)) return true;
        }
        return false;
    }
}
