package martijn.quoridor.model;

/**
 * A BoardListener registered on a Board is notified of any state changes the
 * Board goes through.
 */
public interface BoardListener {

    /**
     * Notification that a move (or more) has been executed and the state has
     * been changed.
     */
    void moveExecuted();

    /**
     * Notification that a new game has started.
     */
    void newGame();

}
