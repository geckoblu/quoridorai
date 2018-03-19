package martijn.quoridor.ui.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.QgfUtils;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class LoadAction extends AbstractAction {

    private final Component _parent;
    private Board _board;

    public LoadAction(Component parent, Board board) {
        super();

        I18N.Action action = I18N.getAction("LOAD");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);

        _parent = parent;
        _board = board;

    }

    @Override
    public void actionPerformed(ActionEvent e) {


        QgfUtils qgfutils = new QgfUtils(_parent);

        Iterator<Move> moves = qgfutils.load();
        if (moves != null) {
            _board.newGame();
            _board.add(moves);
        }

    }

}