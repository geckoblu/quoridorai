package martijn.quoridor.model;

import martijn.quoridor.Config;

public class PutWall implements Move {

    private Position _position;

    private Wall _wall;

    public PutWall(Position position, Wall wall) {
        if (position == null || wall == null) {
            throw new NullPointerException();
        }
        _position = position;
        _wall = wall;
    }

    public PutWall(Notation notation, String move) {
        char cx = move.charAt(0);
        char cy = move.charAt(1);
        String w = move.substring(2);
        int x;
        int y;
        x = cx - 'a';
        switch (notation) {
        case LAMEK:
            y = cy - '1';
            break;
        case GLENDENNING:
            y = cy + '8';
            break;
        default:
            x = cx - '0';
            y = cy - '0';
            break;
        }
        _position = new Position(x, y);
        _wall = Wall.parse(w);
    }
    /*
         public String notation() {
        switch (Config.notation()) {
        case LAMEK:
            return "" + (char) ('a' + _position.getX()) + (char) ('1' + _position.getY()) + wall.notation();
        case GLENDENNING:
            return "" + (char) ('a' + _position.getX()) + (char) ('8' - _position.getY()) + wall.notation();
        default:
            return "" + (char) ('0' + _position.getX()) + (char) ('0' + _position.getY()) + wall.notation();
        }
    }

     */

    public Position getPosition() {
        return _position;
    }

    public Wall getWall() {
        return _wall;
    }

    /**
     * Creates and returns a PutWall move at the same position but with the wall
     * direction flipped.
     */
    public PutWall flip() {
        return new PutWall(_position, _wall.flip());
    }

    @Override
    public void execute(Board board) {
        board.setWall(_position, _wall);
        board.getTurn().takeWall();
    }

    @Override
    public void undo(Board board) {
        board.getTurn().giveWall();
        board.setWall(_position, null);
    }

    @Override
    public void redo(Board board) {
        execute(board);
    }

    @Override
    public boolean isLegal(Board board) {
        // Does position exist on board?
        if (!board.containsWallPosition(_position)) {
            return false;
        }

        // Is the game over?
        if (board.isGameOver()) {
            return false;
        }

        // Is there already a wall at the position?
        if (board.getWall(_position) != null) {
            return false;
        }

        // Does the player have any walls left to place?
        if (board.getTurn().getWallCount() < 1) {
            return false;
        }

        // Does the wall clash with existing walls nearby?
        Position p1, p2;
        switch (_wall) {
        case HORIZONTAL:
            p1 = _position.west();
            p2 = _position.east();
            break;
        case VERTICAL:
            p1 = _position.north();
            p2 = _position.south();
            break;
        default:
            throw new InternalError();
        }
        if (board.containsWallPosition(p1) && board.getWall(p1) == _wall) {
            return false;
        }
        if (board.containsWallPosition(p2) && board.getWall(p2) == _wall) {
            return false;
        }

        // Would the wall block a player from reaching their goal?
        try {
            execute(board);
            for (Player p : board.getPlayers()) {
                if (p.findGoal() == null) {
                    // This player's has no way of reaching their goal anymore.
                    return false;
                }
            }
        } finally {
            undo(board);
        }

        // Placing the wall is okay.
        return true;
    }

    @Override
    public String toString() {
        return "PutWall " + getWall().toString() + " at " + getPosition();
    }

    @Override
    public String notation() {
        switch (Config.notation()) {
        case LAMEK:
            return "" + (char) ('a' + _position.getX()) + (char) ('1' + _position.getY()) + _wall.notation();
        case GLENDENNING:
            return "" + (char) ('a' + _position.getX()) + (char) ('8' - _position.getY()) + _wall.notation();
        default:
            return "" + (char) ('0' + _position.getX()) + (char) ('0' + _position.getY()) + _wall.notation();
        }
    }

}
