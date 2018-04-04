package martijn.quoridor.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.ui.BoardCanvas;
import martijn.quoridor.ui.BrainController;
import martijn.quoridor.ui.Controller;
import martijn.quoridor.ui.HumanController;

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

    protected void fireSetupChanged(int player) {
        for (SetupListener l : _setupListeners) {
            l.setupChanged(player);
        }
    }

    public void initControllers(BoardCanvas boardCanvas) {
        BrainFactory factory = new DefaultBrainFactory();

        List<Brain> brains = new ArrayList<Brain>();
        factory.addBrains(brains);

        _controllers = new Controller[brains.size() + 1];
        _controllers[0] = new HumanController(this, boardCanvas);
        for (int i = 0; i < brains.size(); i++) {
            _controllers[i + 1] = new BrainController(this, brains.get(i));
        }

        _setup = new Setup((HumanController) _controllers[0], new Controller[] {_controllers[0], _controllers[1]});
    }

    public Controller[] getControllers() {
        return _controllers;
    }

    public Controller getController(Player player) {
        return _setup.getController(player);
    }

    public void setController(Player player, Controller controller) {
        _setup.setController(player, controller);
        fireSetupChanged(player.index);
    }

}
