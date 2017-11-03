package martijn.quoridor.model;

public class PositionSet {

	private boolean[][] flags;

	public PositionSet(Board board) {
		this(board.getWidth(), board.getHeight());
	}

	public PositionSet(int width, int height) {
		flags = new boolean[width][height];
	}

	public void add(Position pos) {
		flags[pos.getX()][pos.getY()] = true;
	}

	public boolean contains(Position pos) {
		return flags[pos.getX()][pos.getY()];
	}

	public void remove(Position pos) {
		flags[pos.getX()][pos.getY()] = false;
	}

}
