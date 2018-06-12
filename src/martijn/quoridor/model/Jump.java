package martijn.quoridor.model;

import martijn.quoridor.Config;

/**
 * A jump move.
 */
public class Jump implements Move {

    private Position _oldPosition;

    private Position _newPosition;

    /**
     * Creates a move representing the active player's jump to
     * {@code newPosition}.
     */
    public Jump(Position newPosition) {
        _newPosition = newPosition;
    }

    /**
     * Creates a move representing the active player's jump to
     * {@code move} defined as a string in {@ code notation} notation.
     * @param notation
     * @param move
     */
    public Jump(Notation notation, String move) {
        char cx = move.charAt(0);
        char cy = move.charAt(1);
        int x;
        int y;
        x = cx - 'a';
        switch (notation) {
        case LAMEK:
            y = cy - '1';
            break;
        case GLENDENNING:
            y = cy + '9';
            break;
        default:
            x = cx - '0';
            y = cy - '0';
            break;
        }
        _newPosition = new Position(x, y);
    }

    /** Returns the player's position after the jump. */
    public Position getPosition() {
        return _newPosition;
    }

    @Override
    public void execute(Board board) {
        Player p = board.getTurn();
        _oldPosition = p.getPosition();
        p.setPosition(_newPosition);
    }

    @Override
    public void undo(Board board) {
        board.getTurn().setPosition(_oldPosition);
    }

    @Override
    public void redo(Board board) {
        board.getTurn().setPosition(_newPosition);
    }

    @Override
    public boolean isLegal(Board board) {
        return !board.isGameOver() && board.getTurn().getJumpPositions().contains(_newPosition);
    }

    @Override
    public String toString() {
        return "Jump to " + _newPosition;
    }

    @Override
    public String notation() {
        switch (Config.getNotation()) {
        case LAMEK:
            return "" + (char) ('a' + _newPosition.getX()) + (char) ('1' + _newPosition.getY());
        case GLENDENNING:
            return "" + (char) ('a' + _newPosition.getX()) + (char) ('9' - _newPosition.getY());
        default:
            return "" + (char) ('0' + _newPosition.getX()) + (char) ('0' + _newPosition.getY());
        }
    }

}
