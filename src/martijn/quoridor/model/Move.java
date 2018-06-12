package martijn.quoridor.model;

/**
 * A move in Quoridor. After a move is done, the turn is increased. The move
 * does not remember what player performed the move, or on what board the move
 * was performed.
 */
public interface Move {

    /**
     * Executes the move.
     */
    void execute(Board board);

    /**
     * Undoes the move.
     */
    void undo(Board board);

    /**
     * Re-does the move.
     */
    void redo(Board board);

    /**
     * Returns whether this move is legal in the specified state.
     */
    boolean isLegal(Board board);

    /**
     * Returns the move notation, either in Lamek or Glendenning
     */
    String notation();

}
