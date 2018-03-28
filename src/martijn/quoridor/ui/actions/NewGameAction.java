package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;

@SuppressWarnings("serial")
public class NewGameAction extends AbstractAction implements BoardListener {

    private final Board _board;

    public NewGameAction(Board board) {
        super();

        I18N.Action action = I18N.getAction("NEW_GAME");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _board = board;
        update();
        board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _board.newGame();
        Config.lastLoadFile(null);
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
