package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction implements GameListener {

    private final Component _parent;
    private final GameModel _gameModel;

    public SaveAction(Component parent, GameModel gameModel) {
        super();

        I18N.Action action = I18N.getAction("SAVE");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _parent = parent;
        _gameModel = gameModel;

        update();

        _gameModel.addGameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        QgfUtils qgfutils = new QgfUtils(_parent);

        qgfutils.save(_gameModel.getHistory());
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

    private void update() {
        setEnabled(_gameModel.hasHistory());
    }

}
