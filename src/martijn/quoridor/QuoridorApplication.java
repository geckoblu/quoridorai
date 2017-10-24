/*
 * Created on Aug 4, 2006 
 */
package martijn.quoridor;

import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.model.Board;
import martijn.quoridor.ui.ApplicationFrame;

/**
 * The application's main entry point.
 * @author Martijn van Steenbergen
 * @author Alessio Piccoli
 */
public class QuoridorApplication {

	/** Launches Quoridor with a {@link DefaultBrainFactory}. */
	public static void launch() {
		launch(new DefaultBrainFactory());
	}

	/** Launches Quoridor with the brains created by the specified factory. */
	public static void launch(BrainFactory factory) {
		Board board = new Board();
		ApplicationFrame f = new ApplicationFrame(board, factory);
		
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}

	/** Launches Quoridor. */
	public static void main(String[] args) {
		launch();
	}

}
