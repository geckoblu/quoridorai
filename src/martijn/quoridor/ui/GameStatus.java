package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

@SuppressWarnings("serial")
public class GameStatus extends JPanel implements BoardListener, SetupListener {

	private PlayerStatus[] lines;

	private Setup setup;
	private StatusBar statusbar;

	public GameStatus(Setup setup, Controller[] controllers, StatusBar statusbar) {

		this.setup = setup;
		this.statusbar = statusbar;

		createGUI(controllers);

		setup.addSetupListener(this);
		getBoard().addBoardListener(this);
	}

	private void createGUI(Controller[] controllers) {
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 5, 0, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;

		lines = new PlayerStatus[getBoard().getPlayers().length];
		for (int i = 0; i < getBoard().getPlayers().length; i++) {
			lines[i] = new PlayerStatus(getBoard().getPlayer(i), setup, controllers);
			gbc.gridy = i;
			add(lines[i].getPlayerStatusPanel(), gbc);
		}

		update();

	}

	private void update() {
		for (PlayerStatus s : lines) {
			s.update();
		}

		Player activePlayer = getBoard().getTurn();
		if (getBoard().isGameOver()) {
			activePlayer = getBoard().getWinner();
			statusbar.setWinner(activePlayer);
		} else {

			if (setup.getController(activePlayer).isHuman()) {
				statusbar.setPlayerToMove(activePlayer);
			} else {
				statusbar.setPlayerThinking(activePlayer);
			}
		}
	}

	/**
	 * BoardListener
	 */

	@Override
	public void moveExecuted(Move move) {
		//System.out.println("GS " + move + " " + move.notation());
		System.out.println(" ");
		Stack<Move> history = setup.getBoard().getHistory();
		Iterator<Move> iter = history.iterator();
		int i = 0;
		if (history.size() % 2 == 0) {
			while (iter.hasNext()){
				Move m1 = iter.next();
				Move m2 = iter.next();
				i++;
				System.out.println("" + i + ". " + m1.notation() + " "+ m2.notation());
			}
		}

		update();
	}

	@Override
	public void movesUndone(Move[] moves) {
		update();
	}

	@Override
	public void newGame() {
		for (int i = 0; i < getBoard().getPlayers().length; i++) {
			lines[i].setPlayer(getBoard().getPlayer(i));
		}
		update();
	}

	/**
	 * SetupListener
	 */

	@Override
	public void setupChanged(int player) {
		update();
	}

	private Board getBoard() {
		return setup.getBoard();
	}

}
