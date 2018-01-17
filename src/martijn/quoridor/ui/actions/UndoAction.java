package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import martijn.quoridor.I18N;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction implements BoardListener, SetupListener {

    private Setup setup;

    public UndoAction(Setup setup) {
        super();

        I18N.Action action = I18N.getAction("UNDO");
        // putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
        putValue(Action.SHORT_DESCRIPTION, action.short_description);
        URL url = getClass().getResource("/icons/go-previous.png");
        ImageIcon icon = new ImageIcon(url);
        putValue(Action.LARGE_ICON_KEY, icon);

        this.setup = setup;

        update();

        setup.addSetupListener(this);
        setup.getBoard().addBoardListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setup.getBoard().undo(getUndoLevel());
    }

    private int getUndoLevel() {
        int number = 0;
        int turn = setup.getBoard().getTurnIndex();
        do {
            if (number > setup.getBoard().getHistoryIndex()) {
                return -1;
            }
            number++;
            turn--;
            if (turn < 0) {
                turn += setup.getBoard().getPlayers().length;
            }
        } while (!setup.getController(turn).isHuman());
        return number;
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
        int n = getUndoLevel();
        setEnabled(n >= 0 && n <= setup.getBoard().getHistoryIndex());
    }

    @Override
    public void setupChanged(int player) {
        update();
    }

}
