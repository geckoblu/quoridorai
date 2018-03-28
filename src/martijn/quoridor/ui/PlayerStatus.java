package martijn.quoridor.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.I18N;
import martijn.quoridor.model.Player;
import martijn.quoridor.model.Setup;
import martijn.quoridor.model.SetupListener;

public class PlayerStatus {

    // Model.

    private Setup _setup;

    private Player _player;

    private Controller[] _controllers;

    // Components.

    private PlayerIcon _icon;

    private JComboBox<Controller> _cmbController;

    private JLabel _walls;

    public PlayerStatus(Player player, Setup setup, Controller[] controllers) {
        this._setup = setup;
        this._player = player;
        this._controllers = controllers;
    }

    public JPanel getPlayerStatusPanel() {
        JPanel p = new JPanel();
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);
        // gbc.gridx = 0;

        // p.setBorder(BorderFactory.createEtchedBorder());

        _icon = new PlayerIcon(_player);
        gbc.gridx = 0;
        gbc.gridy = 0;
        p.add(_icon, gbc);

        ControllerModel model = new ControllerModel();
        _setup.addSetupListener(model);
        _cmbController = new JComboBox<Controller>(model);
        gbc.gridx = 1;
        gbc.gridy = 0;
        p.add(_cmbController, gbc);

        _walls = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        p.add(_walls, gbc);

        update();

        return p;
    }

    public void setPlayer(Player player) {
        this._player = player;
        _icon.setPlayer(player);
    }

    void update() {
        _icon.update();
        _walls.setText(getWallText());

    }

    private String getWallText() {
        StringBuffer buf = new StringBuffer();

        buf.append(I18N.tr("WALLS") + ": ");
        if (_player.getWallCount() == 0) {
            buf.append("none");
        } else {
            for (int i = 0; i < _player.getWallCount(); i++) {
                buf.append('|');
                if (i % 5 == 4) {
                    buf.append(' ');
                }
            }
        }

        return buf.toString();
    }

    @SuppressWarnings("serial")
    private class ControllerModel extends AbstractListModel<Controller> implements ComboBoxModel<Controller>,
            SetupListener {

        @Override
        public Object getSelectedItem() {
            return _setup.getController(_player);
        }

        @Override
        public void setSelectedItem(Object controller) {
            _setup.setController(_player, (Controller) controller);
        }

        @Override
        public Controller getElementAt(int index) {
            return _controllers[index];
        }

        @Override
        public int getSize() {
            return _controllers.length;
        }

        @Override
        public void setupChanged(int player) {
            fireContentsChanged(this, 0, getSize());
        }

    }

}
