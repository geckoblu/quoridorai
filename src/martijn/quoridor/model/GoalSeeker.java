package martijn.quoridor.model;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class GoalSeeker implements Comparator<Position> {

    private Player _player;

    private Orientation[][] _from;

    private int[][] _distance;

    private Orientation[] _path;

    public GoalSeeker(Player player) {
        if (player == null) {
            throw new NullPointerException("Player is null.");
        }
        this._player = player;

        if (player.isWinner()) {
            // Special case: we're already at a goal position.
            _path = new Orientation[0];
            return;
        }

        int w = Board.SIZE;
        int h = Board.SIZE;
        _from = new Orientation[w][h];
        _distance = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                _distance[x][y] = Integer.MAX_VALUE;
            }
        }

        Queue<Position> front = new PriorityQueue<Position>(8, this);
        Position pos = player.getPosition();
        _distance[pos.getX()][pos.getY()] = 0;
        front.add(player.getPosition());

        int counter = 0;

        while (!front.isEmpty()) {
            counter++;
            pos = front.remove();
            int x = pos.getX();
            int y = pos.getY();
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
                    front.add(pos2);

                    if (player.isGoal(pos2)) {
                        // We've found a goal position. Build and return path.
                        _path = buildPath(pos2);
                        return;
                    }
                }
            }
        }
    }

    private Orientation[] buildPath(Position to) {
        Orientation[] path = new Orientation[_distance[to.getX()][to.getY()]];
        int i = path.length;
        while (!to.equals(_player.getPosition())) {
            path[--i] = to.visit(_from).opposite();
            to = to.move(to.visit(_from));
        }
        return path;
    }

    public Orientation[] getPath() {
        return _path;
    }

    private Board getBoard() {
        return _player.getBoard();
    }

    private int f(Position pos) {
        return g(pos) + h(pos);
    }

    private int g(Position pos) {
        return _distance[pos.getX()][pos.getY()];
    }

    private int h(Position pos) {
        Position goal;
        switch (_player.getOrientation()) {
        case NORTH:
            goal = new Position(pos.getX(), 0);
            break;
        case EAST:
            goal = new Position(0, pos.getY());
            break;
        case SOUTH:
            goal = new Position(pos.getX(), Board.SIZE - 1);
            break;
        case WEST:
            goal = new Position(Board.SIZE - 1, pos.getY());
            break;
        default:
            throw new InternalError();
        }
        return pos.manhattan(goal);
    }

    // private int h(Position pos) {
    // switch (player.getOrientation()) {
    // case NORTH:
    // return pos.getY() - 1;
    // case EAST:
    // return pos.getX() - 1;
    // case SOUTH:
    // return getBoard().getHeight() - pos.getY() - 1;
    // case WEST:
    // return getBoard().getWidth() - pos.getX() - 1;
    // default:
    // throw new InternalError();
    // }
    // }

    @Override
    public int compare(Position p1, Position p2) {
        return f(p1) - f(p2);
    }

}
