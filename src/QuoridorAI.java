import javax.swing.SwingUtilities;

import martijn.quoridor.ui.ApplicationFrame;

/**
 * In default package to show "QuoridorAI" as application name.
 */
public final class QuoridorAI {

    private QuoridorAI() { };

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ApplicationFrame f = new ApplicationFrame();

                if (!Boolean.getBoolean("donotcenter")) {
                    f.setLocationRelativeTo(null);
                }
                f.setVisible(true);
            }
        });

    }

}
