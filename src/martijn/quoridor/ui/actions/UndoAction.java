package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction implements BoardListener,
		SetupListener {

	private Setup setup;

	public UndoAction(Setup setup) {
		super();

		I18N.Action action = I18N.getAction("UNDO");
		putValue(Action.NAME, action.name);
		putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
		putValue(Action.SHORT_DESCRIPTION, action.short_description);

		this.setup = setup;

		update();

		setup.addSetupListener(this);
		setup.getBoard().addBoardListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setup.getBoard().undo(getUndoLevel());
	}

	public int getUndoLevel() {
		int number = 0;
		int turn = setup.getBoard().getTurnIndex();
		do {
			if (number > setup.getBoard().getHistory().size()) {
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
	public void moveExecuted(Move move) {
		update();
	}

	@Override
	public void movesUndone(Move[] moves) {
		update();
	}

	@Override
	public void newGame() {
		update();
	}

	private void update() {
		int n = getUndoLevel();
		setEnabled(n >= 0 && n <= setup.getBoard().getHistory().size());
	}

	@Override
	public void setupChanged(int player) {
		update();
	}

}
