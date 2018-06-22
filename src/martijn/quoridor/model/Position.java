package martijn.quoridor.model;

import java.util.Objects;

public class Position implements Comparable<Position> {

    private final int _x;

    private final int _y;

    public Position(int x, int y) {
        super();
        this._x = x;
        this._y = y;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public Position north() {
        return new Position(_x, _y + 1);
    }

    public Position east() {
        return new Position(_x + 1, _y);
    }

    public Position south() {
        return new Position(_x, _y - 1);
    }

    public Position west() {
        return new Position(_x - 1, _y);
    }

    public Position move(Orientation orientation) {
        switch (orientation) {
        case NORTH:
            return north();
        case EAST:
            return east();
        case SOUTH:
            return south();
        case WEST:
            return west();
        default:
            throw new NullPointerException("orientation is null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position)) {
            return false;
        }
        Position that = (Position) o;
        return this._x == that._x && this._y == that._y;
    }

    @Override
    public int compareTo(Position o) {
        Position that = o;
        if (this._x != that._x) {
            return this._x - that._x;
        } else {
            return this._y - that._y;
        }
    }

    @Override
    public String toString() {
        return "(" + _x + ", " + _y + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(_x, _y);
    }

    public <E> E visit(E[][] matrix) {
        return matrix[_x][_y];
    }

}
