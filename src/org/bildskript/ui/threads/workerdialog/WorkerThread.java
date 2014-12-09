package org.bildskript.ui.threads.workerdialog;

/**
 *
 */
public abstract class WorkerThread extends Thread {

	protected WorkerDialog callback;

	/**
	 * @param callback the callback to set.
	 */
	public void setCallback(WorkerDialog callback) {
		this.callback = callback;
	}
}
