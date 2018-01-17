package martijn.quoridor.ui;

import java.applet.Applet;
import java.applet.AudioClip;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Setup;

public class SoundPlayer implements BoardListener {

    private Board board;

    private Setup setup;

    /** The audio clip for executing a move. */
    private AudioClip stone;

    /** The audio clip for winning the game. */
    private AudioClip yahoo;

    /** The audio clip for losing the game. */
    private AudioClip yahooSad;

    public SoundPlayer(Board board, Setup setup) {
        this.board = board;
        this.setup = setup;
        board.addBoardListener(this);

        // Load audio.
        stone = Applet.newAudioClip(getClass().getResource("stone.wav"));
        yahoo = Applet.newAudioClip(getClass().getResource("yahoo1.au"));
        yahooSad = Applet.newAudioClip(getClass().getResource("yahoo2.au"));
    }

    @Override
    public void moveExecuted() {
        stone.play();
        if (board.isGameOver()) {
            AudioClip yay = yahoo;

            // Determine winner.
            int winner = board.getWinner().getIndex();

            if (setup.getController(winner).isHuman()) {
                // Maybe we need to play the sad yahoo.
                for (int i = 0; i < board.getPlayers().length; i++) {
                    if (i == winner) {
                        continue;
                    }
                    if (!setup.getController(i).isHuman()) {
                        // We've found a non-human opponent.
                        yay = yahooSad;
                        break;
                    }
                }
            }

            yay.play();
        }
    }

    @Override
    public void newGame() {
        stone.play();
    }

}
