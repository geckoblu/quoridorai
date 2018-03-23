package martijn.quoridor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

public final class I18N {

    public static final class Menu {
        public String label;
        public char mnemonic = ' ';
    }

    public static final class Action {
        public String name;
        public int mnemonic_key = '\0';
        public String short_description = null;
    }

    // private static final I18N i18n = new I18N();

    private static final ResourceBundle rb = ResourceBundle.getBundle("i18n.ResourceBundle");;

    private I18N() {
        // No instantiation allowed
    }

    public static final String tr(String label) {
        try {
            return rb.getString(label);
        } catch (MissingResourceException ex) {
            System.err.println(ex.getMessage());
            return "*" + label + "*";
        }
    }

    public static final I18N.Menu getMenu(String key) {

        I18N.Menu menu = new I18N.Menu();

        try {
            menu.label = rb.getString("MENU." + key + ".LABEL");
        } catch (MissingResourceException ex) {
            System.err.println(ex.getMessage());
            menu.label = "*" + key + "*";
        }

        try {
            String mnem = rb.getString("MENU." + key + ".MNEMONIC");
            if (mnem != null && mnem.length() == 1) {
                menu.mnemonic = mnem.charAt(0);
            } else {
                System.err.println("No mnemonic found for MENU." + key + ".MNEMONIC");
            }
        } catch (MissingResourceException ex) {
            System.err.println(ex.getMessage());
        }

        return menu;
    }

    public static final I18N.Action getAction(String key) {

        I18N.Action action = new I18N.Action();

        // Name
        try {
            action.name = rb.getString("ACTION." + key + ".NAME");
        } catch (MissingResourceException ex) {
            System.err.println(ex.getMessage());
            action.name = "*" + key + "*";
        }

        // Mnemonic Key
        try {
            String mnem = rb.getString("ACTION." + key + ".MNEMONIC_KEY");
            if (mnem != null && mnem.length() == 1) {
                action.mnemonic_key = mnem.charAt(0);
            } else {
                System.err.println("No mnemonic found for ACTION." + key + ".MNEMONIC_KEY");
            }
        } catch (MissingResourceException ex) {
            System.err.println(ex.getMessage());
        }

        // Short description
        try {
            action.short_description = rb.getString("ACTION." + key + ".SHORT_DESCRIPTION");
        } catch (MissingResourceException ex) {
            // Ignore
        }

        return action;
    }

    public static String getTextFile(String filename) {
        String text = "";
        String language = java.util.Locale.getDefault().getLanguage();

        int dot = filename.lastIndexOf('.');
        String base = (dot == -1) ? filename : filename.substring(0, dot);
        String extension = (dot == -1) ? "" : filename.substring(dot+1);

        String localFilename = base + "-" + language;
        if (dot > -1) {
            localFilename = localFilename + "." + extension;
        }

        Core.LOGGER.log(Level.INFO, "Looking for " + localFilename);
        URL url = filename.getClass().getResource("/i18n/" + localFilename);

        if (url == null) {
            Core.LOGGER.log(Level.INFO, "Looking for " + filename);
            url = filename.getClass().getResource("/i18n/" + filename);
        }

        if (url == null) {
            Core.LOGGER.log(Level.WARNING, "Could not find file.");

        } else {

            try(BufferedReader br = new BufferedReader(new FileReader(new File(url.toURI())))) {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    line = br.readLine();
                }
                text = sb.toString();
            } catch (FileNotFoundException e) {
                Core.LOGGER.log(Level.WARNING, "Could not find file.");
            } catch (IOException e) {
                Core.LOGGER.log(Level.WARNING, "Could not read file.", e);
            } catch (URISyntaxException e1) {
                Core.LOGGER.log(Level.SEVERE, "URISyntaxException", e1);
        }
        }
        return text;
    }

}
