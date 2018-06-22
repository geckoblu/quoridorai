package martijn.quoridor.model;

public class Utils {

    public final static Orientation[] getOrientationArray(String orientation) {

        String[] ao = orientation.trim().split(" ");

        Orientation[] res = new Orientation[ao.length];
        for (int i = 0; i < ao.length; i++) {
            res[i] = toOrientation(ao[i]);
        }

        return res;
    }

    private final static Orientation toOrientation(String shortOrientation) {
        switch (shortOrientation) {
        case "N":
            return Orientation.NORTH;
        case "S":
            return Orientation.SOUTH;
        case "E":
            return Orientation.EAST;
        case "W":
            return Orientation.WEST;
        default:
            return null;
        }
    }

    public final static Board getBoard() {
        return new Board();
    }

    public final static Board getBoard(String p0, String p1, String walls) {
        return getBoard(Notation.DEBUG, p0, p1, walls);
    }

    public final static Board getBoard(Notation notation, String p0, String p1, String walls) {

        Board board = new Board();

        if (p0 != null && p0.length() == 2) {
            Jump jump0 = new Jump(notation, p0);
            if (board.containsPlayerPosition(jump0.getPosition())) {
                board.getPlayer(0).setPosition(jump0.getPosition());
            } else {
                throw new Error("Not a valid position for player p0 " + p0);
            }
        }

        if (p1 != null && p1.length() == 2) {
            Jump jump1 = new Jump(notation, p1);
            if (board.containsPlayerPosition(jump1.getPosition())) {
                board.getPlayer(1).setPosition(jump1.getPosition());
            } else {
                throw new Error("Not a valid position for player p1 " + p1);
            }
        }

        if (walls != null && walls.length() > 3) {
            // TOD implement this
        }

        return board;
    }

}
