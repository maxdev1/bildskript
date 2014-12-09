package org.bildskript.ui.editor;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class HardRefreshTrigger extends Thread {

	private static List<HardRefreshTrigger> previousRefreshers = new ArrayList<HardRefreshTrigger>();

	/**
	 *
	 */
	public static void triggerLater(CircuitEditorArea editor) {

		for (HardRefreshTrigger ref : previousRefreshers) {
			ref.setCanceled(true);
		}

		HardRefreshTrigger trigger = new HardRefreshTrigger(editor);
		previousRefreshers.add(trigger);
		trigger.start();
	}

	private boolean canceled;
	private CircuitEditorArea editor;

	/**
	 *
	 */
	public HardRefreshTrigger(CircuitEditorArea editor) {
		this.editor = editor;
		this.canceled = false;
	}

	/**
	 * @param canceled the canceled to set.
	 */
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}

	@Override
	public void run() {

		try {
			Thread.sleep(500);
		} catch (Exception e) {
			return;
		}

		if (this.canceled) {
			return;
		}

		this.editor.hardRefresh();
	}

}
