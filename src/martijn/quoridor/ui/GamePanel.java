package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.PointOfView;
import martijn.quoridor.ui.actions.RedoAction;
import martijn.quoridor.ui.actions.RedoAllAction;
import martijn.quoridor.ui.actions.UndoAction;
import martijn.quoridor.ui.actions.UndoAllAction;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

    private final GameModel _gameModel;

    private final BoardCanvas _boardCanvas;
    private final GameStatus _gamestatusPanel;
    private final HistoryArea _historyArea;

    public GamePanel(GameModel gameModel) {

        _gameModel = gameModel;

        _boardCanvas = new BoardCanvas(_gameModel);
        _boardCanvas.addPropertyChangeListener("POINT_OF_VIEW", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        _gameModel.initControllers(_boardCanvas);

        _gamestatusPanel = new GameStatus(_gameModel);

        _historyArea = new HistoryArea(_gameModel);

        initUI();

        setKeyBindings();

        new SoundPlayer(_gameModel);
    }



    private void initUI() {
        setLayout(new BorderLayout());

        JPanel p1 = new JPanel(new BorderLayout());

        p1.add(_boardCanvas, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new UndoAllAction(_gameModel)));
        buttons.add(new JButton(new UndoAction(_gameModel)));
        buttons.add(new JButton(new RedoAction(_gameModel)));
        buttons.add(new JButton(new RedoAllAction(_gameModel)));
        p1.add(buttons, BorderLayout.SOUTH);

        add(p1, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createEtchedBorder());

        p2.add(_gamestatusPanel, BorderLayout.NORTH);

        JPanel p3 = new JPanel(new BorderLayout());

        _historyArea.setEditable(false);

        p3.add(new JLabel(" "), BorderLayout.NORTH);
        p3.add(new JScrollPane(_historyArea), BorderLayout.CENTER);
        p2.add(p3, BorderLayout.CENTER);

        add(p2, BorderLayout.EAST);
    }

    private void setKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "UP");
        am.put("UP", new UndoAllAction(_gameModel));

        im.put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
        am.put("DOWN", new RedoAllAction(_gameModel));

        im.put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
        am.put("LEFT", new UndoAction(_gameModel));

        im.put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
        am.put("RIGHT", new RedoAction(_gameModel));
    }

    void setPointOfView(PointOfView pointOfView) {
        _boardCanvas.setPointOfView(pointOfView);
    }

}
