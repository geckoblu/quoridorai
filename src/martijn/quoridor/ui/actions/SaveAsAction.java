package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Game;
import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.ui.GamePanel;

@SuppressWarnings("serial")
public class SaveAsAction extends AbstractAction implements GameListener {

    private final Component _parent;
    private final GameModel _gameModel;
    private final GamePanel _gamePanel;

    public SaveAsAction(Component parent, GameModel gameModel, GamePanel gamePanel) {
        super();

        I18N.Action action = I18N.getAction("SAVE_AS");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _parent = parent;
        _gameModel = gameModel;
        _gamePanel = gamePanel;

        update();

        _gameModel.addGameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        QgfUtils qgfutils = new QgfUtils(_parent);

        Game game = _gameModel.getGame();
        game.setPointOfView(_gamePanel.getPointOfView());
        qgfutils.saveAs(game);
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

    private void update() {
        setEnabled(_gameModel.hasHistory());
    }

}