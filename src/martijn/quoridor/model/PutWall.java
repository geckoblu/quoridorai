package martijn.quoridor.model;

import martijn.quoridor.Config;

public class PutWall implements Move {

	private Position position;

	private Wall wall;

	public PutWall(Position position, Wall wall) {
		if (position == null || wall == null) {
			throw new NullPointerException();
		}
		this.position = position;
		this.wall = wall;
	}

	public Position getPosition() {
		return position;
	}

	public Wall getWall() {
		return wall;
	}

	/**
	 * Creates and returns a PutWall move at the same position but with the wall
	 * direction flipped.
	 */
	public PutWall flip() {
		return new PutWall(position, wall.flip());
	}

	@Override
	public void execute(Board board) {
		board.setWall(position, wall);
		board.getTurn().takeWall();
	}

	@Override
	public void undo(Board board) {
		board.getTurn().giveWall();
		board.setWall(position, null);
	}

	@Override
	public boolean isLegal(Board board) {
		// Does position exist on board?
		if (!board.containsWallPosition(position)) {
			return false;
		}

		// Is the game over?
		if (board.isGameOver()) {
			return false;
		}

		// Is there already a wall at the position?
		if (board.getWall(position) != null) {
			return false;
		}

		// Does the player have any walls left to place?
		if (board.getTurn().getWallCount() < 1) {
			return false;
		}

		// Does the wall clash with existing walls nearby?
		Position p1, p2;
		switch (wall) {
		case HORIZONTAL:
			p1 = position.west();
			p2 = position.east();
			break;
		case VERTICAL:
			p1 = position.north();
			p2 = position.south();
			break;
		default:
			throw new InternalError();
		}
		if (board.containsWallPosition(p1) && board.getWall(p1) == wall) {
			return false;
		}
		if (board.containsWallPosition(p2) && board.getWall(p2) == wall) {
			return false;
		}

		// Would the wall block a player from reaching their goal?
		try {
			execute(board);
			for (Player p : board.getPlayers()) {
				if (p.findGoal() == null) {
					// This player's has no way of reaching their goal anymore.
					return false;
				}
			}
		} finally {
			undo(board);
		}

		// Placing the wall is okay.
		return true;
	}

	@Override
	public String toString() {
		return "PutWall " + getWall().toString() + " at " + getPosition();
	}

	@Override
	public String notation() {
		if (Config.lamekNotation()) {
			return "" + (char)( 'a' + position.getX()) + (char)('1' + position.getY()) + wall.notation();
		} else {
			return "" + (char)( 'a' + position.getX()) + (char)('8' - position.getY()) + wall.notation();
		}
	}

}
