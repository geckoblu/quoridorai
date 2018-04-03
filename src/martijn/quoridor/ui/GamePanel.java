package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.model.GameModel;
import martijn.quoridor.model.PointOfView;
import martijn.quoridor.model.Setup;
import martijn.quoridor.ui.actions.RedoAction;
import martijn.quoridor.ui.actions.RedoAllAction;
import martijn.quoridor.ui.actions.UndoAction;
import martijn.quoridor.ui.actions.UndoAllAction;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

    private GameModel _gameModel;

    private BoardCanvas _boardCanvas;

    private Controller[] _controllers;

    private final Setup _setup;
    private final GameStatus _gamestatusPanel;

    private HistoryArea _historyArea;

    public GamePanel(GameModel gameModel) {

        _gameModel = gameModel;

        _boardCanvas = new BoardCanvas(_gameModel);
        _boardCanvas.addPropertyChangeListener("POINT_OF_VIEW", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        _controllers = getControllers();
        _setup = new Setup(_gameModel.getBoard(), (HumanController) _controllers[0], new Controller[] {_controllers[0], _controllers[2]});

        _gamestatusPanel = new GameStatus(_setup, _controllers);

        _historyArea = new HistoryArea(_gameModel);

        initUI();

        setKeyBindings();

        new SoundPlayer(_gameModel, _setup);
    }

    private Controller[] getControllers() {
        BrainFactory factory = new DefaultBrainFactory();

        List<Brain> brains = new ArrayList<Brain>();
        factory.addBrains(brains);

        Controller[] controllers = new Controller[brains.size() + 1];
        controllers[0] = new HumanController(_gameModel.getBoard(), _boardCanvas);
        for (int i = 0; i < brains.size(); i++) {
            controllers[i + 1] = new BrainController(_gameModel.getBoard(), brains.get(i));
        }
        return controllers;
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
