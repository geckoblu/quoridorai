package martijn.quoridor.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.model.Player;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {

    private JLabel _statusLabel;
    private PlayerIcon _playerIcon;

    public StatusBar() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEtchedBorder());

        _playerIcon = new PlayerIcon(Color.red);
        this.add(_playerIcon);
        _playerIcon.setVisible(false);

        _statusLabel = new JLabel(" ");
        this.add(_statusLabel);
    }

    public void setPlayerToMove(Player player) {
        _playerIcon.stopFlipping();
        _playerIcon.setPlayer(player);
        _playerIcon.setVisible(true);
        _statusLabel.setText("Turn to move.");
    }

    public void setPlayerThinking(Player player) {
        _playerIcon.stopFlipping();
        _playerIcon.setPlayer(player);
        _playerIcon.setVisible(true);
        _statusLabel.setText("Thinking ...");
    }

    public void setWinner(Player player) {
        _playerIcon.setPlayer(player);
        _playerIcon.setVisible(true);
        _playerIcon.startFlippingContinuously();
        _statusLabel.setText("Winner!");
    }

}
