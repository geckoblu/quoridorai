package martijn.quoridor.model;

import martijn.quoridor.ui.Controller;
import martijn.quoridor.ui.HumanController;

/**
 * Setup describes a complete game setup: board and controllers.
 */
public class Setup {

    private Controller[] _controllers;
    private final HumanController _humanController;

    public Setup(HumanController humanController, Controller[] controllers) {
        if (controllers.length != Board.NPLAYERS) {
            throw new IllegalArgumentException("Player number mismatch.");
        }

        _humanController = humanController;

        _controllers = controllers;

        // Activate controllers.
        for (int i = 0; i < controllers.length; i++) {
            controllers[i].startControlling(i);
        }
    }

    public Controller getController(int player) {
        return _controllers[player];
    }

    public Controller getController(Player player) {
        return _controllers[player.index];
    }

    public void setController(Player player, Controller controller) {
        if (_controllers[player.index] != controller) {
            _controllers[player.index].stopControlling(player.index);
            _controllers[player.index] = controller;
            controller.startControlling(player.index);
        }
    }

    public void pauseBrainController() {
        for (int i = 0; i < Board.NPLAYERS; i++) {
            if (!_controllers[i].isHuman()) {
                _controllers[i].pause();
                _humanController.startControlling(i);
            }
        }
    }

    public void restartBrainController() {
        for (int i = 0; i < Board.NPLAYERS; i++) {
            if (!_controllers[i].isHuman()) {
                _humanController.stopControlling(i);
                _controllers[i].restart();
            }
        }
    }

}
