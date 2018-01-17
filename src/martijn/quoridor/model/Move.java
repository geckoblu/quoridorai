package martijn.quoridor.model;

/**
 * A move in Quoridor. After a move is done, the turn is increased. The move
 * does not remember what player performed the move, or on what board the move
 * was performed.
 */
public interface Move {

    /** Executes the move. */
    public abstract void execute(Board board);

    /** Undoes the move. */
    public abstract void undo(Board board);

    /** Re-does the move. */
    public abstract void redo(Board board);

    /** Returns whether this move is legal in the specified state. */
    public abstract boolean isLegal(Board board);

    /** Returns the move notation, either in Lamek or Glendenning **/
    public abstract String notation();

}
