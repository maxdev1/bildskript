package org.bildskript.ui.threads;

import java.io.File;
import java.util.Set;
import java.util.regex.Pattern;

import org.bildskript.parser.layout.LayoutFileLoader;
import org.bildskript.ui.threads.workerdialog.WorkerThread;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;

/**
 *
 */
public class LayoutLoadThread extends WorkerThread {

	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		Set<String> syntaxes = new Reflections("components", new ResourcesScanner()).getResources(Pattern.compile(".*"));
		
		if (syntaxes != null) {
			this.callback.progressStarted(syntaxes.size());

			for (String syntax : syntaxes) {
				this.callback.progressContinued(1, syntax);
				LayoutFileLoader.loadSyntax(syntax, this.getClass().getResourceAsStream("/" + syntax));
				System.out.println("Loaded " + syntax);
			}
		}

		this.callback.progressFinished();
	}

}
