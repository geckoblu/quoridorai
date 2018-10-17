package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Game;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.ui.GamePanel;

@SuppressWarnings("serial")
public class LoadFileAction extends AbstractAction {

    private final Component _parent;
    private final GameModel _gameModel;
    private final GamePanel _gamePanel;
    private final File _file;

    public LoadFileAction(int i, Component parent, GameModel gameModel, GamePanel gamePanel, File file) {
        super();

        String path = file.getAbsolutePath();
        String name = file.getName();

        putValue(Action.NAME, i + ". " + name);
        putValue(Action.MNEMONIC_KEY, (char) 48 + i);
        putValue(Action.SHORT_DESCRIPTION, path);

        _parent = parent;
        _gameModel = gameModel;
        _gamePanel = gamePanel;
        _file = file;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        QgfUtils qgfutils = new QgfUtils(_parent);

        Game game = qgfutils.load(_file, false);
        if (game != null) {
            _gamePanel.setPointOfView(game.getPointOfView());
            _gameModel.setGame(game);
        }

    }

}
