package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import martijn.quoridor.brains.Brain;
import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.ui.actions.UndoAction;


@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	public static final int HOR_STRUT = 20;

	private final Board board;

	private BoardCanvas canvas;

	private Controller[] controllers;

	private final Setup setup;

	public GamePanel(Board board, BrainFactory factory) {
		this.board = board;
		canvas = new BoardCanvas(board);
		controllers = getControllers(factory, canvas);
		setup = new Setup(canvas, new Controller[] { controllers[0], controllers[2] });
		initUI();
		new SoundPlayer(board, setup);
	}

	private Controller[] getControllers(BrainFactory factory, BoardCanvas canvas) {
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
		buttons.add(new JButton(new UndoAction(this)));
		//buttons.add(new JButton(new ShowCardAction(combo, ComboPane.ABOUT_CARD, "About")));
		p1.add(buttons, BorderLayout.SOUTH);

		add(p1, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new BorderLayout());
		p2.setBorder(BorderFactory.createEtchedBorder());
		p2.add(new GameStatus(this), BorderLayout.NORTH);

		add(p2, BorderLayout.EAST);
	}

	public Board getBoard() {
		return board;
	}

	public Setup getSetup() {
		return setup;
	}

	public Controller[] getControllers() {
		return controllers;
	}

}
