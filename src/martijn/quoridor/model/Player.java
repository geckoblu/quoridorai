package martijn.quoridor.model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 * A player participating in a game of Quoridor.
 */
public class Player {

    private static final int INITIAL_NWALLS = 10;

    private static final Color[] COLOR;

    static {
        COLOR = new Color[2];
        COLOR[0] = Color.decode("#AE611A"); //Color.decode("#809FFF");
        COLOR[1] = Color.decode("#3E1E0F"); //Color.decode("#FF80FF");
    }

    private final Board _board;

    private final Orientation _orientation;

    public final int index;

    private Position _position;

    private int _nwalls;

    /**
     * Creates a new player.
     *
     * @param board
     *            The board this player is part of.
     * @param orientation
     *            The player's orientation. See {@link #getOrientation()}.
     */
    public Player(int index, Board board, Orientation orientation) {
        this.index = index;
        this._board = board;
        this._orientation = orientation;
        this._nwalls = INITIAL_NWALLS;
        this._position = getInitialPosition();
    }

    /**
     * Creates a player on the specified board that is a clone of the specified player.
     */
    public Player(Board board, Player player) {
        this._board = board;
        this.index = player.index;
        this._nwalls = player._nwalls;
        this._orientation = player._orientation;
        this._position = player._position;
    }

    /**
     * Gets the initial player position on this board
     */
    private Position getInitialPosition() {
        int x = Board.SIZE / 2;
        int y = Board.SIZE / 2;
        switch (_orientation) {
        case NORTH:
            return new Position(x, Board.SIZE - 1);
        case EAST:
            return new Position(Board.SIZE - 1, y);
        case SOUTH:
            return new Position(x, 0);
        case WEST:
            return new Position(0, y);
        default:
            throw new InternalError();
        }
    }

    /**
     * Returns this player's orientation on the board. The orientation
     * determines the player's starting position and goal positions.
     */
    public Orientation getOrientation() {
        return _orientation;
    }

    /**
     * Returns the board this player participates in.
     */
    public Board getBoard() {
        return _board;
    }

    /**
     * Returns the number of walls this player owns.
     */
    public int getWallCount() {
        return _nwalls;
    }

    /**
     * Takes a wall from this player.
     */
    public void takeWall() {
        if (_nwalls == 0) {
            throw new IllegalStateException("Player has no walls left.");
        }
        _nwalls--;
    }

    /**
     * Gives a wall to this player.
     */
    public void giveWall() {
        _nwalls++;
    }

    /**
     * Returns whether the player wins if it reaches the specified position.
     */
    public boolean isGoal(Position p) {
        switch (_orientation) {
        case NORTH:
            return p.getY() == 0;
        case EAST:
            return p.getX() == 0;
        case SOUTH:
            return p.getY() == Board.SIZE - 1;
        case WEST:
            return p.getX() == Board.SIZE - 1;
        default:
            throw new InternalError();
        }
    }

    /**
     * Returns whether this player has won.
     */
    public boolean isWinner() {
        return isGoal(getPosition());
    }

    /**
     * Returns whether it's this player's turn.
     */
    public boolean isTurn() {
        return _board.getTurn() == this;
    }

    /**
     * Returns the player's current position.
     */
    public Position getPosition() {
        return _position;
    }

    /**
     * Returns this player's color.
     */
    public Color getColor() {
        return COLOR[index];
    }

    /**
     * Sets the player's position.
     */
    public void setPosition(Position position) {
        this._position = position;
    }

    /**
     * Finds a goal closest to the player's current position and returns a
     * shortest path to it. The path does not take into account what positions
     * are blocked by other players.
     */
    public Orientation[] findGoal() {
        return new GoalSeeker(this).getPath();
    }

    /**
     * Returns a position one jump away from the player's current position,
     * closer to a goal cell.
     */
    public Position stepToGoal() {
        Position step = null;
        int best = Integer.MAX_VALUE;
        Position old = getPosition();
        for (Position pos : getJumpPositions()) {
            setPosition(pos);
            int d = findGoal().length;
            if (d < best) {
                best = d;
                step = pos;
            }
        }
        setPosition(old);
        return step;
    }

    /**
     * Returns all positions this player can jump to given the board's current
     * state.
     */
    public Set<Position> getJumpPositions() {
        // The set that is going to contain all legal new positions.
        Set<Position> legal = new TreeSet<Position>();

        // A blacklist of positions that we won't visit again.
        Set<Position> illegal = new TreeSet<Position>();

        // The queue containing the positions we're about to visit.
        Queue<Position> front = new LinkedList<Position>();
        front.add(getPosition());

        while (!front.isEmpty()) {
            // Get position from queue.
            Position pos = front.remove();

            if (_board.isTaken(pos)) {
                // This position is taken by a player. Spread 1 unit in all
                // unblocked directions.

                // Never visit this position again.
                illegal.add(pos);

                for (Orientation o : Orientation.values()) {
                    if (!_board.isBlocked(pos, o)) {
                        Position p2 = pos.move(o);
                        if (!illegal.contains(p2)) {
                            // Visit this position soon.
                            front.add(p2);
                        }
                    }
                }
            } else {
                // This is a legal move. Don't spread.
                legal.add(pos);
            }
        }
        return legal;
    }

    @Override
    public String toString() {
        return "Player " + index;
    }

}
