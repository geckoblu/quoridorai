package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class RedoAllAction extends AbstractAction implements BoardListener, SetupListener {

    private Board _board;

    public RedoAllAction(Board board) {
        super();

        I18N.Action action = I18N.getAction("REDO_ALL");
        // putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);
        URL url = getClass().getResource("/icons/go-last.png");
        ImageIcon icon = new ImageIcon(url);
        putValue(Action.LARGE_ICON_KEY, icon);

        _board = board;

        update();

        _board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _board.redoAll();
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
        setEnabled(_board.canRedo());
    }

    @Override
    public void setupChanged(int player) {
        update();
    }

}
