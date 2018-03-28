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
import java.util.logging.Level;

import martijn.quoridor.model.Notation;

public final class Config {

    /*
     * User properties
     */

    public static final String SHOWCOORDINATES = "SHOWCOORDINATES";
    public static final String NOTATION = "NOTATION";

    private static final String LOGLEVEL = "LOGLEVEL";

    /*
     * Cached properties
     */

    private static final String CACHE_LASTLOADPATH = "LAST_LOAD_PATH";

    /*
     * Singleton
     */

    private static final Config CONFIG = new Config();

    /*
     * Instance variables
     */

    private final Properties _prop = new Properties();

    private final Properties _cache = new Properties();

    private final PropertyChangeSupport _pcs = new PropertyChangeSupport(this);


    private Config() {
        File configFile = new File(getConfigFileName());

        if (configFile.exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream(configFile);
                _prop.load(input);
            } catch (IOException ex) {
                Core.log(Level.SEVERE, "Some exception occurs", ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Core.log(Level.SEVERE, "Some exception occurs", ex);
                    }
                }
            }
        }

        Core.setLogLevel(logLevel());

        Core.log(Level.INFO, "Loading properties file: {0}", configFile);

        File cacheFile = new File(getCacheFileName());
        if (cacheFile.exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream(cacheFile);
                _cache.load(input);
            } catch (IOException ex) {
                Core.log(Level.SEVERE, "Some exception occurs", ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Core.log(Level.SEVERE, "Some exception occurs", ex);
                    }
                }
            }
        }

        Core.log(Level.INFO, "Loading cache file: {0}", cacheFile);
    }

    public static void save() {
        File configHome = new File(getConfigHome());
        if (!configHome.exists()) {
            configHome.mkdirs();
        }

        File configFile = new File(getConfigFileName());

        OutputStream output = null;

        try {
            output = new FileOutputStream(configFile);

            CONFIG._prop.store(output, null);

        } catch (IOException ex) {
            Core.log(Level.SEVERE, "Some exception occurs", ex);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Core.log(Level.SEVERE, "Some exception occurs", ex);
                }
            }
        }

        saveCache();

    }

    private static void saveCache() {
        File cacheHome = new File(getCacheHome());
        if (!cacheHome.exists()) {
            cacheHome.mkdirs();
        }

        File cacheFile = new File(getCacheFileName());

        OutputStream output = null;

        try {
            output = new FileOutputStream(cacheFile);

            CONFIG._cache.store(output, null);

        } catch (IOException ex) {
            Core.log(Level.SEVERE, "Some exception occurs", ex);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Core.log(Level.SEVERE, "Some exception occurs", ex);
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

    private Level logLevel() {

        Level logLevel = Level.WARNING; // default value

        String value = _prop.getProperty(LOGLEVEL);

        if (value == null) {
            _prop.setProperty(LOGLEVEL, logLevel.getName());
        } else {
            logLevel = Level.parse(value);
        }

        return logLevel;
    }

    public static boolean showCoordinates() {

        boolean showCoordinates = true; // default value

        String value = CONFIG._prop.getProperty(SHOWCOORDINATES);

        if (value == null) {
            CONFIG._prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
        } else {
            showCoordinates = Boolean.parseBoolean(value);
        }

        return showCoordinates;
    }

    public static void showCoordinates(boolean showCoordinates) {

        boolean oldValue = showCoordinates();

        if (showCoordinates != oldValue) {
            CONFIG._prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
            CONFIG._pcs.firePropertyChange(SHOWCOORDINATES, oldValue, showCoordinates);
        }

    }

    public static Notation notation() {

        Notation notation = Notation.LAMEK; // default value

        String value = CONFIG._prop.getProperty(NOTATION);

        if (value == null) {
            CONFIG._prop.setProperty(NOTATION, notation.toString());
        } else {
            notation = Notation.parse(value);
        }

        return notation;
    }

    public static void notation(Notation notation) {

        Notation oldValue = notation();

        if (notation != oldValue) {
            CONFIG._prop.setProperty(NOTATION, notation.toString());
            CONFIG._pcs.firePropertyChange(NOTATION, oldValue, notation);
        }

    }

    /**
     * Cached properties
     */

    public static String lastLoadPath() {
        return CONFIG._cache.getProperty(CACHE_LASTLOADPATH, ".");
    }

    public static void lastLoadPath(String lastLoadPath) {
        CONFIG._cache.setProperty(CACHE_LASTLOADPATH, lastLoadPath);
        save();
    }


    private static Path _lastLoadFile = null;

    public static Path lastLoadFile() {
        return _lastLoadFile;
    }

    public static void lastLoadFile(Path lastLoadFile) {
        _lastLoadFile = lastLoadFile;
        if (lastLoadFile != null) {
            String lastLoadPath = lastLoadFile.getParent().toString();
            lastLoadPath(lastLoadPath);
        }
    }

}
