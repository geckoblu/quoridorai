package martijn.quoridor.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.model.Controller;
import martijn.quoridor.model.GameListener;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.Player;

@SuppressWarnings("serial")
public final class StatusBar extends JPanel implements GameListener {

    private final JLabel _statusLabel;
    private final PlayerIcon _playerIcon;

    private final GameModel _gameModel;

    public StatusBar(GameModel gameModel) {

        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEtchedBorder());

        _gameModel = gameModel;
        _gameModel.addGameListener(this);

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

    private void update() {

        Player activePlayer = _gameModel.getTurn();
        if (_gameModel.isGameOver()) {
            activePlayer = _gameModel.getWinner();
            this.setWinner(activePlayer);
        } else {

            Controller controller = _gameModel.getController(activePlayer);
            if (controller.isHuman() || controller.isPaused()) {
                this.setPlayerToMove(activePlayer);
            } else {
                this.setPlayerThinking(activePlayer);
            }
        }
    }

    @Override // BoardListener
    public void boardChanged() {
        update();
    }

}
