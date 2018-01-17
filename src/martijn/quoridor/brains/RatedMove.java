package martijn.quoridor.brains;

import martijn.quoridor.model.Move;

public class RatedMove implements Comparable {

    private Move move;

    private int rating;

    public RatedMove(Move move, int rating) {
        super();
        this.move = move;
        this.rating = rating;
    }

    public Move getMove() {
        return move;
    }

    public int getRating() {
        return rating;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RatedMove)) {
            return false;
        }
        RatedMove rm = (RatedMove) o;
        return move.equals(rm.move) && rating == rm.rating;
    }

    @Override
    public int compareTo(Object o) {
        RatedMove rm = (RatedMove) o;
        return rm.rating - rating;
    }

    @Override
    public String toString() {
        return move + " @ " + rating;
    }

}
