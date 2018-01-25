package martijn.quoridor.model;

public enum Wall {

    HORIZONTAL, VERTICAL;

    public static final Wall parse(String wall) {
        wall = wall.toUpperCase();

        if (wall.equals("H")) {
            wall = "HORIZONTAL";
        } else if (wall.equals("V")) {
            wall = "VERTICAL";
        }

        return valueOf(wall);
    }

    public Wall flip() {
        return values()[(ordinal() + 1) % 2];
    }

    public String notation() {
        if (this == HORIZONTAL) {
            return "h";
        } else {
            return "v";
        }
    }

}
