package martijn.quoridor.model;

import martijn.quoridor.Config;

/**
 * A jump move.
 */
public class Jump implements Move {

    private Position oldPosition;

    private Position newPosition;

    /**
     * Creates a move representing the active player's jump to
     * {@code newPosition}.
     */
    public Jump(Position newPosition) {
        this.newPosition = newPosition;
    }

    /** Returns the player's position after the jump. */
    public Position getNewPosition() {
        return newPosition;
    }

    /**
     * Returns the player's position before the jump. Returns null if the move
     * has never been executed yet.
     */
    public Position getOldPosition() {
        return oldPosition;
    }

    @Override
    public void execute(Board board) {
        Player p = board.getTurn();
        oldPosition = p.getPosition();
        p.setPosition(newPosition);
    }

    @Override
    public void undo(Board board) {
        board.getTurn().setPosition(oldPosition);
    }

    @Override
    public void redo(Board board) {
        board.getTurn().setPosition(newPosition);
    }

    @Override
    public boolean isLegal(Board board) {
        return !board.isGameOver() && board.getTurn().getJumpPositions().contains(newPosition);
    }

    @Override
    public String toString() {
        return "Jump to " + getNewPosition();
    }

    @Override
    public String notation() {
        Position position = getNewPosition();
        if (Config.lamekNotation()) {
            return "" + (char) ('a' + position.getX()) + (char) ('1' + position.getY());
        } else {
            return "" + (char) ('a' + position.getX()) + (char) ('9' - position.getY());
        }
    }

}
