package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.model.PointOfView;
import martijn.quoridor.model.Setup;
import martijn.quoridor.ui.actions.RedoAction;
import martijn.quoridor.ui.actions.RedoAllAction;
import martijn.quoridor.ui.actions.UndoAction;
import martijn.quoridor.ui.actions.UndoAllAction;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

    private BoardCanvas _boardCanvas;

    private Controller[] controllers;

    private final Setup setup;
    private final GameStatus gamestatusPanel;

    private HistoryArea historyArea;

    public GamePanel(Board board, BrainFactory factory, StatusBar statusbar) {

        _boardCanvas = new BoardCanvas(board);
        _boardCanvas.addPropertyChangeListener("POINT_OF_VIEW", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
            }
        });

        controllers = getControllers(factory);
        setup = new Setup(board, (HumanController) controllers[0], new Controller[] { controllers[0], controllers[2] });

        gamestatusPanel = new GameStatus(setup, controllers, statusbar);

        historyArea = new HistoryArea(board);

        initUI(board);

        new SoundPlayer(board, setup);
    }

    private Controller[] getControllers(BrainFactory factory) {
        List<Brain> brains = new ArrayList<Brain>();
        factory.addBrains(brains);

        Controller[] controllers = new Controller[brains.size() + 1];
        controllers[0] = new HumanController(_boardCanvas);
        for (int i = 0; i < brains.size(); i++) {
            controllers[i + 1] = new BrainController(_boardCanvas, brains.get(i));
        }
        return controllers;
    }

    private void initUI(Board board) {
        setLayout(new BorderLayout());

        JPanel p1 = new JPanel(new BorderLayout());

        p1.add(_boardCanvas, BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(new JButton(new UndoAllAction(board)));
        buttons.add(new JButton(new UndoAction(board)));
        buttons.add(new JButton(new RedoAction(board)));
        buttons.add(new JButton(new RedoAllAction(board)));
        p1.add(buttons, BorderLayout.SOUTH);

        add(p1, BorderLayout.CENTER);

        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createEtchedBorder());

        p2.add(gamestatusPanel, BorderLayout.NORTH);

        JPanel p3 = new JPanel(new BorderLayout());

        historyArea.setEditable(false);

        p3.add(new JLabel(" "), BorderLayout.NORTH);
        p3.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        p2.add(p3, BorderLayout.CENTER);

        add(p2, BorderLayout.EAST);
    }

    void setPointOfView(PointOfView pointOfView) {
        _boardCanvas.setPointOfView(pointOfView);
    }

}
