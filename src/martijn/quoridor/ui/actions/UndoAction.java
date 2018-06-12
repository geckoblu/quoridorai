package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import martijn.quoridor.I18N;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction implements GameListener {

    private final GameModel _gameModel;

    public UndoAction(GameModel gameModel) {
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

        _gameModel = gameModel;

        update();

        _gameModel.addGameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _gameModel.undo();
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

    private void update() {
        setEnabled(_gameModel.canUndo());
    }

}
