package org.bildskript.ui.threads;

import java.io.File;

import org.bildskript.parser.layout.LayoutFileLoader;
import org.bildskript.ui.threads.workerdialog.WorkerThread;

/**
 *
 */
public class LayoutLoadThread extends WorkerThread {

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		File[] syntaxes = new File("components").listFiles();

		if (syntaxes != null) {
			this.callback.progressStarted(syntaxes.length);

			for (File f : syntaxes) {
				this.callback.progressContinued(1, f.getAbsolutePath());
				LayoutFileLoader.loadSyntax(f);
			}
		}

		this.callback.progressFinished();
	}

}
