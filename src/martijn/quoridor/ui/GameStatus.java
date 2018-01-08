package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

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
			lines[i] = new PlayerStatus(getBoard().getPlayers()[i], setup, controllers);
			gbc.gridy = i;
			add(lines[i].getPlayerStatusPanel(), gbc);
		}

		update();

	}

	private void update() {
		for (PlayerStatus s : lines) {
			s.update();
		}

		System.out.println("GS update");
		Player activePlayer = getBoard().getTurn();
		if (getBoard().isGameOver()) {
			activePlayer = getBoard().getWinner();
		}

		if (setup.getController(activePlayer).isHuman()) {
			statusbar.setPlayerToMove(activePlayer);
		} else {
			statusbar.setPlayerThinking(activePlayer);
		}
	}

	/**
	 * BoardListener
	 */

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
		System.out.println("GS newGame");
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
