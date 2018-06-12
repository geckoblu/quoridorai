package martijn.quoridor.ui;

import java.applet.Applet;
import java.applet.AudioClip;

import martijn.quoridor.Config;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Player;

public class SoundPlayer implements GameListener {

    private GameModel _gameModel;

    /** The audio clip for executing a move. */
    private AudioClip _stone;

    /** The audio clip for winning the game. */
    private AudioClip _yahoo;

    /** The audio clip for losing the game. */
    private AudioClip _yahooSad;

    public SoundPlayer(GameModel gameModel) {
        _gameModel = gameModel;
        gameModel.addGameListener(this);
    }

    @Override // BoardListener
    public void boardChanged() {
        playSound();
    }

    private void playSound() {

        if (!Config.getPlaySounds()) {
            return;
        }

        getStone().play();
        if (_gameModel.isGameOver()) {

            AudioClip yay = getYahoo();

            // Determine winner.
            Player winner = _gameModel.getWinner();

            if (_gameModel.getController(winner).isHuman()) {
                // Maybe we need to play the sad yahoo.
                for (Player p : _gameModel.getPlayers()) {
                    if (p == winner) {
                        continue;
                    }
                    if (!_gameModel.getController(p).isHuman()) {
                        // We've found a non-human opponent.
                        yay = getYahooSad();
                        break;
                    }
                }
            }

            yay.play();
        }
    }

    private AudioClip getStone() {
        if (_stone == null) {
            _stone = Applet.newAudioClip(getClass().getResource("/sounds/stone.wav"));
        }
        return _stone;
    }

    private AudioClip getYahooSad() {
        if (_yahooSad == null) {
            _yahooSad = Applet.newAudioClip(getClass().getResource("/sounds/yahoo2.au"));
        }
        return _yahooSad;
    }

    private AudioClip getYahoo() {
        if (_yahoo == null) {
            _yahoo = Applet.newAudioClip(getClass().getResource("/sounds/yahoo1.au"));
        }
        return _yahoo;
    }

}
