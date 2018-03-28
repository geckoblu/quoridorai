package martijn.quoridor.ui;

import java.applet.Applet;
import java.applet.AudioClip;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Setup;

public class SoundPlayer implements BoardListener {

    private Board _board;

    private Setup _setup;

    /** The audio clip for executing a move. */
    private AudioClip _stone;

    /** The audio clip for winning the game. */
    private AudioClip _yahoo;

    /** The audio clip for losing the game. */
    private AudioClip _yahooSad;

    public SoundPlayer(Board board, Setup setup) {
        this._board = board;
        this._setup = setup;
        board.addBoardListener(this);

        // Load audio.
        _stone = Applet.newAudioClip(getClass().getResource("/sounds/stone.wav"));
        _yahoo = Applet.newAudioClip(getClass().getResource("/sounds/yahoo1.au"));
        _yahooSad = Applet.newAudioClip(getClass().getResource("/sounds/yahoo2.au"));
    }

    @Override
    public void moveExecuted() {
        _stone.play();
        if (_board.isGameOver()) {
            AudioClip yay = _yahoo;

            // Determine winner.
            Player winner = _board.getWinner();

            if (_setup.getController(winner).isHuman()) {
                // Maybe we need to play the sad yahoo.
                for (Player p: _board.getPlayers()) {
                    if (p == winner) {
                        continue;
                    }
                    if (!_setup.getController(p).isHuman()) {
                        // We've found a non-human opponent.
                        yay = _yahooSad;
                        break;
                    }
                }
            }

            yay.play();
        }
    }

    @Override
    public void newGame() {
        _stone.play();
    }

}
