package martijn.quoridor.brains;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

public abstract class AbstractBrain extends Brain {

    protected static final int MAX_VALUE = Integer.MAX_VALUE;
    protected static final int MIN_VALUE = Integer.MIN_VALUE + 1; // To avoid overflow (abs, neg, ...)
    protected static final int WIN = 1000;
    protected static final int LOSE = -1000;

    private static Map<String, Integer> _valutated = new HashMap<String, Integer>();
    private static boolean _loadValutaded = false;
    private static boolean _storeValutaded = false;

    private boolean _deterministic = false;
    private boolean _pruneByBest = true;
    private boolean _pruneAlpha = true;
    private boolean _sortMoves = true;

    private boolean _showMoves = false;
    private boolean _outlevel = false;
    private boolean _outlevel0 = false;

    private int _depth;
    private Set<String> _visiting;

    int _counter;
    int _counterAlpha;

    static {
        if (_loadValutaded) {
            loadValutated();
            //System.out.println("Loaded");
        }
    }

    public AbstractBrain(int depth) {
        this(null, depth);
    }

    public AbstractBrain(String name, int depth) {
        super(name);

        _depth = depth;
    }

    /**
     * Returns all the interesting moves in the board's current state that will
     * be considered by the brain.
     */
    protected abstract List<Move> selectMoves(Board board);

    /**
     * Returns a value that represents how good the board's current state is for
     * the player whose turn it is. High values mean good situations while low
     * values mean bad situations.
     */
    protected abstract int getHeuristic(Board board);

    @Override
    public Move getMove(Board board) throws InterruptedException {

        _visiting = new HashSet<String>(1000);
        _counter = 0;
        _counterAlpha = 0;

        // Rate moves.
        List<RatedMove> moves = new LinkedList<RatedMove>();
        for (Move move : selectMoves(board)) {

            outlevel("" + board.getTurn() + " " + move, 0);
            board.move(move);

            String boardHashString = board.hashString();
            _visiting.add(boardHashString);

            int rate = -negamax(board, _depth - 1, MIN_VALUE, MAX_VALUE, 1);
            outlevel("rate " + rate, 0);

            moves.add(new RatedMove(move, rate));

            _visiting.remove(board.hashString());
            board.undo();
        }

        return chooseMove(moves);
    }

    private int negamax(Board board, int depth, int alpha, int beta, int level) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        _counter += 1;

        // System.out.println(level);
        String boardHashString = board.hashString();
        if (_valutated.containsKey(boardHashString)) {
            return _valutated.get(boardHashString);
        }

