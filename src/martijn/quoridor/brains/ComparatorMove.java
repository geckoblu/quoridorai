package martijn.quoridor.brains;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

public class ComparatorMove implements Comparator<Move> {

    private final AbstractBrain _parent;

    private Map<Move, Integer> _map = new HashMap<Move, Integer>();

    public ComparatorMove(AbstractBrain parent) {
        _parent = parent;
    }

    public void init(List<Move> moves, Board board) {

        _map.clear();

        for (Move move : moves) {
            board.move(move);
            int rate = _parent.getHeuristic(board);
            _map.put(move, new Integer(rate));
            board.undo();
        }
    }

    @Override
    public int compare(Move o1, Move o2) {
        Integer rate1 = _map.get(o1);
        Integer rate2 = _map.get(o2);
        return rate1 - rate2;
    }

}
