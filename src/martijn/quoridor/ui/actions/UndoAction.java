package martijn.quoridor.ui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;
import martijn.quoridor.ui.GamePanel;
import martijn.quoridor.ui.SetupListener;

@SuppressWarnings("serial")
public class UndoAction extends AbstractAction implements BoardListener,
		SetupListener {

	private GamePanel game;

	public UndoAction(GamePanel game) {
		super();
		
		I18N.Action action = I18N.getAction("UNDO");
		putValue(Action.NAME, action.name);
		putValue(Action.MNEMONIC_KEY, action.mnemonic_key);
		putValue(Action.SHORT_DESCRIPTION, action.short_description);
		
		this.game = game;
		update();
		game.getBoard().addBoardListener(this);
		game.getSetup().addSetupListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		game.getBoard().undo(getUndoLevel());
	}

	public int getUndoLevel() {
		Board board = game.getBoard();
		int number = 0;
		int turn = board.getTurnIndex();
		do {
			if (number > board.getHistory().size()) {
				return -1;
			}
			number++;
			turn--;
			if (turn < 0) {
				turn += board.getPlayers().length;
			}
		} while (!game.getSetup().getController(turn).isHuman());
		return number;
	}

	public void moveExecuted(Move move) {
		update();
	}

	public void movesUndone(Move[] moves) {
		update();
	}

	public void newGame() {
		update();
	}

	private void update() {
		int n = getUndoLevel();
		setEnabled(n >= 0 && n <= game.getBoard().getHistory().size());
	}

	public void setupChanged(int player) {
		update();
	}

}
