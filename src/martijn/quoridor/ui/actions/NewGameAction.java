package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.Config;
import martijn.quoridor.I18N;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;

@SuppressWarnings("serial")
public class NewGameAction extends AbstractAction implements GameListener {

    private final GameModel _gameModel;

    public NewGameAction(GameModel board) {
        super();

        I18N.Action action = I18N.getAction("NEW_GAME");
        putValue(Action.NAME, action.name);
        putValue(Action.MNEMONIC_KEY, action.mnemonicKey);
        putValue(Action.SHORT_DESCRIPTION, action.shortDescription);

        _gameModel = board;
        update();
        board.addGameListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _gameModel.newGame();
        Config.setLastLoadFile(null);
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

    private void update() {
        setEnabled(_gameModel.hasHistory());
    }

}
