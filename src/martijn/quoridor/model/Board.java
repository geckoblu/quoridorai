package martijn.quoridor.model;

import java.awt.Color;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Board {

	private static final int NPLAYERS = 2;

	private int width;

	private int height;

	private Wall[][] walls;

	private Player[] players;

	private int turn;

	private LinkedList<Move> _history;
	private int _historyIndex;

	private List<BoardListener> listeners;

	// Initialization.

	/** Creates a new 9x9 board with two players. */
	public Board() {
		this(9);
	}

	/** Creates a board of the specified size. */
	public Board(int size) {
		this(size, size);
	}

	private Board(int width, int height) {
		this.width = width;
		this.height = height;

		players = new Player[NPLAYERS];
		_history = new LinkedList<Move>();
		_historyIndex = 0;
		listeners = new LinkedList<BoardListener>();

		newGame();
	}

	/** Starts a new game, clearing the history. */
	public void newGame() {
		// Clear walls.
		walls = new Wall[width - 1][height - 1];

		// Create fresh players.
		Color[] cs = createPlayerColors();
		players[0] = new Player(this, Orientation.SOUTH, "Player 1", 10, cs[0]);
		players[1] = new Player(this, Orientation.NORTH, "Player 2", 10, cs[1]);

		// Reset turn.
		turn = 0;

		// Clear history.
		_history.clear();
		_historyIndex = 0;

		// Notify listeners.
		fireNewGame();
	}

	// Listeners.

	private Color[] createPlayerColors() {
		List<Color> colors = createPossibleColors();
		Color[] cs = new Color[NPLAYERS];
		for (int i = 0; i < NPLAYERS; i++) {
			cs[i] = colors.get(i);
		}
		return cs;
	}

	private List<Color> createPossibleColors() {
		List<Color> colors = new LinkedList<Color>();
		int n = 24;
		for (int i = 0; i < n; i++) {
			colors.add(Color.getHSBColor((float) i / n, .5f, 1));
		}
		Collections.shuffle(colors);
		return colors;
	}

	/** Causes the listener to be notified of subsequent board events. */
	public void addBoardListener(BoardListener l) {
		listeners.add(l);
	}

	/** Causes the listener to no longer be notified of subsequent board events. */
	public void removeBoardListener(BoardListener l) {
		listeners.remove(l);
	}

	private void fireNewGame() {
		for (BoardListener l : listeners) {
			l.newGame();
		}
	}

	private void fireMoveExecuted(Move move) {
		for (BoardListener l : listeners) {
			l.moveExecuted(move);
		}
	}

	private void fireMovesUndone(Move[] moves) {
		for (BoardListener l : listeners) {
			l.movesUndone(moves);
		}
	}

	// Board size.

	/** Returns the board's width. */
	public int getWidth() {
		return width;
	}

	/** Returns the board's height. */
	public int getHeight() {
		return height;
	}

	// Player positions.

	/** Returns whether the player position is within the board's bounds. */
	public boolean containsPlayerPosition(Position position) {
		return 0 <= position.getX() && position.getX() < width
				&& 0 <= position.getY() && position.getY() < height;
	}

	/** Returns whether the position is taken by any player. */
	public boolean isTaken(Position position) {
		for (Player p : players) {
			if (p.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	// Wall positions.

	/** Returns whether the wall position is within the board's bounds. */
	public boolean containsWallPosition(Position wallPosition) {
		return 0 <= wallPosition.getX() && wallPosition.getX() < width - 1
				&& 0 <= wallPosition.getY() && wallPosition.getY() < height - 1;
	}

	/**
	 * Returns the wall at the specified position, or {@literal null} if there
	 * is no wall at that position.
	 */
	public Wall getWall(Position position) {
		return walls[position.getX()][position.getY()];
	}

	/** Sets the wall at the specified position. */
	public void setWall(Position position, Wall wall) {
		walls[position.getX()][position.getY()] = wall;
	}

	/**
	 * Returns whether a wall or the board's bounds prevents a player from
	 * moving from the specified position in the specified direction.
	 */
	public boolean isBlocked(Position position, Orientation orientation) {
		if (!containsPlayerPosition(position)) {
			throw new IllegalArgumentException("Illegal position: " + position);
		}

		if (!containsPlayerPosition(position.move(orientation))) {
			// Can't move in the specified direction.
			return true;
		}

		// The wall positions are going to be checked for a wall of type "wall".
		// If they have such a wall, the way is blocked.
		Position wallPosition1, wallPosition2;
		Wall wall;

		switch (orientation) {
		case NORTH:
			wallPosition1 = position;
			wallPosition2 = position.west();
			wall = Wall.HORIZONTAL;
			break;
		case EAST:
			wallPosition1 = position;
			wallPosition2 = position.south();
			wall = Wall.VERTICAL;
			break;
		case SOUTH:
			wallPosition1 = position.south();
			wallPosition2 = position.south().west();
			wall = Wall.HORIZONTAL;
			break;
		case WEST:
			wallPosition1 = position.west();
			wallPosition2 = position.west().south();
			wall = Wall.VERTICAL;
			break;
		default:
			throw new InternalError();
		}

		if (containsWallPosition(wallPosition1)
				&& getWall(wallPosition1) == wall) {
			// The wall at position 1 blocks.
			return true;
		} else if (containsWallPosition(wallPosition2)
				&& getWall(wallPosition2) == wall) {
			// The wall at position 2 blocks.
			return true;
		} else {
			// Nothing blocks.
			return false;
		}
	}

	// Players.

	/** Returns all players participating in this game. */
	public Player[] getPlayers() {
		return players;
	}

	/** Returns the i-player. */
	public Player getPlayer(int i) {
		return players[i];
	}

	/** Returns the player whose turn it is. */
	public Player getTurn() {
		return players[turn];
	}

	/** Returns the index of the player whose turn it is. */
	public int getTurnIndex() {
		return turn;
	}

	/**
	 * Returns the player who has won, or {@literal null} if the game is not
	 * over yet.
	 */
	public Player getWinner() {
		for (Player p : getPlayers()) {
			if (p.isWinner()) {
				return p;
			}
		}
		return null;
	}

	/** Returns whether the game is over. */
	public boolean isGameOver() {
		return getWinner() != null;
	}

	// Moves.

	/** Executes the move. */
	public void move(Move move) {
		// if (!move.isLegal(this)) {
		// throw new IllegalArgumentException("Illegal move for " + getTurn()
		// + ": " + move);
		// }

		// Note: execute before increasing turn.

		move.execute(this);
		increaseTurn();

		while (_historyIndex < _history.size()) {
			_history.removeFirst();
		}
		_history.push(move);
		_historyIndex++;

		fireMoveExecuted(move);
	}

	/** Equivalent to {@code undo(1)}. */
	public void undo() {
		undo(1);
	}

	/** Undoes the last {@code number} moves. */
	public void undo(int number) {
		if (number < 1) {
			throw new IllegalArgumentException("Number must be at least 1.");
		}

		if (number > _history.size()) {
			throw new IllegalArgumentException("Cannot undo " + number
					+ " moves (max is " + _history.size() + ")");
		}

		List<Move> undone = new LinkedList<Move>();

		for (int i = 0; i < number; i++) {
			// Note: decrease turn before undoing.
			Move move = _history.get(_history.size() - _historyIndex);
			_historyIndex--;
			decreaseTurn();
			move.undo(this);
			undone.add(move);
		}

		Move[] moves = new Move[undone.size()];
		undone.toArray(moves);
		fireMovesUndone(moves);
	}

	/** Returns an iterator over the stack of executed moves. */
	public Iterator<Move> getHistory() {
		return _history.descendingIterator();
	}

	public int getHistoryIndex() {
		return _historyIndex;
	}

	public boolean hasHistory() {
		return !_history.isEmpty();
	}

	// TODO See if is possible to remove this method
	public int getHistorySize() {
		return _history.size();
	}

	/** Increases the turn by 1. */
	private void increaseTurn() {
		turn = (turn + 1) % players.length;
	}

	/** Decreases the turn by 1. */
	private void decreaseTurn() {
		turn--;
		if (turn < 0) {
			turn += players.length;
		}
	}

	// Cloning.

	/** Creates a deep copy of this board. */
	@Override
	public Board clone() {
		Board clone = new Board(width, height);
		clone._history.addAll(_history);
		for (int i = 0; i < players.length; i++) {
			clone.players[i] = new Player(clone, players[i]);
		}
		clone.turn = turn;
		for (int x = 0; x < width - 1; x++) {
			for (int y = 0; y < height - 1; y++) {
				clone.walls[x][y] = walls[x][y];
			}
		}
		return clone;
	}

}
