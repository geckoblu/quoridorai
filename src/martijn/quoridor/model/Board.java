package martijn.quoridor.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class Board {

    public static final int SIZE = 9;

    public static final int NPLAYERS = 2;

    private Wall[][] _walls;

    private Player[] _players;

    private int _turn;

    private LinkedList<Move> _history;
    private int _historyIndex;

    /** Creates a new 9x9 board with two players. */
    public Board() {

        _players = new Player[NPLAYERS];
        _history = new LinkedList<Move>();
        _historyIndex = 0;

        newGame();
    }

    /** Starts a new game, clearing the history. */
    public void newGame() {
        // Clear walls.
        _walls = new Wall[SIZE - 1][SIZE - 1];

        // Create fresh players.
        _players[0] = new Player(0, this, Orientation.SOUTH);
        _players[1] = new Player(1, this, Orientation.NORTH);

        // Reset turn.
        _turn = 0;

        // Clear history.
        _history.clear();
        _historyIndex = 0;

    }


    // Player positions.

    /** Returns whether the player position is within the board's bounds. */
    public boolean containsPlayerPosition(Position position) {
        return 0 <= position.getX() && position.getX() < SIZE && 0 <= position.getY()
                && position.getY() < SIZE;
    }

    /** Returns whether the position is taken by any player. */
    public boolean isTaken(Position position) {
        for (Player p : _players) {
            if (p.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    // Wall positions.

    /** Returns whether the wall position is within the board's bounds. */
    public boolean containsWallPosition(Position wallPosition) {
        return 0 <= wallPosition.getX() && wallPosition.getX() < SIZE - 1 && 0 <= wallPosition.getY()
                && wallPosition.getY() < SIZE - 1;
    }

    /**
     * Returns the wall at the specified position, or {@literal null} if there
     * is no wall at that position.
     */
    public Wall getWall(Position position) {
        return _walls[position.getX()][position.getY()];
    }

    /** Sets the wall at the specified position. */
    public void setWall(Position position, Wall wall) {
        _walls[position.getX()][position.getY()] = wall;
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

      //CHECKSTYLE.OFF: SimplifyBooleanReturn - Easier to read
        if (containsWallPosition(wallPosition1) && getWall(wallPosition1) == wall) {
            // The wall at position 1 blocks.
            return true;
        } else if (containsWallPosition(wallPosition2) && getWall(wallPosition2) == wall) {
            // The wall at position 2 blocks.
            return true;
        } else {
            // Nothing blocks.
            return false;
        }
      //CHECKSTYLE.ON: SimplifyBooleanReturn
    }

    // Players.

    /** Returns all players participating in this game. */
    public Iterable<Player> getPlayers() {
        return Arrays.asList(_players);
    }

    /** Returns the i-player. */
    public Player getPlayer(int i) {
        return _players[i];
    }

    /** Returns the player whose turn it is. */
    public Player getTurn() {
        return _players[_turn];
    }

    /** Returns the index of the player whose turn it is. */
    public int getTurnIndex() {
        return _turn;
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

        // made a move clears the future history
        while (_historyIndex < _history.size()) {
            _history.removeFirst();
        }

        _history.push(move);
        _historyIndex++;

    }

    /**
     * Add the move to the history, without playing it.
     * @param move
     */
    private void add(Move move) {

        move.execute(this);

        // Note: execute before increasing turn.
        increaseTurn();

        _history.push(move);
        _historyIndex++;

    }

    /**
     * Add the moves to the history, without playing it.
     */
    public void add(Iterator<Move> moves) {
        while (moves.hasNext()) {
            Move move = moves.next();
            add(move);
        }
    }

    public boolean canUndo() {
        return _historyIndex > 0;
    }

    /** Equivalent to {@code undo(1)}. */
    public void undo() {
        undo(1);
    }

    /** **/
    public void undoAll() {
        undo(_historyIndex);
    }

    /** Undoes the last {@code number} moves. */
    public void undo(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Number must be at least 1.");
        }

        if (number > _history.size()) {
            throw new IllegalArgumentException("Cannot undo " + number + " moves (max is " + _history.size()
                    + ")");
        }

        for (int i = 0; i < number; i++) {
            // Note: decrease turn before undoing.
            Move move = _history.get(_history.size() - _historyIndex);
            _historyIndex--;
            decreaseTurn();
            move.undo(this);
        }

    }

    public boolean canRedo() {
        return _historyIndex < _history.size();
    }

    /** Equivalent to {@code redo(1)}. */
    public void redo() {
        redo(1);
    }

    public void redoAll() {
        redo(_history.size() - _historyIndex);
    }

    /** Re-play the next {@code number} moves. */
    public void redo(int number) {
        if (number < 1) {
            throw new IllegalArgumentException("Number must be at least 1.");
        }

        if (number + _historyIndex > _history.size()) {
            throw new IllegalArgumentException("Cannot redo " + number + " moves (max is "
                    + (_history.size() - _historyIndex) + ")");
        }

        for (int i = 0; i < number; i++) {
            // Note: decrease turn before undoing.
            _historyIndex++;
            Move move = _history.get(_history.size() - _historyIndex);
            move.redo(this);
            increaseTurn();
        }

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

    /** Increases the turn by 1. */
    private void increaseTurn() {
        _turn = (_turn + 1) % _players.length;
    }

    /** Decreases the turn by 1. */
    private void decreaseTurn() {
        _turn--;
        if (_turn < 0) {
            _turn += _players.length;
        }
    }

    // Cloning.

    /**
     * Creates a deep copy of this board.
     */
    @Override
    public Board clone() {
        Board clonedBoard = new Board(this);
        return clonedBoard;
    }

    /**
     * Used only for cloned boards
     */
    private Board(Board board) {
        _players = new Player[NPLAYERS];
        _walls = new Wall[SIZE - 1][SIZE - 1];
        _history = new LinkedList<Move>(); // no history for cloned boards
        _historyIndex = 0;

        _turn = board._turn;
        for (int i = 0; i < Board.NPLAYERS; i++) {
            _players[i] = new Player(this, board._players[i]);
        }
        for (int x = 0; x < SIZE - 1; x++) {
            for (int y = 0; y < SIZE - 1; y++) {
                _walls[x][y] = board._walls[x][y];
            }
        }
    }

    @Override
    public int hashCode() {
        return hashString().hashCode();
    }

    public String hashString() {
        StringBuffer sb = new StringBuffer();

        sb.append(getTurn().index);

        for (int i = 0; i < Board.NPLAYERS; i++) {
            Player pl = _players[i];
            sb.append(pl.getPosition().getX());
            sb.append(pl.getPosition().getY());
            int wc = pl.getWallCount();
            if (wc == 10) {
                sb.append("0");
            } else if (wc == 0) {
                    sb.append("-");
            } else {
                sb.append(wc);
            }
        }
        for (int x = 0; x < SIZE - 1; x++) {
            for (int y = 0; y < SIZE - 1; y++) {
                Wall w = _walls[x][y];
                if (w == null) {
                    sb.append("-");
                } else {
                    sb.append(w.notation());
                }
            }
        }

        return sb.toString();
    }

}
