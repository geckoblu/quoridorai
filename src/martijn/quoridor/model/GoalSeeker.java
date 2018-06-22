package martijn.quoridor.model;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class GoalSeeker {

    private final Player _player;

    private final Orientation[][] _from;

    private final int[][] _distance;

    private final Queue<Position> _front = new PriorityQueue<Position>(8, new PositionComparator());

    private Position _goal;

    public GoalSeeker(Player player) {

        if (player == null) {
            throw new NullPointerException("Player is null.");
        }
        _player = player;

        _distance = new int[Board.SIZE][Board.SIZE];
        _from = new Orientation[Board.SIZE][Board.SIZE];

        _goal = null;
    }

    /**
     * Finds a goal closest to the player's current position and returns the
     * length of shortest path to it. The path does not take into account what
     * positions are blocked by other players.
     *
     * Return -1 if no path exists.
     *
     */
    public int findGoal() {

        if (_player.isWinner()) {
            // Special case: we're already at a goal position.
            _goal = _player.getPosition();
            _distance[_goal.getX()][_goal.getY()] = 0;
            return 0;
        }

        // A* (Dijkstra's algorithm)

        int w = Board.SIZE;
        int h = Board.SIZE;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _distance[x][y] = Integer.MAX_VALUE;
                _from[x][y] = null;
            }
        }

        _front.clear();

        Position pos = _player.getPosition();
        _distance[pos.getX()][pos.getY()] = 0;
        _front.add(_player.getPosition());

        while (!_front.isEmpty()) {
            pos = _front.remove();
            int x = pos.getX();
            int y = pos.getY();

            // System.out.println("Visiting " + pos);

            for (Orientation o : Orientation.values()) {
                if (getBoard().isBlocked(pos, o)) {
                    continue;
                }

                Position pos2 = pos.move(o);
                int x2 = pos2.getX();
                int y2 = pos2.getY();
                if (_distance[x2][y2] > _distance[x][y] + 1) {
                    _distance[x2][y2] = _distance[x][y] + 1;
                    _from[x2][y2] = o.opposite();
                    _front.add(pos2);
                    // System.out.println("\tAdding " + pos2);

                    if (_player.isGoal(pos2)) {
                        // We've found a goal position.
                        _goal = pos2;
                        return _distance[x2][y2];
                    }
                }
            }
        }

        _goal = null;
        return -1;

    }

    /**
     * Returns the shortest path from the player's current position to the
     * nearest goal found with method findGoal(). The path does not take into
     * account what positions are blocked by other players.
     *
     * WARNING: The path is searched calling findGoal(), always call it before
     * calling this, otherwise the result is meaningless.
     *
     * Return null if no path exists.
     *
     */
    public Orientation[] getPathToGoal() {

        if (_goal == null) {
            return null;
        }

        Position to = _goal;

        Orientation[] path = new Orientation[_distance[to.getX()][to.getY()]];
        int i = path.length;
        while (!to.equals(_player.getPosition())) {
            path[--i] = to.visit(_from).opposite();
            to = to.move(to.visit(_from));
        }
        return path;

    }

    private Board getBoard() {
        return _player.getBoard();
    }


    private class PositionComparator implements Comparator<Position> {

        @Override
        public int compare(Position p1, Position p2) {
            return f(p1) - f(p2);
        }

        private int f(Position pos) {
            return g(pos) + h(pos);
        }

        private int g(Position pos) {
            return _distance[pos.getX()][pos.getY()];
        }

        private int h(Position pos) {
            // Manhattan distance
            switch (_player.getOrientation()) {
            case NORTH:
                return pos.getY();
            case EAST:
                return pos.getX();
            case SOUTH:
                return Board.SIZE -1 - pos.getY();
            case WEST:
                return Board.SIZE - 1 - pos.getX();
            default:
                throw new InternalError();
            }
        }
    }

}
