package martijn.quoridor;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

import martijn.quoridor.model.Notation;

public final class Config {

    public static final String SHOWCOORDINATES = "SHOWCOORDINATES";
    public static final String NOTATION = "NOTATION";

    private static final Config config = new Config();

    private final Properties prop = new Properties();

    private final Properties cache = new Properties();

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private Config() {
        File configFile = new File(getConfigFileName());

        boolean loaded = false;

        if (configFile.exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream(configFile);
                prop.load(input);
                loaded = true;
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        if (!loaded) { // use defaults
            save();
        }

        File cacheFile = new File(getCacheFileName());
        if (cacheFile.exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream(cacheFile);
                cache.load(input);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    public static final void save() {
        File configHome = new File(getConfigHome());
        if (!configHome.exists()) {
            configHome.mkdirs();
        }

        File configFile = new File(getConfigFileName());

        OutputStream output = null;

        try {
            output = new FileOutputStream(configFile);

            config.prop.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private static final void saveCache() {
        File cacheHome = new File(getCacheHome());
        if (!cacheHome.exists()) {
            cacheHome.mkdirs();
        }

        File cacheFile = new File(getCacheFileName());

        OutputStream output = null;

        try {
            output = new FileOutputStream(cacheFile);

            config.cache.store(output, null);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    private static String getConfigFileName() {
        String configFileName = getConfigHome() + File.separator + "quoridorai.properties";

        return configFileName;
    }

    private static String getXdgConfigHome() {
        Map<String, String> environment = System.getenv();

        String xdgConfigHome = environment.get("XDG_CONFIG_HOME");

        if (xdgConfigHome == null || xdgConfigHome.trim().length() == 0) {

            String home = environment.get("HOME");

            if (home == null || home.trim().length() == 0) {
                home = System.getProperty("user.home");
            }

            xdgConfigHome = home + File.separator + ".config";
        }

        return xdgConfigHome;
    }

    private static String getConfigHome() {
        String configHome = getXdgConfigHome() + File.separator + "quoridorai";

        return configHome;
    }

    private static String getCacheFileName() {
        String cacheFileName = getCacheHome() + File.separator + "quoridorai.properties";

        return cacheFileName;
    }

    private static String getXdgCachegHome() {
        Map<String, String> environment = System.getenv();

        String xdgCacheHome = environment.get("XDG_CACHE_HOME");

        if (xdgCacheHome == null || xdgCacheHome.trim().length() == 0) {

            String home = environment.get("HOME");

            if (home == null || home.trim().length() == 0) {
                home = System.getProperty("user.home");
            }

            xdgCacheHome = home + File.separator + ".cache";
        }

        return xdgCacheHome;
    }

    private static String getCacheHome() {
        String cacheHome = getXdgCachegHome() + File.separator + "quoridorai";

        return cacheHome;
    }

    public static final boolean showCoordinates() {

        boolean showCoordinates = true; // default value

        String value = config.prop.getProperty(SHOWCOORDINATES);

        if (value == null) {
            config.prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
        } else {
            showCoordinates = Boolean.parseBoolean(value);
        }

        return showCoordinates;
    }

    public static final void showCoordinates(boolean showCoordinates) {

        boolean oldValue = showCoordinates();

        if (showCoordinates != oldValue) {
            config.prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
            config.pcs.firePropertyChange(SHOWCOORDINATES, oldValue, showCoordinates);
        }

    }

    public static final Notation notation() {

        Notation notation = Notation.LAMEK; // default value

        String value = config.prop.getProperty(NOTATION);

        if (value == null) {
            config.prop.setProperty(NOTATION, notation.toString());
        } else {
            notation = Notation.parse(value);
        }

        return notation;
    }

    public static final void notation(Notation notation) {

        Notation oldValue = notation();

        if (notation != oldValue) {
            config.prop.setProperty(NOTATION, notation.toString());
            config.pcs.firePropertyChange(NOTATION, oldValue, notation);
        }

    }

    /*
     * Cached properties
     */

    private static final String LAST_LOAD_PATH = "LAST_LOAD_PATH";

    public static final String lastLoadPath() {
        return config.cache.getProperty(LAST_LOAD_PATH, ".");
    }

    public static final void lastLoadPath(String lastLoadPath) {
        config.cache.setProperty(LAST_LOAD_PATH, lastLoadPath);
        saveCache();
    }


    private static Path _lastLoadFile = null;

    public static final Path lastLoadFile() {
        return _lastLoadFile;
    }

    public static final void lastLoadFile(Path lastLoadFile) {
        _lastLoadFile = lastLoadFile;
        if (lastLoadFile != null) {
            String lastLoadPath = lastLoadFile.getParent().toString();
            lastLoadPath(lastLoadPath);
        }
    }

}
