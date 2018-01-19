package martijn.quoridor.ui;

import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

/**
 * A Controller controls a specific player.
 */
public abstract class Controller implements BoardListener {

    private BoardCanvas _canvas;

    /** Which player is this Controller controlling (Player 1 or Player 2 or both or none)**/
    private List<Integer> _controlling;

    private boolean expecting;

    /** Creates a new Controller. */
    public Controller(BoardCanvas canvas) {
        System.out.println("New Controller");
        if (canvas == null) {
            throw new NullPointerException("Canvas is null.");
        }
        _canvas = canvas;
        _controlling = new LinkedList<Integer>();
        getBoard().addBoardListener(this);
    }

    protected BoardCanvas getCanvas() {
        return _canvas;
    }

    /** Returns the board. */
    protected Board getBoard() {
        return _canvas.getBoard();
    }

    /**
     * Activates the controller. The controller will prepare to move immediately
     * if it's its player's turn.
     */
    public void startControlling(int player) {
        _controlling.add(player);
        wake();
    }

    /** Deactivates the controller. */
    public void stopControlling(int player) {
        _controlling.remove(new Integer(player));
        wake();
    }

    @Override
    public void moveExecuted() {
        stopExpecting();
        wake();
    }

    @Override
    public void newGame() {
        stopExpecting();
        wake();
    }

    /** Calls {@link #moveExpected()} if it's {@link #getPlayer()}'s turn. */
    private void wake() {
        System.out.println("CTRL wake " + _controlling);
        if (shouldExpect()) {
            startExpecting();
        } else {
            stopExpecting();
        }
    }

    public boolean shouldExpect() {
        return _controlling.contains(getBoard().getTurnIndex()) && !getBoard().isGameOver();
    }

    /**
     * Returns whether the controller is expecting. That is, whether it is the
     * controller's player's turn, and the controller hasn't made a move yet.
     */
    private boolean isExpecting() {
        return expecting;
    }

    private synchronized void startExpecting() {
        if (!expecting) {
            expecting = true;
            moveExpected();
        }
    }

    private synchronized void stopExpecting() {
        if (expecting) {
            expecting = false;
            moveCancelled();
        }
    }

    /**
     * Notification that it has become this controller's player's turn and that
     * the controller should make a move. The implementation should exit
     * immediately. When the controller has decided which move to execute, it
     * should call {@link #move(Move)} rather than {@link Board#move(Move)}
     * directly.
     */
    protected abstract void moveExpected();

    /**
     * Notification that it's no longer the controller's turn. This will only be
     * called if the controller was expected to execute a move.
     */
    protected abstract void moveCancelled();

    protected void move(Move move) {
        expecting = false;
        getBoard().move(move);
    }

    /** Returns whether the controller is human. */
    public abstract boolean isHuman();

}
