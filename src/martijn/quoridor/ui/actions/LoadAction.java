package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class LoadAction extends AbstractAction {

    private final Component _parent;
    private final GameModel _gameModel;

    public LoadAction(Component parent, GameModel gameModel) {
        super();

        I18N.Action action = I18N.getAction("LOAD");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _parent = parent;
        _gameModel = gameModel;

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        QgfUtils qgfutils = new QgfUtils(_parent);

        Iterator<Move> moves = qgfutils.load();
        if (moves != null) {
            _gameModel.newGame();
            _gameModel.add(moves);
        }

    }

}
