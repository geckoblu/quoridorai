package martijn.quoridor.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class GameModel {

    private final Board _board;

    private final Setup _setup;

    private final List<GameListener> _listeners = new LinkedList<GameListener>();


    public GameModel() {
        _board  = new Board();
        _board.addBoardListener(new GameListener() {
            @Override
            public void newGame() {
                GameModel.this.fireNewGame();
            }
            @Override
            public void moveExecuted() {
                GameModel.this.fireMoveExecuted();
            }
        });

        _setup = null;
    }

    public Board getBoard() {
        return _board;
    }

    public void newGame() {
        _board.newGame();
    }

    public void add(Iterator<Move> moves) {
        _board.add(moves);
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
        _board.undo();
    }

    public void undoAll() {
        _board.undoAll();
    }

    public boolean canRedo() {
        return _board.canRedo();
    }

    public void redo() {
        _board.redo();
    }

    public void redoAll() {
        _board.redoAll();
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

    public Player getTurn() {
        return _board.getTurn();
    }

    /** Causes the listener to be notified of subsequent board events. */
    public void addGameListener(GameListener l) {
        _listeners.add(l);
    }

    /** Causes the listener to no longer be notified of subsequent board events. */
    public void removeGameListener(GameListener l) {
        _listeners.remove(l);
    }

    private void fireNewGame() {
        for (GameListener l : _listeners) {
            l.newGame();
        }
    }

    private void fireMoveExecuted() {
        if (_listeners != null) { // null in cloned board
            for (GameListener l : _listeners) {
                l.moveExecuted();
            }
        }
    }

}