        if (depth == 0 || board.isGameOver()) {
            int rate = getHeuristic(board);

            valutated(boardHashString, rate);
            return rate;
        } else {
            int negamax = MIN_VALUE;
            List<Move> moves = selectMoves(board);

            if (_sortMoves) {
                moves = sortMoves(moves, board);
            }

            for (Move move : moves) {

                outlevel("" + board.getTurn() + " " + move, level);
                board.move(move);

                outlevel("    " + alpha + " " + beta, level);

                String bhs = board.hashString();

                if (!_visiting.contains(bhs)) {
                    _visiting.add(bhs);

                    int rate = -negamax(board, depth - 1, beta * -1, alpha * -1, level + 1);
                    outlevel("rate " + rate, level);
                    negamax = Math.max(negamax, rate);
                    outlevel("negamax " + negamax, level);

                    alpha = Math.max(alpha, rate);

                    outlevel("alpha " + alpha + " beta " + beta, level);

                    _visiting.remove(board.hashString());
                }

                board.undo();

                if (_pruneAlpha && alpha >= beta) {
                    //System.err.println("alpha >= beta (" + alpha + ",  " + beta + ")");
                    _counterAlpha += 1;
                    break;
                }

                if (_pruneByBest && negamax == WIN) {
                    break;
                }
            }

            // if (negamax == MIN_VALUE) {
            // System.err.println(("UNDEFINED RATE FOR BOARD " +
            // boardHashString));
            // throw new RuntimeException("UNDEFINED RATE FOR BOARD " +
            // boardHashString);
            // }
            valutated(boardHashString, negamax);
            return negamax;
        }
    }

    private void valutated(String boardHashString, int rate) {
        if (rate == WIN || rate == LOSE) {
            // System.out.println(boardHashString + " " + rate);
            _valutated.put(boardHashString, new Integer(rate));

            if (_storeValutaded) {
                storeValutated(boardHashString, rate);
            }
        }
    }

    private Move chooseMove(List<RatedMove> moves) {
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
            // System.out.println("Getting radom move " + moves);
            int bestRating = moves.get(0).getRating();
            for (RatedMove m : moves) {
                if (m.getRating() == bestRating) {
                    best.add(m);
                } else {
                    break;
                }
            }
            // System.out.println("Picking a random move from " + best.size() +
            // " best move(s).");
            Collections.shuffle(best);
            move = best.get(0);

        }

        if (_showMoves) {
            out("");
            out("----------");
            int bestRating = move.getRating();
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

        return move.getMove();
    }

    private final ComparatorMove _comparatorMoves = new ComparatorMove(this);

    List<Move> sortMoves(List<Move> moves, Board board) {

        _comparatorMoves.init(moves, board);

        Collections.sort(moves, _comparatorMoves);

        return moves;
    }

    protected void out(String s) {
        System.out.println(s);
    }

    protected void outlevel(String s, int level) {
        if (!_outlevel || (level == 0 && !_outlevel0)) {
            return;
        }
        if (level < 0) {
            level = level * -1;
        }
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
        System.out.println(s);
    }

    public void setDeterministic(boolean deterministic) {
        _deterministic = deterministic;
    }

    public void setPruneByBest(boolean pruneByBest) {
        _pruneByBest = pruneByBest;
    }

    public void setPruneAlpha(boolean pruneAlpha) {
        _pruneAlpha = pruneAlpha;
    }

    public void setSortMoves(boolean sortMoves) {
        _sortMoves = sortMoves;
    }

    private void storeValutated(String boardHashString, int rate) {

        try {
            File storedir = new File(getStoreDir());
            storedir.mkdirs();

            if (_bw0 == null) {
                _bw0 = new BufferedWriter(new FileWriter(new File(storedir, "0"), true));

            }

            if (_bw1 == null) {
                _bw1 = new BufferedWriter(new FileWriter(new File(storedir, "1"), true));

            }

            BufferedWriter bw;
            if (boardHashString.charAt(0) == '0') {
                bw = _bw0;
            } else {
                bw = _bw1;
            }

            String val;
            if (rate == WIN) {
                val = "+";
            } else if (rate == LOSE) {
                val = "-";
            } else {
                val = "!";
            }
            bw.write(boardHashString + " " + val + "\n");
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadValutated() {
        _valutated.clear();

        try {
            File storedir = new File(getStoreDir());

            File br0 = new File(storedir, "0");
            if (br0.isFile()) {
                loadValutaded(br0);
            }

            File br1 = new File(storedir, "1");
            if (br1.isFile()) {
                loadValutaded(br1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadValutaded(File brf) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(brf))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] s = line.split(" ");
                try {
                String boardHashString = s[0];
                if (s[1].equals("+")) {
                    _valutated.put(boardHashString, new Integer(WIN));
                } else if (s[1].equals("-")) {
                    _valutated.put(boardHashString, new Integer(LOSE));
                } else {
                    System.err.println("Check line " + line + " in file: " + brf.getName());
                }
                } catch (Exception e) {
                    System.err.println("Check line " + line + " in file: " + brf.getName());
                }
            }
        }
    }

    private static String getStoreDir() {
        // String home = System.getProperty("user.home");
        // String dir = home + File.separator + ".cache" + File.separator +
        // "quoridorai" + File.separator
        // + "size" + Board.SIZE;
        String dir = "/mnt/storage/quoridorai-cache/size" + Board.SIZE;
        return dir;
    }

    private BufferedWriter _bw0;
    private BufferedWriter _bw1;

}
