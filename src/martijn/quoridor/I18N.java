package martijn.quoridor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

public final class I18N {

    public static class Menu {
        public final String label;
        public final char mnemonic;

        Menu(String label, char mnemonic) {
            this.label = label;
            this.mnemonic = mnemonic;
        }
    }

    public static class Action {
        public final String name;
        public final int mnemonicKey;
        public final String shortDescription;

        Action(String name, int mnemonicKey, String shortDescription) {
            this.name = name;
            this.mnemonicKey = mnemonicKey;
            this.shortDescription = shortDescription;
        }
    }

    private static final ResourceBundle RB = ResourceBundle.getBundle("i18n.ResourceBundle");;

    /* Utility class, no instantiation allowed */
    private I18N() {
    }

    public static String tr(String label) {
        try {
            return RB.getString(label);
        } catch (MissingResourceException ex) {
            Core.LOGGER.log(Level.WARNING, ex.getMessage());
            return "*" + label + "*";
        }
    }

    public static I18N.Menu getMenu(String key) {

        String label;
        char mnemonic = ' ';

        try {
            label = RB.getString("MENU." + key + ".LABEL");
        } catch (MissingResourceException ex) {
            Core.LOGGER.log(Level.WARNING, "No label found for MENU." + key + ".LABEL");
            label = "*" + key + "*";
        }

        try {
            String mnem = RB.getString("MENU." + key + ".MNEMONIC");
            if (mnem != null && mnem.length() == 1) {
                mnemonic = mnem.charAt(0);
            } else {
                Core.LOGGER.log(Level.FINE, "No valid mnemonic found for MENU." + key + ".MNEMONIC");
            }
        } catch (MissingResourceException ex) {
            Core.LOGGER.log(Level.FINE, "No mnemonic found for MENU." + key + ".MNEMONIC");
        }

        return new I18N.Menu(label, mnemonic);
    }

    public static I18N.Action getAction(String key) {

        String name;
        int mnemonicKey = '\0';
        String shortDescription = "";

        // Name
        try {
            name = RB.getString("ACTION." + key + ".NAME");
        } catch (MissingResourceException ex) {
            Core.LOGGER.log(Level.WARNING, ex.getMessage());
            name = "*" + key + "*";
        }

        // Mnemonic Key
        try {
            String mnem = RB.getString("ACTION." + key + ".MNEMONIC_KEY");
            if (mnem != null && mnem.length() == 1) {
                mnemonicKey = mnem.charAt(0);
            } else {
                Core.LOGGER.log(Level.FINE, "No valid mnemonic found for ACTION." + key + ".MNEMONIC_KEY");
            }
        } catch (MissingResourceException ex) {
            Core.LOGGER.log(Level.FINE, "No mnemonic found for ACTION." + key + ".MNEMONIC_KEY");
        }

        // Short description
        try {
            shortDescription = RB.getString("ACTION." + key + ".SHORT_DESCRIPTION");
        } catch (MissingResourceException ex) {
            // Ignore
        }

        return new I18N.Action(name, mnemonicKey, shortDescription);
    }

    public static String getTextFile(String filename) {
        String text = "";
        String language = java.util.Locale.getDefault().getLanguage();

        int dot = filename.lastIndexOf('.');
        String base = (dot == -1) ? filename : filename.substring(0, dot);
        String extension = (dot == -1) ? "" : filename.substring(dot + 1);

        String localFilename = base + "-" + language;
        if (dot > -1) {
            localFilename = localFilename + "." + extension;
        }

        String fn = "/i18n/" + localFilename;

        Core.LOGGER.log(Level.CONFIG, "Looking for: {0}", fn);
        InputStream is = I18N.class.getResourceAsStream(fn);

        if (is == null) {
            fn = "/i18n/" + filename;
            Core.LOGGER.log(Level.CONFIG, "Looking for: {0}", fn);
            is = I18N.class.getResourceAsStream(fn);

            if (is == null) {
                Core.LOGGER.log(Level.SEVERE, "Could not find file: {0}", filename);
                return text;
            }
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
        } catch (IOException e) {
            Core.LOGGER.log(Level.SEVERE, "Could not read file.", e);
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                Core.LOGGER.log(Level.WARNING, "Problem closing BufferedReader.", e);
            }
        }
        text = sb.toString();

        return text;
    }

}
