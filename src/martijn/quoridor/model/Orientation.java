package martijn.quoridor.model;

public enum Orientation {

    NORTH, EAST, SOUTH, WEST;

    public Orientation opposite() {
        return values()[(ordinal() + 2) % 4];
    }

}
