package martijn.quoridor.model;

import static java.lang.Math.abs;

public class Position implements Comparable<Position> {

	private int x;

	private int y;

	public Position(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Position north() {
		return new Position(x, y + 1);
	}

	public Position east() {
		return new Position(x + 1, y);
	}

	public Position south() {
		return new Position(x, y - 1);
	}

	public Position west() {
		return new Position(x - 1, y);
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
		Position that = (Position) o;
		return this.x == that.x && this.y == that.y;
	}

	@Override
	public int compareTo(Position o) {
		Position that = o;
		if (this.x != that.x) {
			return this.x - that.x;
		} else {
			return this.y - that.y;
		}
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	public <E> E visit(E[][] matrix) {
		return matrix[x][y];
	}

	public int manhattan(Position pos) {
		return abs(pos.x - x) + abs(pos.y - y);
	}

}
