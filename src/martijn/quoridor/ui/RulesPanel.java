package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import martijn.quoridor.Core;
import martijn.quoridor.I18N;

@SuppressWarnings("serial")
public class RulesPanel extends JPanel {

    public RulesPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 500));

        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        String txt = I18N.getTextFile("rules.html");

        if (txt.equals("")) {
            txt = "WARNING: Could nof find rules file. Check log.";
        }

        textPane.setText(txt);

        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    Core.openHyperlink(RulesPanel.this, e.getURL());
                }
            }
        });

        add(new JScrollPane(textPane), BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        Core.LOGGER.setLevel(Level.WARNING);

        JOptionPane.showMessageDialog(null, new RulesPanel(), I18N.tr("RULES"),
                JOptionPane.PLAIN_MESSAGE);
    }
    /*
    */
}
