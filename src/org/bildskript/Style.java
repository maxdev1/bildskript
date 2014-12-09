package org.bildskript;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;

/**
 * @author Max
 */
public class Style {

	private static final String BEST_SYS_FONT = "Segoe UI";

	public static final Font normalFont = new Font(BEST_SYS_FONT, Font.PLAIN, 12);
	public static final Font boldFont = new Font(BEST_SYS_FONT, Font.BOLD, 12);
	public static final Font statusFont = new Font(BEST_SYS_FONT, Font.PLAIN, 11);
	public static final Font statusFontInFriendlist = new Font(BEST_SYS_FONT, Font.ITALIC, 10);
	public static final Font notificationButtonFont = new Font(BEST_SYS_FONT, Font.PLAIN, 14);
	public static final Font notificationTypeFont = new Font(BEST_SYS_FONT, Font.PLAIN, 10);
	public static final Font flyoutTitleFont = new Font(BEST_SYS_FONT, Font.PLAIN, 14);

	/**
	 *
	 */
	public static void setDefaults() {
		UIDefaults defaults = UIManager.getDefaults();
		Style.setDefaultFont(defaults);
	}

	/**
	 * @param defaults
	 */
	private static void setDefaultFont(UIDefaults defaults) {
		defaults.put("OptionPane.buttonFont", normalFont);
		defaults.put("List.font", normalFont);
		defaults.put("TableHeader.font", normalFont);
		defaults.put("Panel.font", normalFont);
		defaults.put("TextArea.font", normalFont);
		defaults.put("ToggleButton.font", normalFont);
		defaults.put("ComboBox.font", normalFont);
		defaults.put("ScrollPane.font", normalFont);
		defaults.put("Spinner.font", normalFont);
		defaults.put("RadioButtonMenuItem.font", normalFont);
		defaults.put("Slider.font", normalFont);
		defaults.put("EditorPane.font", normalFont);
		defaults.put("OptionPane.font", normalFont);
		defaults.put("ToolBar.font", normalFont);
		defaults.put("Tree.font", normalFont);
		defaults.put("CheckBoxMenuItem.font", normalFont);
		defaults.put("TitledBorder.font", normalFont);
		defaults.put("FileChooser.listFont", normalFont);
		defaults.put("Table.font", normalFont);
		defaults.put("MenuBar.font", normalFont);
		defaults.put("PopupMenu.font", normalFont);
		defaults.put("Label.font", normalFont);
		defaults.put("MenuItem.font", normalFont);
		defaults.put("MenuItem.acceleratorFont", normalFont);
		defaults.put("TextField.font", normalFont);
		defaults.put("TextPane.font", normalFont);
		defaults.put("CheckBox.font", normalFont);
		defaults.put("ProgressBar.font", normalFont);
		defaults.put("FormattedTextField.font", normalFont);
		defaults.put("CheckBoxMenuItem.acceleratorFont", normalFont);
		defaults.put("Menu.acceleratorFont", normalFont);
		defaults.put("ColorChooser.font", normalFont);
		defaults.put("Menu.font", normalFont);
		defaults.put("PasswordField.font", normalFont);
		defaults.put("InternalFrame.titleFont", normalFont);
		defaults.put("OptionPane.messageFont", normalFont);
		defaults.put("RadioButtonMenuItem.acceleratorFont", normalFont);
		defaults.put("Viewport.font", normalFont);
		defaults.put("TabbedPane.font", normalFont);
		defaults.put("RadioButton.font", normalFont);
		defaults.put("ToolTip.font", normalFont);
		defaults.put("Button.font", normalFont);
		defaults.put("Application.useSystemFontSettings", normalFont);
	}
}
