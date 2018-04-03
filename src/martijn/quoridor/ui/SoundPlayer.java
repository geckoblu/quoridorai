package martijn.quoridor.ui;

import java.applet.Applet;
import java.applet.AudioClip;

import martijn.quoridor.Config;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Setup;

public class SoundPlayer implements GameListener {

    private GameModel _gameModel;

    private Setup _setup;

    /** The audio clip for executing a move. */
    private AudioClip _stone;

    /** The audio clip for winning the game. */
    private AudioClip _yahoo;

    /** The audio clip for losing the game. */
    private AudioClip _yahooSad;

    public SoundPlayer(GameModel gameModel, Setup setup) {
        _gameModel = gameModel;
        _setup = setup;
        gameModel.addGameListener(this);

        // Load audio.
        _stone = Applet.newAudioClip(getClass().getResource("/sounds/stone.wav"));
        _yahoo = Applet.newAudioClip(getClass().getResource("/sounds/yahoo1.au"));
        _yahooSad = Applet.newAudioClip(getClass().getResource("/sounds/yahoo2.au"));
    }

    @Override
    public void moveExecuted() {

        if (!Config.playSounds()) {
            return;
        }

        _stone.play();
        if (_gameModel.isGameOver()) {
            AudioClip yay = _yahoo;

            // Determine winner.
            Player winner = _gameModel.getWinner();

            if (_setup.getController(winner).isHuman()) {
                // Maybe we need to play the sad yahoo.
                for (Player p : _gameModel.getPlayers()) {
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
        if (Config.playSounds()) {
            _stone.play();
        }
    }

}
