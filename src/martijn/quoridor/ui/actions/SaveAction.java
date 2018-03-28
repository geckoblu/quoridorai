package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction implements BoardListener {

    private final Component _parent;
    private final Board _board;

    public SaveAction(Component parent, Board board) {
        super();

        I18N.Action action = I18N.getAction("SAVE");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _parent = parent;
        _board = board;

        update();

        _board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        QgfUtils qgfutils = new QgfUtils(_parent);

        qgfutils.save(_board.getHistory());
    }

    @Override
    public void moveExecuted() {
        update();
    }

    @Override
    public void newGame() {
        update();
    }

    private void update() {
        setEnabled(_board.hasHistory());
    }

}
