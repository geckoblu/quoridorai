package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Game;
import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.ui.GamePanel;

@SuppressWarnings("serial")
public class LoadAction extends AbstractAction {

    private final Component _parent;
    private final GameModel _gameModel;
    private final GamePanel _gamePanel;

    public LoadAction(Component parent, GameModel gameModel, GamePanel gamePanel) {
        super();

        I18N.Action action = I18N.getAction("LOAD");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _parent = parent;
        _gameModel = gameModel;
        _gamePanel = gamePanel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        QgfUtils qgfutils = new QgfUtils(_parent);

        Game game = qgfutils.load();
        if (game != null) {
            _gamePanel.setPointOfView(game.getPointOfView());
            _gameModel.setGame(game);
        }

    }

}
