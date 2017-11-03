package martijn.quoridor;

import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

public final class Config {

	public static final String SHOWCOORDINATES = "SHOWCOORDINATES";
	public static final String LAMEKNOTATION = "LAMEKNOTATION";

	private static final Config config = new Config();

	private final Properties prop = new Properties();

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

		if (! loaded) { // use defaults
			save();
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

	private static String getConfigFileName() {
		String configFileName = getConfigHome() + File.separator + "quoridorai.properties";

		return configFileName;
	}

	private static String getXdgConfigHome() {
		Map<String,String> environment = System.getenv();

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

	public static final boolean lamekNotation() {

		boolean lamekNotation = true; // default value

		String value = config.prop.getProperty(LAMEKNOTATION);

		if (value == null) {
			config.prop.setProperty(LAMEKNOTATION, Boolean.toString(lamekNotation));			
		} else {
			lamekNotation = Boolean.parseBoolean(value);
		}

		return lamekNotation;
	}
	
	public static final void lamekNotation(boolean lamekNotation) {
		
		boolean oldValue = lamekNotation();

		if (lamekNotation != oldValue) {
			config.prop.setProperty(LAMEKNOTATION, Boolean.toString(lamekNotation));
			config.pcs.firePropertyChange(LAMEKNOTATION, oldValue, lamekNotation);
		}
	
	}

}
