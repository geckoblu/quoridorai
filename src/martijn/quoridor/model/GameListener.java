package martijn.quoridor.model;

/**
 * A GameListener registered on a Game is notified of any state changes
 * the Game goes through.
 */
public interface GameListener {

    /**
     * Notification that a move (or more) has been executed
     * and the state has been changed.
     */
    void boardChanged();

}
