package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;

@SuppressWarnings("serial")
public class GameStatus extends JPanel implements GameListener {

    private PlayerStatusPanel[] _lines;

    private final GameModel _gameModel;

    public GameStatus(GameModel gameModel) {

        _gameModel = gameModel;

        createGUI();

        //setup.addSetupListener(this);
        _gameModel.addGameListener(this);
    }

    private void createGUI() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 0, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;

        _lines = new PlayerStatusPanel[Board.NPLAYERS];
        for (int i = 0; i < Board.NPLAYERS; i++) {
            _lines[i] = new PlayerStatusPanel(i, _gameModel);
            gbc.gridy = i;
            add(_lines[i], gbc);
        }

        update();

    }

    private void update() {
        for (PlayerStatusPanel s : _lines) {
            s.update();
        }
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

}
