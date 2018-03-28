package martijn.quoridor.model;

public enum Notation {

    LAMEK, GLENDENNING, DEBUG;

    public static Notation parse(String notation) {
        notation = notation.toUpperCase();

        return valueOf(notation);
    }

    @Override
    public String toString() {
        String name = this.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
