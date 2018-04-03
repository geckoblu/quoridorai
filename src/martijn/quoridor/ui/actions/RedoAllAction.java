package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import martijn.quoridor.I18N;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class RedoAllAction extends AbstractAction implements GameListener, SetupListener {

    private final GameModel _gameModel;

    public RedoAllAction(GameModel gameModel) {
        super();

        I18N.Action action = I18N.getAction("REDO_ALL");
        // putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);
        URL url = getClass().getResource("/icons/go-last.png");
        ImageIcon icon = new ImageIcon(url);
        putValue(Action.LARGE_ICON_KEY, icon);

        _gameModel = gameModel;

        update();

        _gameModel.addGameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _gameModel.redoAll();
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
        setEnabled(_gameModel.canRedo());
    }

    @Override
    public void setupChanged(int player) {
        update();
    }

}
