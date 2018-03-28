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
public class RedoAction extends AbstractAction implements BoardListener, SetupListener {

    private final Board _board;

    public RedoAction(Board board) {
        super();

        I18N.Action action = I18N.getAction("REDO");
        // putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);
        URL url = getClass().getResource("/icons/go-next.png");
        ImageIcon icon = new ImageIcon(url);
        putValue(Action.LARGE_ICON_KEY, icon);

        _board = board;

        update();

        _board.addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _board.redo();
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
