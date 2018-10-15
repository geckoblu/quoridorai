package martijn.quoridor.brains;

import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Jump;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Position;
import martijn.quoridor.model.PutWall;
import martijn.quoridor.model.Wall;

public class DeepBrain extends AbstractBrain {

    public DeepBrain(int depth) {
        super("DeepBrain " + depth, depth);
    }

    @Override
    protected List<Move> selectMoves(Board board) {
        List<Move> moves = new LinkedList<Move>();
        Player player = board.getTurn();

        // Add Jump.
        Position bestJump = player.stepToGoal();
        moves.add(new Jump(bestJump));

        for (Position p: player.getJumpPositions()) {
            if (!p.equals(bestJump)) {
                moves.add(new Jump(p));
            }
        }


        // Add Walls
        for (int x = 0; x < Board.SIZE - 1; x++) {
            for (int y = 0; y < Board.SIZE - 1; y++) {
                Move hwall = new PutWall(new Position(x, y), Wall.HORIZONTAL);
                if (hwall.isLegal(board)) {
                    moves.add(hwall);
                }
                Move vwall = new PutWall(new Position(x, y), Wall.VERTICAL);
                if (vwall.isLegal(board)) {
                    moves.add(vwall);
                }
            }
        }


        //Move[] rv = new Move[moves.size()];
        //return moves.toArray(rv);

        return moves;
    }

    @Override
    public int getHeuristic(Board board) {
        return getShortestPathHeuristic(board);
    }

    private int getShortestPathHeuristic(Board board) {
        Player p1 = board.getTurn();
        Player p2 = board.getOpponent();

        if (p1.isWinner()) {
            return WIN;
        } else if (p2.isWinner()) {
            return LOSE;
        } else {
            return p2.findGoal() - p1.findGoal();
        }
    }
}
