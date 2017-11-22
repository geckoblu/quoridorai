package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.model.Board;
import martijn.quoridor.model.Player;

public class PlayerStatus {

	// Model.

	private GamePanel game;

	private int playerIndex;

	// Components.

	private PlayerIcon icon;

	private JComboBox<Controller> controller;

	private JLabel walls;

	public PlayerStatus(GamePanel game, int player) {
		this.game = game;
		this.playerIndex = player;
	}

	public JPanel getPlayerStatusPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 2, 2, 2);
		//gbc.gridx = 0;

		//p.setBorder(BorderFactory.createEtchedBorder());

		icon = new PlayerIcon(getBoard(), playerIndex);
		gbc.gridx = 0;
		gbc.gridy = 0;
		p.add(icon, gbc);

		ControllerModel model = new ControllerModel();
		getSetup().addSetupListener(model);
		controller = new JComboBox<Controller>(model);
		gbc.gridx = 1;
		gbc.gridy = 0;
		p.add(controller, gbc);

		walls = new JLabel();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		p.add(walls, gbc);

		update();

		return p;
	}

	private Board getBoard() {
		return game.getBoard();
	}

	public Setup getSetup() {
		return game.getSetup();
	}

	void update() {
		icon.update();

		// Find the active player.
		Player activePlayer = getBoard().getTurn();
		if (getBoard().isGameOver()) {
			activePlayer = getBoard().getWinner();
		}
		boolean active = activePlayer == getPlayer();

		// Set icon solidness.
		icon.setSolid(active);

		icon.stopFlipping();
		if (getPlayer().isWinner()) {
			icon.startFlippingContinuously();
		} else if (active && !getSetup().getController(playerIndex).isHuman()) {
			icon.startFlippingSlowly();
		}

		walls.setText(getWallText());

	}

	private String getWallText() {
		StringBuffer buf = new StringBuffer();
		if (getBoard().isGameOver()) {
			if (getPlayer().isWinner()) {
				buf.append("Winner!");
			}
		} else {
			buf.append("Walls: ");
			if (getPlayer().getWallCount() == 0) {
				buf.append("none");
			} else {
				for (int i = 0; i < getPlayer().getWallCount(); i++) {
					buf.append('|');
					if (i % 5 == 4) {
						buf.append(' ');
					}
				}
			}
		}
		return buf.toString();
	}

	private Player getPlayer() {
		return getBoard().getPlayers()[playerIndex];
	}

	@SuppressWarnings("serial")
	private class ControllerModel extends AbstractListModel<Controller>
	                              implements ComboBoxModel<Controller>, SetupListener {

		@Override
		public Object getSelectedItem() {
			return getSetup().getController(playerIndex);
		}

		@Override
		public void setSelectedItem(Object controller) {
			getSetup().setController(playerIndex, (Controller) controller);
		}

		@Override
		public Controller getElementAt(int index) {
			return game.getControllers()[index];
		}

		@Override
		public int getSize() {
			return game.getControllers().length;
		}

		@Override
		public void setupChanged(int player) {
			fireContentsChanged(this, 0, getSize());
		}

	}

}
