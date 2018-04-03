package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class GameStatus extends JPanel implements GameListener, SetupListener {

    private PlayerStatus[] _lines;

    private Setup _setup;

    public GameStatus(Setup setup, Controller[] controllers) {

        _setup = setup;

        createGUI(controllers);

        setup.addSetupListener(this);
        getBoard().addBoardListener(this);
    }

    private void createGUI(Controller[] controllers) {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;

        _lines = new PlayerStatus[Board.NPLAYERS];
        for (int i = 0; i < Board.NPLAYERS; i++) {
            _lines[i] = new PlayerStatus(getBoard().getPlayer(i), _setup, controllers);
            gbc.gridy = i;
            add(_lines[i].getPlayerStatusPanel(), gbc);
        }

        update();

    }

    private void update() {
        for (PlayerStatus s : _lines) {
            s.update();
        }
    }

    /**
     * BoardListener
     */

    @Override
    public void moveExecuted() {
        update();
    }

    @Override
    public void newGame() {
        for (int i = 0; i < Board.NPLAYERS; i++) {
            _lines[i].setPlayer(getBoard().getPlayer(i));
        }
        update();
    }

    /**
     * SetupListener
     */

    @Override
    public void setupChanged(int player) {
        update();
    }

    private Board getBoard() {
        return _setup.getBoard();
    }

}
