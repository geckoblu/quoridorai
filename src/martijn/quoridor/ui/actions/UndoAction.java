package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction implements BoardListener {

    private final Board _board;

    public UndoAction(Board board) {
        super();

        I18N.Action action = I18N.getAction("UNDO");
        // putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        URL url = getClass().getResource("/icons/go-previous.png");
        ImageIcon icon = new ImageIcon(url);
        putValue(Action.LARGE_ICON_KEY, icon);

        //putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke((char)KeyEvent.VK_LEFT));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke('u'));

        _board = board;

        update();

        _board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _board.undo();
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
        setEnabled(_board.canUndo());
    }

}
