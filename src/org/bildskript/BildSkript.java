package org.bildskript;

import java.io.IOException;

import javax.swing.UIManager;

import org.bildskript.ui.BoardStudio;
import org.bildskript.ui.threads.LayoutLoadThread;
import org.bildskript.ui.threads.workerdialog.WorkerDialog;

/**
 *
 */
public class BildSkript {

	private BoardStudio ui;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			Style.setDefaults();
		} catch (Exception e) {}

		BildSkript s = new BildSkript();
		s.run();
	}

	/**
	 *
	 */
	public BildSkript() {
		this.ui = new BoardStudio();
	}

	/**
	 *
	 */
	private void run() {
		this.ui.setVisible(true);

		WorkerDialog.perform(new LayoutLoadThread(), this.ui);
	}
}
