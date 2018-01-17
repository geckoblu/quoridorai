package martijn.quoridor.ui;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import martijn.quoridor.model.Player;

@SuppressWarnings("serial")
public class StatusBar extends JPanel {

    private JLabel statusLabel;
    private PlayerIcon playerIcon;

    public StatusBar() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEtchedBorder());

        playerIcon = new PlayerIcon(Color.red);
        this.add(playerIcon);
        playerIcon.setVisible(false);

        statusLabel = new JLabel(" ");
        this.add(statusLabel);
    }

    public void setPlayerToMove(Player player) {
        playerIcon.stopFlipping();
        playerIcon.setPlayer(player);
        playerIcon.setVisible(true);
        statusLabel.setText("Turn to move.");
    }

    public void setPlayerThinking(Player player) {
        playerIcon.stopFlipping();
        playerIcon.setPlayer(player);
        playerIcon.setVisible(true);
        statusLabel.setText("Thinking ...");
    }

    public void setWinner(Player player) {
        playerIcon.setPlayer(player);
        playerIcon.setVisible(true);
        playerIcon.startFlippingContinuously();
        statusLabel.setText("Winner!");
    }

}
