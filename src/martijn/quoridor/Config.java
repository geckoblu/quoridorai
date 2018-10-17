package martijn.quoridor;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import martijn.quoridor.model.Notation;

public final class Config {

    /*
     * User properties
     */
    private static final String NOTATION = "NOTATION";
    private static final String SHOWCOORDINATES = "SHOWCOORDINATES";
    private static final String LASTLOADPATH = "LAST_LOAD_PATH";
    private static final String LASTLOADFILE = "LAST_LOAD_FILE";
    private static final String LASTLOADFILE_ = "LAST_LOAD_FILE_";
    private static final String LASTLOADFILE2 = "LAST_LOAD_FILE_2";
    private static final String LASTLOADFILE3 = "LAST_LOAD_FILE_3";
    private static final String LASTLOADFILE4 = "LAST_LOAD_FILE_4";
    private static final String LASTLOADFILE5 = "LAST_LOAD_FILE_5";
    private static final String PLAYSOUNDS = "PLAYSOUNDS";
    private static final String BRAIN = "BRAIN";

    /**
     * Singleton
     */
    private static final Config THIS = new Config();

    /*
     * Instance variables
     */
    private final Properties _prop = new Properties();
    private final PropertyChangeSupport _pcs = new PropertyChangeSupport(this);

    private Config() {
        File configFile = new File(getConfigFileName());

        if (configFile.exists()) {
            InputStream input = null;
            try {
                input = new FileInputStream(configFile);
                _prop.load(input);
            } catch (IOException ex) {
                Core.LOGGER.log(Level.SEVERE, "Some exception occurs", ex);
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException ex) {
                        Core.LOGGER.log(Level.SEVERE, "Some exception occurs", ex);
                    }
                }
            }
        }

        Core.LOGGER.log(Level.CONFIG, "Loading properties file: {0}", configFile);

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

            THIS._prop.store(output, null);

        } catch (IOException ex) {
            Core.LOGGER.log(Level.SEVERE, "Some exception occurs", ex);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Core.LOGGER.log(Level.SEVERE, "Some exception occurs", ex);
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

    public static boolean getShowCoordinates() {

        boolean showCoordinates = true; // default value

        String value = THIS._prop.getProperty(SHOWCOORDINATES);

        if (value == null) {
            THIS._prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
        } else {
            showCoordinates = Boolean.parseBoolean(value);
        }

        return showCoordinates;
    }

    public static void setShowCoordinates(boolean showCoordinates) {

        boolean oldValue = getShowCoordinates();

        if (showCoordinates != oldValue) {
            THIS._prop.setProperty(SHOWCOORDINATES, Boolean.toString(showCoordinates));
            THIS._pcs.firePropertyChange(SHOWCOORDINATES, oldValue, showCoordinates);
        }

    }

    public static boolean getPlaySounds() {

        boolean playSounds = true; // default value

        String value = THIS._prop.getProperty(PLAYSOUNDS);

        if (value == null) {
            THIS._prop.setProperty(PLAYSOUNDS, Boolean.toString(playSounds));
        } else {
            playSounds = Boolean.parseBoolean(value);
        }

        return playSounds;
    }

    public static void setPlaySounds(boolean playSounds) {

        boolean oldValue = getPlaySounds();

        if (playSounds != oldValue) {
            THIS._prop.setProperty(PLAYSOUNDS, Boolean.toString(playSounds));
            THIS._pcs.firePropertyChange(PLAYSOUNDS, oldValue, playSounds);
        }

    }

    public static Notation getNotation() {

        Notation notation = Notation.LAMEK; // default value

        String value = THIS._prop.getProperty(NOTATION);

        if (value == null) {
            THIS._prop.setProperty(NOTATION, notation.toString());
        } else {
            notation = Notation.parse(value);
        }

        return notation;
    }

    public static void setNotation(Notation notation) {

        Notation oldValue = getNotation();

        if (notation != oldValue) {
            THIS._prop.setProperty(NOTATION, notation.toString());
            THIS._pcs.firePropertyChange(NOTATION, oldValue, notation);
        }

    }

    public static String getBrain(int player) {

        String brainName; // default value
        if (player == 0) {
            brainName = "HUMAN";
        } else {
            brainName = "SMARTBRAIN 2";
        }

        String value = THIS._prop.getProperty(BRAIN + player);

        if (value == null) {
            THIS._prop.setProperty(BRAIN + player, "" + brainName);
        } else {
            brainName = value;
        }

        return brainName;
    }

    public static void setBrain(int player, String brainName) {

        String oldValue = getBrain(player);

        if (brainName != oldValue) {
            THIS._prop.setProperty(BRAIN + player, brainName);
        }

    }

    public static String getLastLoadPath() {
        return THIS._prop.getProperty(LASTLOADPATH, ".");
    }

    public static void setLastLoadPath(String lastLoadPath) {

        String oldValue = getLastLoadPath();

        if (lastLoadPath != oldValue) {
            THIS._prop.setProperty(LASTLOADPATH, lastLoadPath);
            save();
        }
    }

    public static File getLastLoadFile() {
        String llf = THIS._prop.getProperty(LASTLOADFILE, null);
        if (llf != null) {
            return new File(llf);
        } else {
            return null;
        }
    }

    public static void setLastLoadFile(File lastLoadFile) {

        File oldValue = getLastLoadFile();

        if (lastLoadFile != oldValue) {

            if (lastLoadFile != null) {
                pushLastLoadFiles(lastLoadFile);

                THIS._prop.setProperty(LASTLOADFILE, lastLoadFile.toString());
                String lastLoadPath = lastLoadFile.getParent().toString();
                setLastLoadPath(lastLoadPath);

            } else {
                THIS._prop.remove(LASTLOADFILE);
            }
            save();
        }
    }

    public static List<File> getLastLoadFiles() {
        List<File> fileList = new ArrayList<File>();
        File f;
        String llf;

        f = getLastLoadFile();
        if (f != null && f.exists()) {
            fileList.add(f);
        }

        llf = THIS._prop.getProperty(LASTLOADFILE2, null);
        if (llf != null) {
            f = new File(llf);
            if (f.exists()) {
                fileList.add(f);
            }
        }

        llf = THIS._prop.getProperty(LASTLOADFILE3, null);
        if (llf != null) {
            f = new File(llf);
            if (f.exists()) {
                fileList.add(f);
            }
        }

        if (fileList.size() >= 3) {
            return fileList;
        }

        llf = THIS._prop.getProperty(LASTLOADFILE4, null);
        if (llf != null) {
            f = new File(llf);
            if (f.exists()) {
                fileList.add(f);
            }
        }

        if (fileList.size() >= 3) {
            return fileList;
        }

        llf = THIS._prop.getProperty(LASTLOADFILE5, null);
        if (llf != null) {
            f = new File(llf);
            if (f.exists()) {
                fileList.add(f);
            }
        }

        return fileList;
    }

    public static void pushLastLoadFiles(File lastLoadFile) {
        List<File> fileList = getLastLoadFiles();

        fileList.remove(lastLoadFile);

        THIS._prop.remove(LASTLOADFILE2);
        THIS._prop.remove(LASTLOADFILE3);
        THIS._prop.remove(LASTLOADFILE4);
        THIS._prop.remove(LASTLOADFILE5);

        int i = 2;
        for(File file: fileList) {
            THIS._prop.setProperty(LASTLOADFILE_ + i, file.toString());
            i = i + 1;
            if (i > 5) {
                break;
            }
        }
    }

}
