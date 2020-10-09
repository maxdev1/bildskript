package org.bildskript.ui;

import java.util.prefs.Preferences;

/**
 *
 */
public class BildSkriptPreferences {

	private static Preferences prefs = Preferences.userRoot();
	private static final String LAST_SELECTED_FILE = "last-selected-file";

	/**
	 * @return
	 */
	public static String getLastSelectedFile() {

		return prefs.get(LAST_SELECTED_FILE, "");
	}

	/**
	 * @param absolutePath
	 */
	public static void setLastSelectedFile(String absolutePath) {
		prefs.put(LAST_SELECTED_FILE, absolutePath);
	}

}
