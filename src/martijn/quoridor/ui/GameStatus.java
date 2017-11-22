package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.BoardListener;
import martijn.quoridor.model.Move;

@SuppressWarnings("serial")
public class GameStatus extends JPanel implements BoardListener, SetupListener {

	private PlayerStatus[] lines;

	private GamePanel game;

	public GameStatus(GamePanel game) {
		this.game = game;
		createGUI();
		getBoard().addBoardListener(this);
		getSetup().addSetupListener(this);
	}

	private Setup getSetup() {
		return game.getSetup();
	}

	private Board getBoard() {
		return game.getBoard();
	}

	private void createGUI() {
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 5, 0, 5);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0;

		lines = new PlayerStatus[getBoard().getPlayers().length];
		for (int i = 0; i < getBoard().getPlayers().length; i++) {
			lines[i] = new PlayerStatus(game, i);
			gbc.gridy = i;
			add(lines[i].getPlayerStatusPanel(), gbc);
		}

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

	@Override
	public void setupChanged(int player) {
		update();
	}

	private void update() {
		for (PlayerStatus s : lines) {
			s.update();
		}
	}

}
