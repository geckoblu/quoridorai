package martijn.quoridor.brains;

import martijn.quoridor.model.Move;

public class RatedMove implements Comparable<RatedMove> {

    private Move _move;

    private int _rating;

    public RatedMove(Move move, int rating) {
        super();
        this._move = move;
        this._rating = rating;
    }

    public Move getMove() {
        return _move;
    }

    public int getRating() {
        return _rating;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RatedMove)) {
            return false;
        }
        RatedMove rm = (RatedMove) o;
        return _move.equals(rm._move) && _rating == rm._rating;
    }

    @Override
    public int compareTo(RatedMove o) {
        RatedMove rm = o;
        return rm._rating - _rating;
    }

    @Override
    public String toString() {
        return _move + " @ " + _rating;
    }

}
