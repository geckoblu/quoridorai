package martijn.quoridor;

import java.util.Iterator;

import martijn.quoridor.model.Move;
import martijn.quoridor.model.Notation;
import martijn.quoridor.model.PointOfView;

public class Game {

    private String _player1;
    private String _player2;
    private Notation _notation;
    private Iterator<Move> _moves;
    private PointOfView _pointOfView;

    public Game(String player1, String player2, Notation notation, Iterator<Move> moves, PointOfView pointOfView) {
        _player1 = player1;
        _player2 = player2;
        _notation = notation;
        _moves = moves;
        _pointOfView = pointOfView;
    }

    public Game(String player1, String player2, Notation notation, Iterator<Move> moves) {
        _player1 = player1;
        _player2 = player2;
        _notation = notation;
        _moves = moves;
        _pointOfView = PointOfView.POV1;
    }

    /**
     * @return the Player1
     */
    public String getPlayer1() {
        return _player1;
    }

    /**
     * @return the Player2
     */
    public String getPlayer2() {
        return _player2;
    }

    /**
     * @return the Notation
     */
    public Notation getNotation() {
        return _notation;
    }

    /**
     * @return the Moves
     */
    public Iterator<Move> getMoves() {
        return _moves;
    }

    /**
     * @return the PointOfView
     */
    public PointOfView getPointOfView() {
        return _pointOfView;
    }

    /**
     * @param _pointOfView
     *            the PointOfView to set
     */
    public void setPointOfView(PointOfView pointOfView) {
        _pointOfView = pointOfView;
    }

}
