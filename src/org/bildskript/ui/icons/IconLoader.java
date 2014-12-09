package org.bildskript.ui.icons;

import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 *
 */
public class IconLoader {

	private static final IconLoader INSTANCE = new IconLoader();

	private static HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();

	/**
	 *
	 */
	private IconLoader() {}

	/**
	 * @return the instance
	 */
	public static IconLoader getInstance() {
		return INSTANCE;
	}

	/**
	 *
	 */
	public ImageIcon getImage(String name) {
		if (cache.containsKey(name)) {
			return cache.get(name);
		}

		ImageIcon imageIcon;
		try {
			imageIcon = new ImageIcon(IconLoader.class.getResource("resources/" + name));
		} catch (Exception e) {
			imageIcon = new ImageIcon();
		}
		cache.put(name, imageIcon);
		return imageIcon;
	}
}
