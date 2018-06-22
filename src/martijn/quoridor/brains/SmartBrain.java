package martijn.quoridor.brains;

import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Orientation;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PositionSet;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

public class SmartBrain extends NegamaxBrain {

    public SmartBrain(int depth) {
        super("SmartBrain " + depth, depth);
    }

    @Override
    public Move[] selectMoves(Board board) {
        List<Move> moves = new LinkedList<Move>();
        Player p = board.getTurn();

        // Add Jump.
        moves.add(new Jump(p.stepToGoal()));

        // Add PutWalls.
        if (p.getWallCount() > 0) {
            PositionSet set = new PositionSet(Board.SIZE + 1, Board.SIZE + 1);
            for (Player pl : board.getPlayers()) {
                markWallLocations(set, pl);
            }
            for (int x = 1; x < Board.SIZE; x++) {
                for (int y = 1; y < Board.SIZE; y++) {
                    Position pos = new Position(x, y);
                    if (set.contains(pos)) {
                        pos = pos.south().west();
                        add(moves, new PutWall(pos, Wall.HORIZONTAL), board);
                        add(moves, new PutWall(pos, Wall.VERTICAL), board);
                    }
                }
            }
        }

        Move[] rv = new Move[moves.size()];
        return moves.toArray(rv);
    }

    private void markWallLocations(PositionSet set, Player player) {
        Orientation[] path = player.findPathToGoal();
        Position pos = player.getPosition();
        select(set, pos);
        for (Orientation o : path) {
            pos = pos.move(o);
            select(set, pos);
        }
    }

    private void add(List<Move> moves, Move move, Board board) {
        if (move.isLegal(board)) {
            moves.add(move);
        }
    }

    private void select(PositionSet set, Position pos) {
        set.add(pos);
        set.add(pos.east());
        set.add(pos.north());
        set.add(pos.east().north());
    }

    @Override
    public int getHeuristic(Board board) {
        return getShortestPathHeuristic(board);
    }

    public static int getShortestPathHeuristic(Board board) {
        Player p1 = board.getTurn();
        // Find player whose turn it is not.
        Player p2 = null;
        for (Player p : board.getPlayers()) {
            if (board.getTurn() != p) {
                p2 = p;
            }
        }

        if (p1.isWinner()) {
            return 1000;
        } else if (p2.isWinner()) {
            return -1000;
        } else {
            return p2.findGoal() - p1.findGoal();
        }
    }

}
