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
import martijn.quoridor.model.Controller;
import martijn.quoridor.model.GameModel;

@SuppressWarnings("serial")
public final class PlayerStatusPanel extends JPanel {

    private static final String NONE = I18N.tr("NONE");

    private final int _playerIndex;
    private final GameModel _gameModel;
    private final Controller[] _controllers;

    private final PlayerIcon _icon;

    private final JComboBox<Controller> _cmbController;

    private final JLabel _walls;

    public PlayerStatusPanel(int i, GameModel gameModel) {
        _gameModel = gameModel;
        _playerIndex = i;
        _controllers = _gameModel.getControllers();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(2, 2, 2, 2);

        _icon = new PlayerIcon(_gameModel.getPlayer(_playerIndex));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(_icon, gbc);

        ControllerModel model = new ControllerModel();
        _cmbController = new JComboBox<Controller>(model);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(_cmbController, gbc);

        _walls = new JLabel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(_walls, gbc);

        update();

    }

    void update() {
        _icon.update();
        _walls.setText(getWallText());

    }

    private String getWallText() {

        int wallCount = _gameModel.getPlayer(_playerIndex).getWallCount();

        StringBuffer buf = new StringBuffer();

        buf.append(I18N.tr("WALLS") + ": ");
        if (wallCount == 0) {
            buf.append(NONE);
        } else {
            for (int i = 0; i < wallCount; i++) {
                buf.append('|');
                if (i % 5 == 4) {
                    buf.append(' ');
                }
            }
        }

        return buf.toString();
    }

    private class ControllerModel extends AbstractListModel<Controller> implements ComboBoxModel<Controller> {

        @Override
        public Object getSelectedItem() {
            return _gameModel.getController(_gameModel.getPlayer(_playerIndex));
        }

        @Override
        public void setSelectedItem(Object controller) {
            _gameModel.setController(_gameModel.getPlayer(_playerIndex), (Controller) controller);
        }

        @Override
        public Controller getElementAt(int index) {
            return _controllers[index];
        }

        @Override
        public int getSize() {
            return _controllers.length;
        }

    }

}
