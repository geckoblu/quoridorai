package martijn.quoridor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * The Core provides the Logger and some utility methods.
 */
public final class Core {

    private static Component _rootComponent;

    /**
     * The Logger used for logging errors and warnings.
     * */
    public static final Logger LOGGER;

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s\t%2$s - %5$s%6$s%n");

        LOGGER = Logger.getLogger(Core.class.getName());
        LOGGER.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        LOGGER.addHandler(handler);

        String loglevel = System.getProperty("loglevel");
        if (loglevel != null) {
            try {
                Level level = Level.parse(loglevel.toUpperCase());
                LOGGER.setLevel(level);
                LOGGER.log(Level.CONFIG, "Setting log level to " + level);
            } catch (Exception ex) {
               LOGGER.log(Level.WARNING, "Error setting level via -Dloglevel - " + ex.getMessage());
            }
        }

    }

    /* Utility class, no instantiation allowed */
    private Core() {
    }

    /**
     * Returns the same color but with 50% transparency.
     * */
    public static Color transparent(Color c) {
        return transparent(c, 0x4f);
    }

    /**
     * Returns the same color but with the specified alpha value.
     *
     * @param c
     *            the color whose RGB components are used.
     * @param alpha
     *            the alpha value of the created color, between 0 and 255
     *            inclusive.
     */
    public static Color transparent(Color c, int alpha) {
        if (alpha < 0 || alpha >= 256) {
            throw new IllegalArgumentException("Illegal alpha value " + alpha
                    + ". Must be between 0 and 255 inclusive.");
        }
        int rgb = c.getRGB() & 0x00ffffff;
        int a = alpha << 24;
        return new Color(rgb | a, true);
    }

    /**
     * Opens the given URL in the desktop browser if possible, in a dialog box
     * as fallback.
     *
     */
    public static void openHyperlink(Component parent, URL url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException ex) {
                Core.LOGGER.log(Level.WARNING, "Some exception occurs", ex);
            }
        } else {
            JOptionPane.showMessageDialog(parent, I18N.tr("PLEASE_VISIT") + ": " + url.toString(),
                    I18N.tr("OPEN_HYPERLINK"), JOptionPane.PLAIN_MESSAGE);
        }
    }

    public static void setRootCompnent(Component rootComponent) {
        _rootComponent = rootComponent;
    }

    public static Component getRootComponent() {
        return _rootComponent;
    }
}
