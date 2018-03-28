package martijn.quoridor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import martijn.quoridor.brains.BrainFactory;
import martijn.quoridor.brains.DefaultBrainFactory;
import martijn.quoridor.ui.ApplicationFrame;

/**
 * The application's main entry point.
 */
public class QuoridorApplication {

    public static final String VERSION = "2.1";

    /**
     * Launches Quoridor with a {@link DefaultBrainFactory}.
     */
    public static void launch() {
        launch(new DefaultBrainFactory());
    }

    /**
     * Launches Quoridor with the brains created by the specified factory.
     */
    public static void launch(BrainFactory factory) {

        final BrainFactory ffactory = factory;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UIManager.put("swing.boldMetal", false);

                ApplicationFrame f = new ApplicationFrame(ffactory);

                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    /** Launches Quoridor. */
    public static void main(String[] args) {
        launch();
    }

}
