package martijn.quoridor.ui;

import java.awt.BorderLayout;
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
import martijn.quoridor.model.Setup;
import martijn.quoridor.ui.actions.RedoAction;
import martijn.quoridor.ui.actions.UndoAction;


@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private BoardCanvas canvas;

	private Controller[] controllers;

	private final Setup setup;
	private final GameStatus gamestatusPanel;

	private HistoryArea historyArea;

	public GamePanel(Board board, BrainFactory factory, StatusBar statusbar) {

		canvas = new BoardCanvas(board);

		controllers = getControllers(factory);
		setup = new Setup(board, new Controller[] { controllers[0], controllers[1] });

		gamestatusPanel = new GameStatus(setup, controllers, statusbar);

		historyArea = new HistoryArea(board);

		initUI();

		new SoundPlayer(board, setup);
	}

	private Controller[] getControllers(BrainFactory factory) {
		List<Brain> brains = new ArrayList<Brain>();
		factory.addBrains(brains);

		Controller[] controllers = new Controller[brains.size() + 1];
		controllers[0] = new HumanController(canvas);
		for (int i = 0; i < brains.size(); i++) {
			controllers[i + 1] = new BrainController(canvas, brains.get(i));
		}
		return controllers;
	}

	private void initUI() {
		setLayout(new BorderLayout());

		JPanel p1 = new JPanel(new BorderLayout());

		p1.add(canvas, BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		//buttons.add(new JButton(new NewGameAction(board)));
		buttons.add(new JButton(new UndoAction(setup)));
		buttons.add(new JButton(new RedoAction(setup)));
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

}
