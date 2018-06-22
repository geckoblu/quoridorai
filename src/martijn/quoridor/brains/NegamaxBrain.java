package martijn.quoridor.brains;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

public abstract class NegamaxBrain extends Brain {

    private int _depth;

    private boolean _deterministic = false;

    private boolean _debug = false;

    private boolean _showMoves = true;

    public NegamaxBrain(int depth) {
        this(null, depth);
    }

    public NegamaxBrain(String name, int depth) {
        super(name);
        if (depth < 1) {
            throw new IllegalArgumentException("Depth must be at least 1.");
        }
        _depth = depth;
    }

    public void setDebug(boolean debug) {
        this._debug = debug;
    }

    public void setDeterministic(boolean deterministic) {
        this._deterministic = deterministic;
    }

    @Override
    public Move getMove(Board board) throws InterruptedException {
        debug("");

        // Remember current board value.
        int cur = getHeuristic(board);

        // Rate moves.
        List<RatedMove> moves = new LinkedList<RatedMove>();
        for (Move move : selectMoves(board)) {
            board.move(move);
            int negamax = -negamax(board, _depth - 1);
            debug(move + " @ " + (negamax - cur));
            moves.add(new RatedMove(move, negamax));
            board.undo();
        }

        // Sort moves: best moves first.
        Collections.sort(moves);

        // Pick one of the moves.
        RatedMove move;
        if (_deterministic) {
            // Just pick the first one.
            move = moves.get(0);
        } else {
            // Pick a random one from the best moves.
            List<RatedMove> best = new LinkedList<RatedMove>();
            int bestRating = moves.get(0).getRating();
            for (RatedMove m : moves) {
                if (m.getRating() == bestRating) {
                    best.add(m);
                } else {
                    break;
                }
            }
            // System.out.println("Picking a random move from " + best.size()
            // + " best move(s).");
            Collections.shuffle(best);
            move = best.get(0);

        }

        if (_showMoves) {
            out("");
            out("----------");
            int bestRating = moves.get(0).getRating();
            for (RatedMove m : moves) {
                if (m.getRating() == bestRating) {
                    if (m == move) {
                        out("  > " + m);
                    } else {
                        out("    " + m);
                    }
                } else {
                    break;
                }
            }
            out("");
        }

        debug("Moving " + move.getMove() + " (" + (move.getRating() - cur) + ")");
        debug("My current advantage: " + move.getRating());

        return move.getMove();
    }

    protected void out(String s) {
        System.out.println(s);
    }

    protected void debug(String s) {
        if (_debug) {
            System.out.println(s);
        }
    }

    protected int negamax(Board board, int depth) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (depth == 0 || board.isGameOver()) {
            return getHeuristic(board);
        } else {
            int negamax = Integer.MIN_VALUE;
            for (Move move : selectMoves(board)) {
                board.move(move);
                negamax = Math.max(negamax, -negamax(board, depth - 1));
                board.undo();
            }
            return negamax;
        }
    }

    /**
     * Returns all the interesting moves in the board's current state that will
     * be considered by the brain.
     */
    protected abstract Move[] selectMoves(Board board);

    /**
     * Returns a value that represents how good the board's current state is for
     * the player whose turn it is. High values mean good situations while low
     * values mean bad situations.
     */
    protected abstract int getHeuristic(Board board);

}
