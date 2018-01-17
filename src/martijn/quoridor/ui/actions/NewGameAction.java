package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class NewGameAction extends AbstractAction implements BoardListener {

	private Board board;

	public NewGameAction(Board board) {
		super();

		I18N.Action action = I18N.getAction("NEW_GAME");
		putValue(Action.NAME, action.name);
		putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
		putValue(Action.SHORT_DESCRIPTION, action.short_description);

		this.board = board;
		update();
		board.addBoardListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		board.newGame();
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
		setEnabled(board.hasHistory());
	}

}
