package martijn.quoridor.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JOptionPane;

import martijn.quoridor.Config;
import martijn.quoridor.Core;
import martijn.quoridor.I18N;
import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.ui.BoardCanvas;

public final class GameModel {

    private final Board _board;

    private Setup _setup;

    private Controller[] _controllers;

    private final List<GameListener> _gameListeners = new LinkedList<GameListener>();
    private final List<SetupListener> _setupListeners = new LinkedList<SetupListener>();


    public GameModel() {
        _board  = new Board();
    }

    public Board getBoard() {
        return _board;
    }

    public void newGame() {
        _board.newGame();
        fireBoardChanged();
    }

    public void move(Move move) {
        _board.move(move);
        _setup.restartBrainController();
        fireBoardChanged();
    }

    public void add(Iterator<Move> moves) {
        _board.add(moves);
        fireBoardChanged();
    }

    public boolean hasHistory() {
        return _board.hasHistory();
    }

    public Iterator<Move> getHistory() {
        return _board.getHistory();
    }

    public int getHistoryIndex() {
        return _board.getHistoryIndex();
    }

    public boolean canUndo() {
        return _board.canUndo();
    }

    public void undo() {
        _setup.pauseBrainController();
        _board.undo();
        fireBoardChanged();
    }

    public void undoAll() {
        _setup.pauseBrainController();
        _board.undoAll();
        fireBoardChanged();
    }

    public boolean canRedo() {
        return _board.canRedo();
    }

    public void redo() {
        _board.redo();
        fireBoardChanged();
    }

    public void redoAll() {
        _board.redoAll();
        fireBoardChanged();
    }

    public boolean isGameOver() {
        return _board.isGameOver();
    }

    public Player getWinner() {
        return _board.getWinner();
    }

    public Iterable<Player> getPlayers() {
        return _board.getPlayers();
    }

    public Player getPlayer(int i) {
        return _board.getPlayer(i);
    }

    public Player getTurn() {
        return _board.getTurn();
    }

    /** Causes the listener to be notified of subsequent board events. */
    public void addGameListener(GameListener l) {
        _gameListeners.add(l);
    }

    /** Causes the listener to no longer be notified of subsequent board events. */
    public void removeGameListener(GameListener l) {
        _gameListeners.remove(l);
    }

    private void fireBoardChanged() {
        for (GameListener l : _gameListeners) {
            l.boardChanged();
        }
    }

    public void addSetupListener(SetupListener l) {
        _setupListeners.add(l);
    }

    public void removeSetupListener(SetupListener l) {
        _setupListeners.remove(l);
    }

    protected void fireSetupChanged() {
        for (SetupListener l : _setupListeners) {
            l.setupChanged();
        }
    }

    public void initControllers(BoardCanvas boardCanvas) {
        BrainFactory factory = new DefaultBrainFactory();
        List<Brain> brains = factory.getBrains();

        HumanController humanController = new HumanController(this, boardCanvas);

        _controllers = new Controller[brains.size() + 1];
        _controllers[0] = humanController;
        for (int i = 0; i < brains.size(); i++) {
            _controllers[i + 1] = new BrainController(this, brains.get(i));
        }

        Controller controller0 =  getController(0);
        Controller controller1 =  getController(1);

        _setup = new Setup(humanController, new Controller[] {controller0, controller1});
    }

    public Controller[] getControllers() {
        return _controllers;
    }

    public Controller getController(Player player) {
        return _setup.getController(player);
    }

    public void setController(Player player, Controller controller) {
        _setup.setController(player, controller);
        Config.setBrain(player.index, controller.getName());
        fireSetupChanged();
    }

    private Controller getController(int playerIndex) {

        String brainName = Config.getBrain(playerIndex);

        for (Controller controller : _controllers) {
            if (controller.getName().equals(brainName)) {
                return controller;
            }
        }

        // Something went wrong
        Controller fallback = _controllers[0];
        String title = I18N.tr("WRONG_BRAIN_TITLE");
        String message = I18N.tr("WRONG_BRAIN_MESSAGE");
        message = message.replace("$1", brainName);
        message = message.replace("$2", fallback.getName());
        Core.LOGGER.log(Level.WARNING, message.replace('\n', ' '));
        JOptionPane.showMessageDialog(Core.getRootComponent(), message, title, JOptionPane.WARNING_MESSAGE);
        Config.setBrain(playerIndex, fallback.getName());
        return fallback;
    }

}
