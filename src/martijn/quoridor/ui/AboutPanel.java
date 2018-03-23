package martijn.quoridor.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import martijn.quoridor.Core;
import martijn.quoridor.I18N;
import martijn.quoridor.QuoridorApplication;

@SuppressWarnings("serial")
public class AboutPanel extends JPanel {

    public AboutPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 500));

        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.add(getAbout());
        tabbedPane.add(getOldAbout());
    }

    private Component getAbout() {
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");
        String txt = I18N.getTextFile("about.html");

        if (txt.equals("")) {
            txt = "WARNING: Could nof find about file. Check log.";
        } else {
            URL url = getClass().getResource("/icons/quoridor.png");
            txt = txt.replace("%SRC_ICON%", url.toString());
            txt = txt.replace("%VERSION%", QuoridorApplication.VERSION);
        }

        textPane.setText(txt);

        textPane.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    Core.openHyperlink(AboutPanel.this, e.getURL());
                }
            }
        });

        return new JScrollPane(textPane);
    }

    private Component getOldAbout() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        String txt = I18N.getTextFile("about-old");

        if (txt.equals("")) {
            txt = "WARNING: Could nof find about file. Check log.";
        }

        area.setText(txt);
        area.setEditable(false);

        return new JScrollPane(area);
    }

    /*
    public static void main(String[] args) {
        Core.LOGGER.setLevel(Level.INFO);

        JOptionPane.showMessageDialog(null, new AboutPanel(), I18N.tr("ABOUT_QUORIDORAI"),
                JOptionPane.PLAIN_MESSAGE);
    }
    */
}
