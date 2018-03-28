package martijn.quoridor.model;

import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.ui.Controller;
import martijn.quoridor.ui.HumanController;

/**
 * Setup describes a complete game setup: board and controllers.
 */
public class Setup {

    private final Board _board;
    private Controller[] _controllers;
    private final HumanController _humanController;

    private List<SetupListener> _listeners;

    public Setup(Board board, HumanController humanController, Controller[] controllers) {
        if (controllers.length != Board.NPLAYERS) {
            throw new IllegalArgumentException("Player number mismatch.");
        }

        _board = board;
        _board.setSetup(this);

        _humanController = humanController;

        _controllers = controllers;
        _listeners = new LinkedList<SetupListener>();

        // Activate controllers.
        for (int i = 0; i < controllers.length; i++) {
            controllers[i].startControlling(i);
        }
    }

    public Board getBoard() {
        return _board;
    }

    public void addSetupListener(SetupListener l) {
        _listeners.add(l);
    }

    public void removeSetupListener(SetupListener l) {
        _listeners.remove(l);
    }

    protected void fireSetupChanged(int player) {
        for (SetupListener l : _listeners) {
            l.setupChanged(player);
        }
    }

    public Controller getController(int player) {
        return _controllers[player];
    }

    public Controller getController(Player player) {
        return _controllers[player._index];
    }

    public void setController(Player player, Controller controller) {
        if (_controllers[player._index] != controller) {
            _controllers[player._index].stopControlling(player._index);
            _controllers[player._index] = controller;
            controller.startControlling(player._index);
            fireSetupChanged(player._index);
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
