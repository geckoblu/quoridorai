package martijn.quoridor.model;

public class PositionSet {

    private boolean[][] _flags;

    public PositionSet(Board board) {
        this(Board.SIZE, Board.SIZE);
    }

    public PositionSet(int width, int height) {
        _flags = new boolean[width][height];
    }

    public void add(Position pos) {
        _flags[pos.getX()][pos.getY()] = true;
    }

    public boolean contains(Position pos) {
        return _flags[pos.getX()][pos.getY()];
    }

    public void remove(Position pos) {
        _flags[pos.getX()][pos.getY()] = false;
    }

}
