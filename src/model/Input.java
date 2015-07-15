package model;

/**
 * Created by Adam on 2015-06-26.
 */
public class Input {
    public enum Action { WEST, EAST, NORTH, SOUTH, SHOOT, SPLIT };
    final public Action action;

    public Input(Action action) {
        this.action = action;
    }

    public boolean equals(Object o) {
        if (o instanceof Input) {
            Input input = (Input) o;
            if (this.action == input.action) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return action != null ? action.hashCode() : 0;
    }
}
