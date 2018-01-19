package martijn.quoridor.model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.ui.Controller;

/**
 * Setup describes a complete game setup: board and controllers.
 */
public class Setup implements Iterable<Controller> {

    private Board board;
    private Controller[] controllers;

    private List<SetupListener> listeners;

    public Setup(Board board, Controller[] controllers) {
        if (board.getPlayers().length != controllers.length) {
            throw new IllegalArgumentException("Player number mismatch.");
        }

        this.board = board;
        this.controllers = controllers;
        listeners = new LinkedList<SetupListener>();

        // Activate controllers.
        for (int i = 0; i < controllers.length; i++) {
            controllers[i].startControlling(i);
        }
    }

    public Board getBoard() {
        return board;
    }

    @Override
    public Iterator<Controller> iterator() {
        return Arrays.asList(controllers).iterator();
    }

    public void addSetupListener(SetupListener l) {
        listeners.add(l);
    }

    public void removeSetupListener(SetupListener l) {
        listeners.remove(l);
    }

    protected void fireSetupChanged(int player) {
        for (SetupListener l : listeners) {
            l.setupChanged(player);
        }
    }

    public Controller getController(int player) {
        return controllers[player];
    }

    public Controller getController(Player player) {
        return controllers[player.getIndex()];
    }

    public void setController(Player player, Controller controller) {
        if (controllers[player.getIndex()] != controller) {
            controllers[player.getIndex()].stopControlling(player.getIndex());
            controllers[player.getIndex()] = controller;
            controller.startControlling(player.getIndex());
            fireSetupChanged(player.getIndex());
        }
    }

}
