package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.model.Player;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

public class PlayerStatus {

	// Model.

	private Setup setup;

	private Player player;

	private Controller[] controllers;

	// Components.

	private PlayerIcon icon;

	private JComboBox<Controller> cmbController;

	private JLabel walls;

	public PlayerStatus(Player player, Setup setup, Controller[] controllers) {
		this.setup = setup;
		this.player = player;
		this.controllers = controllers;
	}

	public JPanel getPlayerStatusPanel() {
		JPanel p = new JPanel();
		p.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(2, 2, 2, 2);
		//gbc.gridx = 0;

		//p.setBorder(BorderFactory.createEtchedBorder());

		icon = new PlayerIcon(player);
		gbc.gridx = 0;
		gbc.gridy = 0;
		p.add(icon, gbc);

		ControllerModel model = new ControllerModel();
		setup.addSetupListener(model);
		cmbController = new JComboBox<Controller>(model);
		gbc.gridx = 1;
		gbc.gridy = 0;
		p.add(cmbController, gbc);

		walls = new JLabel();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		p.add(walls, gbc);

		update();

		return p;
	}

	public void setPlayer(Player player) {
		this.player = player;
		icon.setPlayer(player);
	}

	void update() {
		icon.update();
		walls.setText(getWallText());

	}

	private String getWallText() {
		StringBuffer buf = new StringBuffer();

		buf.append("Walls: ");
		if (player.getWallCount() == 0) {
			buf.append("none");
		} else {
			for (int i = 0; i < player.getWallCount(); i++) {
				buf.append('|');
				if (i % 5 == 4) {
					buf.append(' ');
				}
			}
		}

		return buf.toString();
	}

	@SuppressWarnings("serial")
	private class ControllerModel extends AbstractListModel<Controller>
	                              implements ComboBoxModel<Controller>, SetupListener {

		@Override
		public Object getSelectedItem() {
			return setup.getController(player);
		}

		@Override
		public void setSelectedItem(Object controller) {
			setup.setController(player, (Controller) controller);
		}

		@Override
		public Controller getElementAt(int index) {
			return controllers[index];
		}

		@Override
		public int getSize() {
			return controllers.length;
		}

		@Override
		public void setupChanged(int player) {
			fireContentsChanged(this, 0, getSize());
		}

	}

}
